package com.cobbler.eladmin.wechat.base.service;

import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.common.utils.BException;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.app.entity.sucai.Material;
import com.thinkgem.jeesite.modules.app.entity.sucai.Picture;
import com.thinkgem.jeesite.modules.app.entity.wx.WxCustomMenu.Menu;
import com.thinkgem.jeesite.modules.app.service.sucai.MaterialService;
import com.thinkgem.jeesite.modules.app.service.sucai.NewsService;
import com.thinkgem.jeesite.modules.app.service.sucai.PictureService;
import com.thinkgem.jeesite.modules.app.service.wx.WxCustomMenuService;
import com.thinkgem.jeesite.modules.wechat.commonUtil.WeChatUtils;
import com.thinkgem.jeesite.modules.wechat.commonUtil.WxConstants.DownLoadMaterial;
import com.thinkgem.jeesite.modules.wechat.commonUtil.WxConstants.MaterialType;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialFileBatchGetResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNews;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.builder.outxml.BaseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信菜单
 * 
 * @author weilecai
 *
 */
@Service
public class WXMenuService {

    private Logger logger = LoggerFactory.getLogger(WXMenuService.class);
    @Autowired
    private WxCustomMenuService wxCustomMenuService;
    @Autowired
    private WxMpService wxService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private WeChatUtils weChatUtils;

    /**
     * 获取微信菜单
     * 
     * @return
     */

    public Menu getMenu() {
        JSONObject jsonObject = wxCustomMenuService.getAllMenuForWX();
        @SuppressWarnings("static-access")
        Menu menu = jsonObject.toJavaObject(jsonObject, Menu.class);
        return menu;
    }

    /**
     * 
     * 创建微信菜单
     * 
     * @param
     * @return String
     * @throws WxErrorException
     *
     */
    @Transactional(readOnly = false)
    public String menuCreate() throws WxErrorException {
        JSONObject jsonObject = wxCustomMenuService.getAllMenuForWX();
        logger.info("获取微信菜单数据 ：" + jsonObject);
        String strJson = jsonObject.toString();
        // 创建菜单，不用接受返回值，有异常直接在controller处理
        return this.wxService.getMenuService().menuCreate(strJson);

    }

    /**
     * 菜单跳转链接事件
     * 
     * @param openid
     * @param url
     */
    public void doView(WxMpXmlMessage wxMessage, String url) {
        // TODO 追加 openid 参数，其实就是网页授权代码

    }

    /**
     * 菜单点击事件
     * 
     * @param wxMessage
     * @param key
     */
    @SuppressWarnings("unused")
    public BaseBuilder doClick(WxMpXmlMessage wxMessage, String key) throws BException {
        if (StringUtils.isEmpty(key)) {
            throw BException.E("key不能为空");
        }
        Material material = materialService.get(key);
        String mediaId = material.getMediaId();
        // 判断是否存在对应的素材
        if (null == material) {
            throw BException.E("未找到对应素材");
        }
        // 类型(text:文本，image:图片, voice:语音,video:视频，music:音乐,news:图文)
        if ("news".equals(material.getType())) {
            WxMpMaterialNews wxMpMaterialNews = null;
            // 检查微信媒体库是否存在该素材
         //   Boolean result = checkWeChatMediaPool(mediaId);
//            if (result) {
//            }
            // 调取微信端图文永久素材，只适用于获取永久图文素材，(如果是image、text、voice、video、music请绕路)
            wxMpMaterialNews = weChatUtils.getMaterialInfo(mediaId);
            if (null == wxMpMaterialNews) {
                throw BException.E("从微信媒体库没有找到该媒体ID对应的素材！");
            }
            List<WxMpXmlOutNewsMessage.Item> items = new ArrayList<WxMpXmlOutNewsMessage.Item>();
            wxMpMaterialNews.getArticles().forEach((articles) -> {
                WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
                item.setTitle(articles.getTitle());
                item.setDescription(articles.getDigest());
                item.setPicUrl(articles.getThumbUrl());
                item.setUrl(articles.getUrl());
                items.add(item);
            });
            return WxMpXmlOutMessage.NEWS().articles(items);
        }
        else if ("text".equals(material.getType())) {
            return WxMpXmlOutMessage.TEXT().content(material.getContent());
        }
        else if ("image".equals(material.getType())) {
            Picture picture = pictureService.get(material.getPictureId());
            if (picture == null) {
                throw BException.E("未找到图片素材");
            }
            return WxMpXmlOutMessage.IMAGE().mediaId(picture.getMediaId());
        }
        else if ("voice".equals(material.getType())) {
            return null;
        }
        else if ("music".equals(material.getType())) {
            return null;
        }
        return null;
    }

    /**
     * 检查微信媒体库是否存在该素材
     * 
     * @author cao_wencao
     * @param materialType
     * @return
     */
    private Boolean checkWeChatMediaPool(String mediaId) {
        Boolean result = false;
        // 获取其他媒体素材列表接收实体类 图片（image）、视频（video）、语音 （voice）、图文（news）
        WxMpMaterialFileBatchGetResult wxMpMaterialFileBatchGetResult = null;
        // 获取图文素材总数量
        String news_count = weChatUtils.materialCount(MaterialType.NEWS);
        if (!StringUtils.isBlank(news_count)) {
            int totleCount = Integer.parseInt(news_count);
            // 获取图文素材内容细节
            wxMpMaterialFileBatchGetResult = weChatUtils.materialFileBatchGet(MaterialType.NEWS, DownLoadMaterial.OFFECT, totleCount);
            if (StringUtils.isBlank(wxMpMaterialFileBatchGetResult.toString())) {
                throw BException.E("从微信素材库获取图文素材为空");
            }
        }
        List<WxMaterialFileBatchGetNewsItem> itemList = wxMpMaterialFileBatchGetResult.getItems();
        for (int i = 0; i < wxMpMaterialFileBatchGetResult.getTotalCount(); i++) {
            WxMaterialFileBatchGetNewsItem wxMaterialFileBatchGetNewsItem = itemList.get(i);
            // 获取图文素材mediaId
            String wxMediaId = wxMaterialFileBatchGetNewsItem.getMediaId();
            if (!StringUtils.isBlank(wxMediaId) && StringUtils.equals(mediaId, wxMediaId)) {
                result = true;
                return result;
            }
        }
        return result;
    }

}

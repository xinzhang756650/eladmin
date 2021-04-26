package com.cobbler.eladmin.wechat.base.service;

import com.thinkgem.jeesite.common.utils.BException;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.app.entity.sucai.Material;
import com.thinkgem.jeesite.modules.app.entity.sucai.Picture;
import com.thinkgem.jeesite.modules.app.entity.wx.AutoReply;
import com.thinkgem.jeesite.modules.app.entity.wx.KeywordReply;
import com.thinkgem.jeesite.modules.app.entity.wx.SubscribeReply;
import com.thinkgem.jeesite.modules.app.service.sucai.MaterialService;
import com.thinkgem.jeesite.modules.app.service.sucai.NewsService;
import com.thinkgem.jeesite.modules.app.service.sucai.PictureService;
import com.thinkgem.jeesite.modules.app.service.wx.AutoReplyService;
import com.thinkgem.jeesite.modules.app.service.wx.KeywordReplyService;
import com.thinkgem.jeesite.modules.app.service.wx.SubscribeReplyService;
import com.thinkgem.jeesite.modules.wechat.commonUtil.WeChatUtils;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNews;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.builder.outxml.BaseBuilder;
import me.chanjar.weixin.mp.builder.outxml.ImageBuilder;
import me.chanjar.weixin.mp.builder.outxml.NewsBuilder;
import me.chanjar.weixin.mp.builder.outxml.TextBuilder;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信素材
 *
 * @author weilecai
 */
@Service
public class WXMaterialService {

    @Autowired
    MaterialService materialService;

    @Autowired
    SubscribeReplyService subscribeReplyService;
    @Autowired
    PictureService pictureService;
    @Autowired
    NewsService newsService;
    @Autowired
    AutoReplyService autoReplyService;
    @Autowired
    KeywordReplyService keywordReplyService;
    @Autowired
    WeChatUtils weChatUtils;

    public BaseBuilder getMaterial(String materialId, WxMpUser user) throws BException {
        if (materialId == null)
            throw BException.E("素材ID为空");
        // 根据素材Id去素材库查询素材
        Material material = materialService.get(materialId);
        if (material == null) {
            throw BException.E("素材未找到");
        }
        return getMaterial(material, user);
    }

    /**
     * 根据素材ID获取 素材类
     *
     * @param materialId
     * @return
     */
    public BaseBuilder getMaterial(Material material, WxMpUser user) throws BException {
        if (material == null)
            throw BException.E("素材未找到");

        if ("text".equals(material.getType())) {
            // 处理文本
            return doMessageText(material, user);
        }
        else if ("image".equals(material.getType())) {
            // 处理图片
            return doMessageImage(material, user);
        }
        else if ("news".equals(material.getType())) {
            // 处理图文
            return doMessageNews(material, user);
        }
        else if ("voice".equals(material.getType())) {
            // 处理语音

        }
        else if ("music".equals(material.getType())) {
            // 处理音乐

        }
        else {
            throw BException.E("未找到相关消息类型");
        }

        return null;
    }

    /**
     * 图文信息 封装
     *
     * @param material
     * @return
     */
    public NewsBuilder doMessageNews(Material material, WxMpUser user) throws BException {
        // TODO 明细 WxMpXmlOutNewsMessage.Item
        List<WxMpXmlOutNewsMessage.Item> items = new ArrayList<WxMpXmlOutNewsMessage.Item>();
        // List<PageData> newsArticles =
        // newsService.getArticlesByNewsId(material.getNewsId());
        // if (newsArticles == null) {
        // throw BException.E("未找到相关图文消息");
        // }
        if (StringUtils.isBlank(material.getMediaId())) {
            throw new BException("媒体id为空！");
        }
        WxMpMaterialNews wxMpMaterialNews = weChatUtils.getMaterialInfo(material.getMediaId());
        // newsArticles.forEach((articles) -> {
        // WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
        // item.setTitle(articles.getString("title"));
        // item.setDescription(articles.getString("content"));
        // item.setPicUrl(articles.getString("pictureUrl"));
        // item.setUrl(articles.getString("sourceUrl"));
        // items.add(item);
        // });
        if (wxMpMaterialNews == null) {
            throw new BException("未找到相关图文信息！");
        }
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

    public NewsBuilder doMessageNews(String materialId, WxMpUser user) throws BException {
        Material material = materialService.get(materialId);
        if (material == null)
            throw BException.E("素材未找到");
        return doMessageNews(material, user);
    }

    /**
     * 图片信息封装
     *
     * @param material
     * @return mediaId 媒体ID
     */
    public ImageBuilder doMessageImage(Material material, WxMpUser user) {
        // TODO 关键字段 media_id
        Picture picture = pictureService.findImageByMaterial(material.getId());
        if (null == picture) {
            throw BException.E("根据图片素材id未找到对应的图片 : " + material.getId());
        }
        String media_id = picture.getMediaId();
        return WxMpXmlOutMessage.IMAGE().mediaId(media_id);
    }

    public ImageBuilder doMessageImage(String materialId, WxMpUser user) {
        Material material = materialService.get(materialId);
        if (material == null)
            throw BException.E("素材未找到");
        return doMessageImage(material, user);
    }

    /**
     * 文本信息 封装
     *
     * @param material
     * @return
     */
    public TextBuilder doMessageText(Material material, WxMpUser user) {
        String content = material.getContent();
        // 如果有 占位符 替换掉
        if (content != null && (!content.equals("")) && content.indexOf("nickname") > 0) {
            content = content.replace("nickname", user.getNickname());
        }
        if (content.contains("&lt;br/&gt;")) {
            content = content.replace("&lt;br/&gt;", "\n");
        }if(content.contains("&ldquo;"))
        {
            content =content.replace("&ldquo;", "\"");
        }
        if (content.contains("&rdquo;")) {
            content =content.replace("&rdquo;", "\"");
        }
//        if (content.contains("&lsquo;") || content.contains("&rsquo;")) {
//            content = content.replace("&lsquo;", "\'");
//        }
        //content = content.replace("&lt;br/&gt;", "\n").replace("&ldquo;", "\"").replace("&rdquo;", "\"");
        String result = content;
        // 反转义html   org.apache.commons.lang3.StringEscapeUtils
        String resultStr = StringEscapeUtils.unescapeHtml4(result);
        return WxMpXmlOutMessage.TEXT().content(resultStr);
    }

    public TextBuilder doMessageText(String materialId, WxMpUser user) throws BException {
        Material material = materialService.get(materialId);
        if (material == null) {
            throw BException.E("素材未找到");
        }
        return doMessageText(material, user);
    }

    /**
     * 获取自动回复
     */
    public BaseBuilder getAutoReply(WxMpUser user) {
        // TODO 获取自动回复
        AutoReply autoReply = autoReplyService.getAutoReply();
        if (autoReply != null) {
            return getMaterial(autoReply.getMaterialId(), user);
        }
        return null;
    }

    /**
     * 获取关键字回复
     */
    public BaseBuilder getKeyWordReply(String keyword, WxMpUser user) {
        // 获取与用户输入相匹配的关键字
        KeywordReply keyWord = keywordReplyService.getKeyword(keyword);
        if (null != keyWord) {
            return getMaterial(keyWord.getMaterialId(), user);
        }
        return null;
    }

    /**
     * 获取关注回复
     */
    public BaseBuilder getSubscribeReply(WxMpUser user) throws BException {
        // TODO 获取关注回复
        SubscribeReply subscribe = subscribeReplyService.getSubscribeReply();
        if (null == subscribe) {
            return null;
        }
        else {
            return getMaterial(subscribe.getMaterialId(), user);
        }
    }

    /**
     * 文本回复
     *
     * @param text
     * @return
     */
    public BaseBuilder smartReply(String text) {
        // TODO 关键字回复 优先
        // TODO 自动回复

        return null;
    }

    /**
     * 上传图片
     */
    public void uploadImage() {

    }

    /**
     * 下载图片
     */
    public void downloadImage(String mediaId) {

    }

    /**
     * 下载语音
     */
    public void downloadVoice() {

    }

    /**
     * 下载视频
     */
    public void downloadVideo() {

    }

    // 上传素材 图文

    // 下载素材 图文

}

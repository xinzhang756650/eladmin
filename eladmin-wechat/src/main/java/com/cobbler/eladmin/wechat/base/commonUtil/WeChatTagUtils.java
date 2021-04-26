package com.cobbler.eladmin.wechat.base.commonUtil;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.tag.WxTagListUser;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <pre>
 * @Description: 用户标签管理相关接口
 * @Aouth: cao_wencao
 * @Date: 2019-02-18 15:35
 * </pre>
 */
@Slf4j
@Component
public class WeChatTagUtils {
    @Autowired
    private WxMpService wxMpService;

    /**
     * <pre>
     * 创建标签
     * name: 标签名（30个字符以内）标签名长度不能超过30个字节
     * @auther: cao_wencao
     * @date: 2019/2/18 15:39
     * </pre>
     */
    public WxUserTag tagCreate(String name) throws WxErrorException {
        WxUserTag wxUserTag = wxMpService.getUserTagService().tagCreate(name);
        return wxUserTag;
    }


    /**
     * <pre>
     * 获取公众号已创建的标签
     * @auther: cao_wencao
     * @date: 2019/2/18 15:41
     * </pre>
     */
    public List<WxUserTag> tagGet() throws WxErrorException {
        List<WxUserTag> tagList = wxMpService.getUserTagService().tagGet();
        return tagList;
    }

    /**
     * <pre>
     * 编辑标签
     * tagId： 标签id
     * name： 标签name
     * 标签名长度不能超过30个字节
     * @auther: cao_wencao
     * @date: 2019/2/18 15:42
     * </pre>
     */
    public Boolean tagUpdate(Long tagId, String name) throws WxErrorException {
        Boolean flag = wxMpService.getUserTagService().tagUpdate(tagId, name);
        return flag;
    }

    /**
     * <pre>
     * 删除标签
     * 不能修改0/1/2这三个系统默认保留的标签
     * tagId： 标签id
     * @auther: cao_wencao
     * @date: 2019/2/18 15:45
     * </pre>
     */
    public Boolean tagDelete(Long tagId) throws WxErrorException {
        Boolean flag = wxMpService.getUserTagService().tagDelete(tagId);
        return flag;
    }

    /**
     * <pre>
     * 获取标签下粉丝列表
     * tagId: 标签id
     * nextOpenid: 第一个拉取的OPENID，不填默认从头开始拉取
     * @auther: cao_wencao
     * @date: 2019/2/18 15:46
     * </pre>
     */
    public WxTagListUser tagListUser(Long tagId, String nextOpenid) throws WxErrorException {
        WxTagListUser wxTagListUser = wxMpService.getUserTagService().tagListUser(tagId, nextOpenid);
        return wxTagListUser;
    }

    /**
     * <pre>
     * 批量为用户打标签
     * tagId: 标签id
     * openids: 粉丝列表 每次传入的openid列表个数不能超过50个
     * @auther: cao_wencao
     * @date: 2019/2/18 15:49
     * </pre>
     */
    public boolean batchTagging(Long tagId, String[] openids) throws WxErrorException {
        Boolean flag = wxMpService.getUserTagService().batchTagging(tagId, openids);
        return flag;
    }

    /**
     * <pre>
     * 批量为用户取消标签
     * tagId: 标签id
     * openids: 粉丝列表 每次传入的openid列表个数不能超过50个
     * @auther: cao_wencao
     * @date: 2019/2/18 15:49
     * </pre>
     */
    public boolean batchUntagging(Long tagId, String[] openids) throws WxErrorException {
        Boolean flag = wxMpService.getUserTagService().batchUntagging(tagId, openids);
        return flag;
    }

    /**
     * <pre>
     * 获取用户身上的标签列表
     * openid： 用户openid
     * @auther: cao_wencao
     * @date: 2019/2/18 15:55
     * </pre>
     */
    public List<Long> userTagList(String openid) throws WxErrorException {
        List<Long> userTagList = wxMpService.getUserTagService().userTagList(openid);
        return userTagList;
    }

}

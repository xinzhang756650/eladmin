package com.cobbler.eladmin.wechat.base.commonUtil;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpUserQuery;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <pre>
 * @Description:用户管理相关操作接口
 * @Aouth: cao_wencao
 * @Date: 2019-02-18 16:08
 * </pre>
 */
@Slf4j
@Component
public class WeChatUserUtils{
    @Autowired
    private WxMpService wxMpService;

    /**
     * <pre>
     * 设置用户备注名
     * openid:   用户标识
     * remark:   新的备注名，长度必须小于30字符
     * @auther: cao_wencao
     * @date: 2019/2/18 16:10
     * </pre>
     */
    public void userUpdateRemark(String openid, String remark) throws WxErrorException {
        wxMpService.getUserService().userUpdateRemark(openid, remark);
    }

    /**
     * <pre>
     * 获取用户基本信息（语言为默认的zh_CN 简体）
     * openid:   用户标识
     * @auther: cao_wencao
     * @date: 2019/2/18 16:12
     * </pre>
     */
    public WxMpUser userInfo(String openid) throws WxErrorException {
        WxMpUser wxMpUser = wxMpService.getUserService().userInfo(openid);
        return wxMpUser;
    }

    /**
     * <pre>
     * 获取用户基本信息
     * openid:   用户标识
     * lang:   语言
     * @auther: cao_wencao
     * @date: 2019/2/18 16:20
     * </pre>
     */
    public WxMpUser userInfo(String openid, String lang) throws WxErrorException {
        WxMpUser wxMpUser = wxMpService.getUserService().userInfo(openid, lang);
        return wxMpUser;
    }

    /**
     * <pre>
     * 获取用户基本信息列表
     * openids： 用户openid列表
     * @auther: cao_wencao
     * @date: 2019/2/18 16:22
     * </pre>
     */
    public List<WxMpUser> userInfoList(List<String> openids) throws WxErrorException{
        List<WxMpUser> userInfoList = wxMpService.getUserService().userInfoList(openids);
        return userInfoList;
    }

    /**
     * <pre>
     * 批量获取用户基本信息列表
     * 开发者可通过该接口来批量获取用户基本信息。最多支持一次拉取100条。
     * @auther: cao_wencao
     * @date: 2019/2/18 16:25
     * </pre>
     */
    public List<WxMpUser> userInfoList(WxMpUserQuery userQuery) throws WxErrorException{
        List<WxMpUser> userInfoList = wxMpService.getUserService().userInfoList(userQuery);
        return userInfoList;
    }

    /**
     * <pre>
     * 获取用户列表
     * next_openid：	第一个拉取的OPENID，不填默认从头开始拉取
     * @auther: cao_wencao
     * @date: 2019/2/18 16:28
     * </pre>
     */
    public WxMpUserList userList(String nextOpenid) throws WxErrorException{
        WxMpUserList wxMpUserList = wxMpService.getUserService().userList(nextOpenid);
        return wxMpUserList;
    }
}

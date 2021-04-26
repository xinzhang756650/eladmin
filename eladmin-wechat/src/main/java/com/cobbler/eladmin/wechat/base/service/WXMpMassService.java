package com.cobbler.eladmin.wechat.base.service;

import com.thinkgem.jeesite.common.result.JsonResult;
import com.thinkgem.jeesite.modules.app.entity.wx.MassMessage;
import com.thinkgem.jeesite.modules.app.service.wx.MassMessageService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpMassOpenIdsMessage;
import me.chanjar.weixin.mp.bean.result.WxMpMassSendResult;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * <pre>
 * 定时群发类
 * @author cao_wencao
 * @date 2018年9月11日 上午11:22:07
 * </pre>
 */
@Service
@Slf4j
public class WXMpMassService {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private MassMessageService massMessageService;

    /**
     * @throws Exception
     *  按openId列表群发消息.
     * @param 参数
     * @return WxMpMassSendResult 返回类型
     * @throws
     */
    @PostMapping("/massOpenIdsMessageSend")
    public Object massOpenIdsMessageSend(String msgtype, String content, String mediaId){
        if (StringUtils.isBlank(msgtype)) {
            return new JsonResult(null, "群发消息msgtype不能为空!",false);
        }
        if (StringUtils.isBlank(content) && WxConsts.MassMsgType.TEXT.equals(msgtype)) {
            return new JsonResult(null, "群发消息content不能为空!",false); // 一般针对文本
        }
        if (StringUtils.isBlank(mediaId) && !WxConsts.MassMsgType.TEXT.equals(msgtype)) {
            return new JsonResult(null, "群发消息mediaId不能为空!",false);
        }
        // 获取群发历史记录
        List<MassMessage> getMonthMassCount = massMessageService.getMonthMassCount();
        if (getMonthMassCount.size() >= 4) {
            return new JsonResult(null, "本月可群发消息数量已达上线!", false);
        }               
        WxMpMassOpenIdsMessage massMessage = new WxMpMassOpenIdsMessage();     
        // 群发结果
        WxMpMassSendResult openIdMassResult;
        try {
         // 获取用户列表 关注者列表由一串OpenID组成，第一个拉取的OPENID，不填默认从头开始拉取      
            WxMpUserList  userList = this.wxMpService.getUserService().userList("");
            if (null == userList) {
                return new JsonResult(null, "获取微信关注者列表为空!");
            }
            massMessage.setMsgType(msgtype);
            if (msgtype.equals(WxConsts.MassMsgType.MPNEWS)) {
                massMessage.setSendIgnoreReprint(true);
            }
            massMessage.setToUsers(userList.getOpenids());
            // 图过是图文、图片，则content是media_id，如果是文本，则content是文本内容
            massMessage.setContent(content);
            massMessage.setMediaId(mediaId);
            openIdMassResult = this.wxMpService.getMassMessageService().massOpenIdsMessageSend(massMessage);
            // 群发结果
            if (null == openIdMassResult || StringUtils.isEmpty(openIdMassResult.getMsgId())) {
                log.debug("【定时群发执行失败】 ： {} ", openIdMassResult);
                return new JsonResult(null, "群发失败", false);
            }      
        }catch (WxErrorException e1) {
            log.error("【调用微信群发接口失败】 : {}", e1.getMessage());
            return new JsonResult(null, e1.getMessage(), false);
        }catch (Exception e) {
            log.error("【调用微信群发接口失败】 : {}", e.getMessage());
            return new JsonResult(null, e.getMessage(), false);
        }
        log.info("【定时群发执行成功】 ： {} ", openIdMassResult);
        return new JsonResult(openIdMassResult, "群发成功！", true);
    }
    
}

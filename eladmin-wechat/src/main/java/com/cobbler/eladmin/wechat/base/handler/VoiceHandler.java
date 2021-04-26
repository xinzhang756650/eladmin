package com.cobbler.eladmin.wechat.base.handler;

import com.cobbler.eladmin.wechat.base.service.WXMaterialService;
import com.cobbler.eladmin.wechat.base.service.WeixinService;
import com.cobbler.eladmin.wechat.base.service.WxReplyService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.builder.outxml.BaseBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <pre>
 * @ClassName: VoiceHandler
 * @Description: 语音消息处理器
 * @author cao_wencao
 * @date 2018年9月8日 下午6:44:36
 * </pre>
 */
@Component
public class VoiceHandler extends AbstractHandler{
    @Autowired
    private WXMaterialService wXMaterialService;
    @Autowired
    private WxReplyService wxReplyService;

    @SuppressWarnings("rawtypes")
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        WeixinService weixinService = (WeixinService) wxMpService;
        // msgType 消息类型
        String msgType = wxMessage.getMsgType();
        // content 消息内容
        String content = wxMessage.getContent();

        // 获取微信用户基本信息
        WxMpUser userWxInfo = weixinService.getUserService().userInfo(wxMessage.getFromUser(), null);

        if (!msgType.equals(WxConsts.XmlMsgType.EVENT)) {
            // TODO 可以选择将消息保存到本地
        }
        // 当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
        if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服") && weixinService.hasKefuOnline()) {
            return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE().fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).build();
        }
        // 如果没有匹配到关键字，则回复自动回复
        BaseBuilder autoReplyBuilder = wXMaterialService.getAutoReply(userWxInfo);
        if (null == autoReplyBuilder) {
           // 如果自动回复为空， 那么回复"success",避免微信三次重试失败提示"该公众号故障........"
           return wxReplyService.replyNvl(wxMessage);
         }
       return wxReplyService.replyOut(autoReplyBuilder, wxMessage);
    }
}
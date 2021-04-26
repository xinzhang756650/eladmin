package com.cobbler.eladmin.wechat.base.handler;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 
 * @Description: 点击菜单跳转链接事件
 * @author cao_wencao
 * @date 2018年7月17日
 */
@Component
public class NullHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        // 事件类型，VIEW
        String eventType = wxMessage.getEvent();
        if (WxConsts.EventType.VIEW.equals(eventType)) {

        }
        return null;
    }

}

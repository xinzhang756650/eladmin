package com.cobbler.eladmin.wechat.base.handler;

import com.cobbler.eladmin.wechat.base.service.WXSubscribeService;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 取消关注处理
 * 
 * @ClassName: UnsubscribeHandler
 * @author cao_wencao
 * @date 2018年6月27日 下午3:13:38
 */
@Component
public class UnsubscribeHandler extends AbstractHandler {

    @Autowired
    private WXSubscribeService wxSubscribeService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        String openId = wxMessage.getFromUser();
        this.logger.info("取消关注用户 OPENID: " + openId);
        // 更新本地数据库为取消关注状态
        try {
            // 取消关注
            wxSubscribeService.unSubscribe(openId);
        }
        catch (Exception e) {
            this.logger.debug(e.getMessage());
        }
        return null;
    }

}

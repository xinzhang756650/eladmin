package com.cobbler.eladmin.wechat.base.handler;

import com.cobbler.eladmin.wechat.base.service.WXLogService;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 日志处理器Handler
 * 
 * @ClassName: LogHandler
 * @author cao_wencao
 * @date 2018年6月27日 下午3:16:46
 */
@Component
public class LogHandler extends AbstractHandler {
    @Autowired
    private WXLogService wXLogService;
    private static final ObjectMapper JSON = new ObjectMapper();
    static {
        JSON.setSerializationInclusion(Include.NON_NULL);
        JSON.configure(SerializationFeature.INDENT_OUTPUT, Boolean.TRUE);
    }

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        try {
            this.logger.info("\n接收到请求消息，内容：{}", JSON.writeValueAsString(wxMessage));
            // 日志入口时，保存微信发过来的XML（给微信后台）
            wXLogService.doSaveMsgLogIn(wxMessage);
            //this.logger.info("\n保存微信输入日志WxMpXmlMessage信息：\n{}", wxMessage);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

}

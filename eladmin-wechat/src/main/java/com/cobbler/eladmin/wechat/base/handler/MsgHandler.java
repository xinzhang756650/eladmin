package com.cobbler.eladmin.wechat.base.handler;

import com.cobbler.eladmin.wechat.base.builder.TextBuilder;
import com.cobbler.eladmin.wechat.base.commonUtil.JsonUtils;
import com.cobbler.eladmin.wechat.base.service.WeixinService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 默认消息处理
 * 
 * @ClassName: MsgHandler
 * @author cao_wencao
 * @date 2018年7月4日 下午3:02:21
 */
@Component
public class MsgHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {

        WeixinService weixinService = (WeixinService) wxMpService;
        // msgType 消息类型
        String msgType = wxMessage.getMsgType();
        if (!msgType.equals(WxConsts.XmlMsgType.EVENT)) {
            // TODO 可以选择将消息保存到本地
        }

        // 当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
        if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服") && weixinService.hasKefuOnline()) {
            return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE().fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).build();
        }

        // TODO 组装回复消息
//        String respContent = "您好，我是绿小宝！您输入的信息无法查询。请输入以下问题相应的序号进行查看。" + "\n" + "[1]什么是“绿色账户”？" + "\n" + "[2]如何办理“绿色账户卡”？" + "\n" + "[3]绿色账户持卡用户如何获取积分？" + "\n" + "[4]如何查询自己有多少分？" + "\n" + "[5]如何兑换礼品？" + "\n" + "[6]在线兑换多久送货？" + "\n" + "[7]如何联系我们？" + "\n" + "[8]周周秀" + "\n"
//                + "[9]绿色账户官方网站" + "\n" + "[10]我想和你跳" + "\n" + "[11]投票" + "\n" + "[12]洗车券";
        String respContent = "收到信息内容：" + JsonUtils.toJson(wxMessage);

        return new TextBuilder().build(respContent, wxMessage, weixinService);

    }

}

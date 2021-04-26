//package com.cobbler.eladmin.wechat.base.handler;
//
//import com.cobbler.eladmin.wechat.base.service.WXMenuService;
//import com.cobbler.eladmin.wechat.base.service.WxCustomMenuService;
//import com.cobbler.eladmin.wechat.base.service.WxReplyService;
//import me.chanjar.weixin.common.api.WxConsts;
//import me.chanjar.weixin.common.session.WxSessionManager;
//import me.chanjar.weixin.mp.api.WxMpService;
//import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
//import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
//import me.chanjar.weixin.mp.builder.outxml.BaseBuilder;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
///**
// * 点击自定义菜单时，处理CLICK事件
// *
// * @ClassName: MenuHandler
// * @author cao_wencao
// * @date 2018年7月4日 下午3:43:40
// */
//@Component
//public class MenuHandler extends AbstractHandler {
//    @Autowired
//    private WxCustomMenuService wxCustomMenuService;
//    @Autowired
//    private WXMenuService wXMenuService;
//    @Autowired
//    private WxReplyService wxReplyService;
//
//    @Override
//    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService weixinService, WxSessionManager sessionManager) {
//        String msg = String.format("type:%s, event:%s, key:%s", wxMessage.getMsgType(), wxMessage.getEvent(), wxMessage.getEventKey());
//        if (WxConsts.MenuButtonType.VIEW.equals(wxMessage.getEvent())) {
//            return null;
//        }
//        String respContent = "您好，我是绿小宝！您输入的信息无法查询。请输入以下问题相应的序号进行查看。" + "\n" + "[1]什么是“绿色账户”？" + "\n" + "[2]如何办理“绿色账户卡”？" + "\n" + "[3]绿色账户持卡用户如何获取积分？" + "\n" + "[4]如何查询自己有多少分？" + "\n" + "[5]如何兑换礼品？" + "\n" + "[6]在线兑换多久送货？" + "\n" + "[7]如何联系我们？" + "\n" + "[8]周周秀" + "\n"
//                + "[9]绿色账户官方网站" + "\n" + "[10]我想和你跳" + "\n" + "[11]投票" + "\n" + "[12]洗车券";
//
//        //
//
//        String eventKey = wxMessage.getEventKey();
//        // 获取eventKey对应的菜单数据
//        WxCustomMenu menu = wxCustomMenuService.getMenuByKey(eventKey);
//        // eventKey = keyValue = 素材ID，通过eventKey取得素材
//        @SuppressWarnings("rawtypes")
//        BaseBuilder builder = wXMenuService.doClick(wxMessage, eventKey);
//        if (StringUtils.equals(menu.getKeyValue(), eventKey)) {
//            if (null == builder) {
//                // 如果没有构建builder，走空回复
//                return wxReplyService.replyNvl(wxMessage);
//            }
//            return wxReplyService.replyOut(builder, wxMessage);
//        }
//
//        return WxMpXmlOutMessage.TEXT()
//                            .content(respContent)
//                            .fromUser(wxMessage.getToUser())
//                            .toUser(wxMessage.getFromUser())
//                            .build();
//    }
//
//}

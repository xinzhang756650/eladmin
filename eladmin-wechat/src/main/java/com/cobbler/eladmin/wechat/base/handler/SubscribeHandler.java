//package com.cobbler.eladmin.wechat.base.handler;
//
//import com.cobbler.eladmin.wechat.base.commonUtil.WxConstants;
//import com.cobbler.eladmin.wechat.base.service.*;
//import me.chanjar.weixin.common.error.WxErrorException;
//import me.chanjar.weixin.common.session.WxSessionManager;
//import me.chanjar.weixin.mp.api.WxMpService;
//import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
//import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
//import me.chanjar.weixin.mp.bean.result.WxMpUser;
//import me.chanjar.weixin.mp.builder.outxml.BaseBuilder;
//import me.zhengjie.exception.BException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.text.ParseException;
//import java.util.Map;
//
///**
// * 用户关注处理
// *
// * @ClassName: SubscribeHandler
// * @author cao_wencao
// * @date 2018年6月13日 上午10:09:43
// */
//@Component
//public class SubscribeHandler extends AbstractHandler {
//
//    @Autowired
//    private WXSubscribeService wxSubscribeService;
//    @Autowired
//    private WxReplyService wxReplyService;
//    @Autowired
//    private WXMaterialService wXMaterialService;
//    @Autowired
//    private SceneQrcodeService sceneQrcodeService;
//    @Autowired
//    private SceneFlowService sceneFlowService;
//
//    // 新用户关注处理
//    @Override
//    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
//        this.logger.info("新关注用户 OPENID: " + wxMessage.getFromUser());
//
//        WeixinService weixinService = (WeixinService) wxMpService;
//        // 获取微信用户基本信息
//        WxMpUser userWxInfo = weixinService.getUserService().userInfo(wxMessage.getFromUser(), null);
//        // 针对扫描带参数二维码关注场景
//        this.handleSpecial(wxMessage,userWxInfo);
//        // 保存 用户信息
//        try {
//            wxSubscribeService.subscribe(userWxInfo);
//        }
//        catch (BException e) {
//            logger.error("保存粉丝信息失败", e.getMessage());
//            e.printStackTrace();
//        }
//        catch (ParseException e) {
//            logger.error("保存粉丝信息失败", e.getMessage());
//            e.printStackTrace();
//        }
//
//        // 回复用户
//        @SuppressWarnings("rawtypes")
//        BaseBuilder builder = wXMaterialService.getSubscribeReply(userWxInfo);
//        if (builder == null) {
//            // 如果 没有关注回复 回复""
//            return wxReplyService.replyNvl(wxMessage);
//        }
//        else {
//            // 有固定的关注回复， 回复什么
//            return wxReplyService.replyOut(builder, wxMessage);
//        }
//
//    }
//
//    /**
//     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
//     */
//    protected WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage,WxMpUser userWxInfo) throws WxErrorException {
//        String eventKey = wxMessage.getEventKey();
//        //如果为true，则表明是扫描带参数二维码关注进来的
//        if (null != eventKey && eventKey.contains(WxConstants.QRSCENE)){
//            //获取sceneId
//            String sceneId = eventKey.substring(eventKey.lastIndexOf("_")+1);
//            SceneQrcode sceneQrcode = sceneQrcodeService.findQrcodeBySceneId(sceneId);
//            if (null != sceneQrcode){
//                //每个唯一渠道商二维码扫码次数统计
//                sceneQrcodeService.saveSceneQrcode(sceneQrcode);
//                //扫码关注场景统计
//                sceneFlowService.saveSceneFlow(sceneQrcode,userWxInfo);
//            }
//        }
//        return null;
//    }
//
//}

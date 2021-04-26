package com.cobbler.eladmin.wechat.base.service;

import com.cobbler.eladmin.wechat.base.handler.*;
import com.cobbler.eladmin.wechat.config.WxMpConfig;
import me.chanjar.weixin.common.api.WxConsts.EventType;
import me.chanjar.weixin.common.api.WxConsts.MenuButtonType;
import me.chanjar.weixin.common.api.WxConsts.XmlMsgType;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.kefu.result.WxMpKfOnlineList;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.constant.WxMpEventConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


/**
 * 
* @ClassName: WeixinService
* @author CaoWenCao
* @date 2018年6月26日 下午3:35:43
 */
@Service
public class WeixinService extends WxMpServiceImpl {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected LogHandler logHandler;

    @Autowired
    protected NullHandler nullHandler;

    @Autowired
    protected KfSessionHandler kfSessionHandler;

    @Autowired
    protected StoreCheckNotifyHandler storeCheckNotifyHandler;

    @Autowired
    private WxMpConfig wxConfig;

    @Autowired
    private LocationHandler locationHandler;

    @Autowired
    private MenuHandler menuHandler;

    @Autowired
    private MsgHandler msgHandler;

    @Autowired
    private UnsubscribeHandler unsubscribeHandler;

    @Autowired
    private SubscribeHandler subscribeHandler;
    
    @Autowired
    private TextHandler textHandler;
    
    @Autowired
    private ImageHandler imageHandler;
    
    @Autowired
    private VoiceHandler voiceHandler;
    
    @Autowired
    private VideoHandler videoHandler;

    private WxMpMessageRouter router;

    /*
     * 初始化 微信相关配置
     */
    @PostConstruct
    public void init() {
        final WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(this.wxConfig.getAppid());// 设置微信公众号的appid
        config.setSecret(this.wxConfig.getAppsecret());// 设置微信公众号的app corpSecret
        config.setToken(this.wxConfig.getToken());// 设置微信公众号的token
        config.setAesKey(this.wxConfig.getAesKey());// 设置消息加解密密钥
        super.setWxMpConfigStorage(config);
        this.refreshRouter();
    }

    private void refreshRouter() {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(this);

        // 记录所有事件的日志
        newRouter.rule().handler(this.logHandler).next();

        // 接收客服会话管理事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(WxMpEventConstants.CustomerService.KF_CREATE_SESSION).handler(this.kfSessionHandler).end();
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(WxMpEventConstants.CustomerService.KF_CLOSE_SESSION).handler(this.kfSessionHandler).end();
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(WxMpEventConstants.CustomerService.KF_SWITCH_SESSION).handler(this.kfSessionHandler).end();

        // 门店审核事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(WxMpEventConstants.POI_CHECK_NOTIFY).handler(this.storeCheckNotifyHandler).end();

        // 点击菜单拉取消息事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(MenuButtonType.CLICK).handler(this.getMenuHandler()).end();

        // 点击菜单连接事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(MenuButtonType.VIEW).handler(this.nullHandler).end();

        // 关注事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(EventType.SUBSCRIBE).handler(this.getSubscribeHandler()).end();

        // 取消关注事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(EventType.UNSUBSCRIBE).handler(this.getUnsubscribeHandler()).end();

        // 上报地理位置事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(EventType.LOCATION).handler(this.getLocationHandler()).end();

        // 接收地理位置消息
        newRouter.rule().async(false).msgType(XmlMsgType.LOCATION).handler(this.getLocationHandler()).end();

        // 扫码事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(EventType.SCAN).handler(this.getScanHandler()).end();

        // 处理文本信息事件
        newRouter.rule().async(false).msgType(XmlMsgType.TEXT).handler(this.getTextHandler()).end();
        
        // 处理图片信息事件
        newRouter.rule().async(false).msgType(XmlMsgType.IMAGE).handler(this.getImageHandler()).end();
       
        // 处理语音信息事件
        newRouter.rule().async(false).msgType(XmlMsgType.VOICE).handler(this.getVoiceHandler()).end();
        
        // 处理视频信息事件
        newRouter.rule().async(false).msgType(XmlMsgType.VIDEO).handler(this.getVideoHandler()).end();


        
        // 默认消息处理器
        newRouter.rule().async(false).handler(this.getMsgHandler()).end();

        this.router = newRouter;
    }

    public WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return this.router.route(message);
        }
        catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        return null;
    }

    public boolean hasKefuOnline() {
        try {
            WxMpKfOnlineList kfOnlineList = this.getKefuService().kfOnlineList();
            return kfOnlineList != null && kfOnlineList.getKfOnlineList().size() > 0;
        }
        catch (Exception e) {
            this.logger.error("获取客服在线状态异常: " + e.getMessage(), e);
        }

        return false;
    }

    protected MenuHandler getMenuHandler() {
        return this.menuHandler;
    }

    protected SubscribeHandler getSubscribeHandler() {
        return this.subscribeHandler;
    }

    protected UnsubscribeHandler getUnsubscribeHandler() {
        return this.unsubscribeHandler;
    }

    protected AbstractHandler getLocationHandler() {
        return this.locationHandler;
    }

    protected MsgHandler getMsgHandler() {
        return this.msgHandler;
    }

    protected AbstractHandler getScanHandler() {
        return null;
    }

    public TextHandler getTextHandler() {
        return textHandler;
    }

    public ImageHandler getImageHandler() {
        return imageHandler;
    }

    public VoiceHandler getVoiceHandler() {
        return voiceHandler;
    }

    public VideoHandler getVideoHandler() {
        return videoHandler;
    }


}

package com.cobbler.eladmin.wechat.base.service;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.builder.outxml.BaseBuilder;
import me.chanjar.weixin.mp.builder.outxml.ImageBuilder;
import me.chanjar.weixin.mp.builder.outxml.NewsBuilder;
import me.chanjar.weixin.mp.builder.outxml.TextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 回复服务
 * 
 * @author weilecai
 *
 */
@Service
public class WxReplyService {

    @Autowired
    com.thinkgem.jeesite.modules.wechat.service.WXMaterialService wXMaterialService;

    /**
     * 回复消息OUT
     * 
     * @param bulider
     * @param wxMessage
     * @return
     */
    public WxMpXmlOutMessage replyOut(BaseBuilder bulider, WxMpXmlMessage wxMessage) {
        bulider.fromUser(wxMessage.getToUser());
        bulider.toUser(wxMessage.getFromUser());
        if (bulider instanceof ImageBuilder) {
            return ((ImageBuilder) bulider).build();
        }
        else if (bulider instanceof TextBuilder) {
            return ((TextBuilder) bulider).build();
        }
        else if (bulider instanceof NewsBuilder) {
            return ((NewsBuilder) bulider).build();
        }
        else {
            // 未知消息体
            return null;
        }
    }

    /**
     * 答复成功 无任何消息回复
     * 
     * @param materialId
     * @return
     */
    public WxMpXmlOutMessage replyNvl(WxMpXmlMessage wxMessage) {
        //String content = "您的消息,我已经收到!我们会及时回复给您!";
        //return replyOut(WxMpXmlOutMessage.TEXT().content(content), wxMessage);
        return null;
    }

    /**
     * 不回复
     * 
     * @param materialId
     * @return
     */
    public WxMpXmlOutMessage replyNull(WxMpXmlMessage wxMessage) {
        return null;
    }

    /**
     * 根据素材ID回复
     * 
     * @param materialId
     * @return
     */
    public WxMpXmlOutMessage reply(String materialId, WxMpXmlMessage wxMessage) {
        return null;
    }

    /**
     * 回复文本
     * 
     * @param content
     * @return
     */
    public WxMpXmlOutMessage replyText(String content, WxMpXmlMessage wxMessage) {
        return replyOut(WxMpXmlOutMessage.TEXT().content(content), wxMessage);
    }

}

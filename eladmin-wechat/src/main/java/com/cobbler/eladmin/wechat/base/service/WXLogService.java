package com.cobbler.eladmin.wechat.base.service;

import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.common.utils.BException;
import com.thinkgem.jeesite.common.utils.EmojiConverterUtils;
import com.thinkgem.jeesite.common.utils.EmojiUtils;
import com.thinkgem.jeesite.modules.app.entity.log.MessageLog;
import com.thinkgem.jeesite.modules.app.entity.log.ReceiveMessage;
import com.thinkgem.jeesite.modules.app.entity.log.ReplyMessage;
import com.thinkgem.jeesite.modules.app.entity.wx.KeywordReply;
import com.thinkgem.jeesite.modules.app.service.log.MessageLogService;
import com.thinkgem.jeesite.modules.app.service.log.ReceiveMessageService;
import com.thinkgem.jeesite.modules.app.service.log.ReplyMessageService;
import com.thinkgem.jeesite.modules.app.service.wx.KeywordReplyService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 微信 日志
 * 
 * @author weilecai
 *
 */
@Service
public class WXLogService {
    @Autowired
    private MessageLogService messageLogService;
    @Autowired
    private ReceiveMessageService receiveMessageService;
    @Autowired
    private ReplyMessageService replyMessageService;
    @Autowired
    private KeywordReplyService keywordReplyService;

    /**
     * 保存 微信消息in 日志(XML)
     */
    @Transactional(readOnly = false)
    public void doSaveMsgLogIn(WxMpXmlMessage wxMessage) throws BException {
        // 保存微信消息日志，XML
        MessageLog messageLog = new MessageLog();
        messageLog.setFromUser(wxMessage.getFromUser());
        messageLog.setToUser(wxMessage.getToUser());
        messageLog.setOccurDate(new Date());
        // 日志状态 0:正常 1:异常
        messageLog.setStatus(DictUtils.getDictValue("正常", "message_log_status", "1"));
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(wxMessage);
        messageLog.setContent(EmojiUtils.emojiChange(jsonObject.toJSONString()));
        // 日志状态 0:输出 1:输入
        messageLog.setMessageWay(DictUtils.getDictValue("输入", "message_way", "1"));
        messageLogService.save(messageLog);
    }

    /**
     * 保存 微信消息out 日志(XML)
     */
    @Transactional(readOnly = false)
    public void doSaveMsgLogOut(WxMpXmlOutMessage wxMessage) throws BException {
        // 保存微信消息日志，XML
        MessageLog messageLog = new MessageLog();
        messageLog.setFromUser(wxMessage.getToUserName());
        messageLog.setToUser(wxMessage.getFromUserName());
        messageLog.setOccurDate(new Date());
        // 日志状态 0:正常 1:异常
        messageLog.setStatus(DictUtils.getDictValue("正常", "message_log_status", "1"));
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(wxMessage);
        messageLog.setContent(jsonObject.toJSONString());
        // 日志状态 0:输出 1:输入
        messageLog.setMessageWay(DictUtils.getDictValue("输出", "message_way", "0"));
        messageLogService.save(messageLog);
    }

    /**
     * 保存 微信接收 信息(后台接受微信发过来的)
     */
    @Transactional(readOnly = false)
    public void doSaveReceiveLog(WxMpXmlMessage wxMessage) throws BException {
          // 文本消息
        if (WxConsts.XmlMsgType.TEXT.equals(wxMessage.getMsgType())) {
            this.insertTextMessage(wxMessage);
        } // 图片消息
        if (WxConsts.XmlMsgType.IMAGE.equals(wxMessage.getMsgType())) {
            this.insertImageMessage(wxMessage);
        } // 语音消息
        if (WxConsts.XmlMsgType.VOICE.equals(wxMessage.getMsgType())) {
            this.insertVoiceMessage(wxMessage);
        } // 视频消息
        if (WxConsts.XmlMsgType.VIDEO.equals(wxMessage.getMsgType())) {
            this.insertVideoMessage(wxMessage);
        }
    }

    /**
     * 保存 微信回复 日志(后台回复出去给微信端的)
     */
    @Transactional(readOnly = false)
    public void doSaveReplyLog(WxMpXmlOutMessage message) throws BException {
        // 保存后台回复出去的消息
        ReplyMessage replyMessage = new ReplyMessage();
        Date date = new Date();
        User user = UserUtils.getUser();
        replyMessage.setToUser(message.getFromUserName());
        replyMessage.setFromUser(message.getToUserName());
        replyMessage.setContent("");
        replyMessage.setMaterialId("");
        replyMessage.setMsgType(message.getMsgType());
        replyMessage.setReplyDate(date);
        replyMessage.setReplyOperator("");
        replyMessage.setCreateBy(user);
        replyMessage.setCreateDate(date);
        replyMessage.setUpdateBy(user);
        replyMessage.setUpdateDate(date);
        replyMessageService.save(replyMessage);
    }

    // 保存图片消息
    private void insertImageMessage(WxMpXmlMessage wxMessage) {
        ReceiveMessage receiveMessage = new ReceiveMessage();
        Date date = new Date();
        receiveMessage.setToUser(wxMessage.getToUser());
        receiveMessage.setFromUser(wxMessage.getFromUser());
        receiveMessage.setMsgId(String.valueOf(wxMessage.getMsgId()));
        receiveMessage.setContent("");
        receiveMessage.setMsgType(wxMessage.getMsgType());
        receiveMessage.setMsgId(String.valueOf(wxMessage.getMsgId()));
        receiveMessage.setPicUrl(wxMessage.getPicUrl());
        receiveMessage.setMediaId(wxMessage.getMediaId());
        receiveMessage.setThumbMediaId("");
        receiveMessage.setIsRead("");
        receiveMessage.setReadDate(date);
        receiveMessage.setReplyDate(date);
        receiveMessage.setReplyStatus(DictUtils.getDictValue("未回复", "replyStatus", "0"));
        receiveMessage.setReplyOperator("1");
        // 是否关键字 (不是关键字)
        receiveMessage.setIsKeyword(DictUtils.getDictValue("否", "isKeyword", "0"));
        // 默认不收藏
        receiveMessage.setIsCollection(DictUtils.getDictValue("否", "isCollection", "0"));
        receiveMessageService.save(receiveMessage);
    }

    // 保存文本消息
    private void insertTextMessage(WxMpXmlMessage wxMessage) {
        String content = wxMessage.getContent();
        String content1 =EmojiConverterUtils.emojiConverterToAlias(wxMessage.getContent()) ;
        String isKeyword = null;
        String replyStatus = null;
        if (null != content) {
            // 调取关键字方法，校验是否是关键字
            if (!StringUtils.isBlank(content)) {
                KeywordReply keywordReply = keywordReplyService.getKeyword(content);
                if (null == keywordReply) {
                    isKeyword = DictUtils.getDictValue("否", "isKeyword", "0");
                    replyStatus = DictUtils.getDictValue("未回复", "replyStatus", "0");
                }
                else {
                    isKeyword = DictUtils.getDictValue("是", "isKeyword", "1");
                    // 关键字默认算作"已回复"
                    replyStatus = DictUtils.getDictValue("已回复", "replyStatus", "1");

                }
            }
            else {
                isKeyword = DictUtils.getDictValue("否", "isKeyword", "0"); // 如果不是关键字，就否
                replyStatus = DictUtils.getDictValue("未回复", "replyStatus", "0");
            }
            // 保存后台接受过来的消息
            // 获取当前登录的用户
            // User user = UserUtils.getUser();
            ReceiveMessage receiveMessage = new ReceiveMessage();
            Date date = new Date();
            receiveMessage.setToUser(wxMessage.getToUser());
            receiveMessage.setFromUser(wxMessage.getFromUser());
            receiveMessage.setMsgId(String.valueOf(wxMessage.getMsgId()));
            receiveMessage.setContent(EmojiConverterUtils.emojiConverterToAlias(wxMessage.getContent()));
            receiveMessage.setMsgType(wxMessage.getMsgType());
            receiveMessage.setPicUrl(wxMessage.getPicUrl());
            receiveMessage.setMediaId(wxMessage.getMediaId());
            receiveMessage.setThumbMediaId(wxMessage.getThumbMediaId());
            receiveMessage.setIsRead("");
            receiveMessage.setReadDate(date);
            receiveMessage.setReplyDate(date);
            receiveMessage.setReplyStatus(replyStatus);
            receiveMessage.setReplyOperator("1");
            // 是否关键字
            receiveMessage.setIsKeyword(isKeyword);
            // 默认不收藏
            receiveMessage.setIsCollection(DictUtils.getDictValue("否", "isCollection", "0"));
            receiveMessageService.save(receiveMessage);
        }
    }
    
    
    // 保存语音消息
    private void insertVoiceMessage(WxMpXmlMessage wxMessage){
        ReceiveMessage receiveMessage = new ReceiveMessage();
        Date date = new Date();
        receiveMessage.setToUser(wxMessage.getToUser());
        receiveMessage.setFromUser(wxMessage.getFromUser());
        receiveMessage.setMsgId(String.valueOf(wxMessage.getMsgId()));
        receiveMessage.setContent("");
        receiveMessage.setMsgType(wxMessage.getMsgType());
        receiveMessage.setMsgId(String.valueOf(wxMessage.getMsgId()));
        receiveMessage.setPicUrl("");
        receiveMessage.setMediaId(wxMessage.getMediaId());
        receiveMessage.setThumbMediaId("");
        receiveMessage.setIsRead("");
        receiveMessage.setReadDate(date);
        receiveMessage.setReplyDate(date);
        receiveMessage.setReplyStatus(DictUtils.getDictValue("未回复", "replyStatus", "0"));
        receiveMessage.setReplyOperator("1");
        // 是否关键字 (不是关键字)
        receiveMessage.setIsKeyword(DictUtils.getDictValue("否", "isKeyword", "0"));
        // 默认不收藏
        receiveMessage.setIsCollection(DictUtils.getDictValue("否", "isCollection", "0"));
        receiveMessageService.save(receiveMessage);
    }
    
    // 保存视频消息
    private void insertVideoMessage(WxMpXmlMessage wxMessage){
        ReceiveMessage receiveMessage = new ReceiveMessage();
        Date date = new Date();
        receiveMessage.setToUser(wxMessage.getToUser());
        receiveMessage.setFromUser(wxMessage.getFromUser());
        receiveMessage.setMsgId(String.valueOf(wxMessage.getMsgId()));
        receiveMessage.setContent("");
        receiveMessage.setMsgType(wxMessage.getMsgType());
        receiveMessage.setMsgId(String.valueOf(wxMessage.getMsgId()));
        receiveMessage.setPicUrl("");
        receiveMessage.setMediaId(wxMessage.getMediaId());
        receiveMessage.setThumbMediaId(wxMessage.getThumbMediaId());
        receiveMessage.setIsRead("");
        receiveMessage.setReadDate(date);
        receiveMessage.setReplyDate(date);
        receiveMessage.setReplyStatus(DictUtils.getDictValue("未回复", "replyStatus", "0"));
        receiveMessage.setReplyOperator("1");
        // 是否关键字 (不是关键字)
        receiveMessage.setIsKeyword(DictUtils.getDictValue("否", "isKeyword", "0"));
        // 默认不收藏
        receiveMessage.setIsCollection(DictUtils.getDictValue("否", "isCollection", "0"));
        receiveMessageService.save(receiveMessage);
    }

}

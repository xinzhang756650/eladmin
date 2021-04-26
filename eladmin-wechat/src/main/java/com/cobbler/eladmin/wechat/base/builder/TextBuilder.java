package com.cobbler.eladmin.wechat.base.builder;

import com.cobbler.eladmin.wechat.base.service.WeixinService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;


/**
 * 文本生成
* @ClassName: TextBuilder
* @author CaoWenCao
* @date 2018年6月13日 上午10:11:41
 */
public class TextBuilder extends AbstractBuilder {

  @Override
  public WxMpXmlOutMessage build(String content, WxMpXmlMessage wxMessage, WeixinService service)   {
    WxMpXmlOutTextMessage m = WxMpXmlOutMessage.TEXT().content(content)
        .fromUser(wxMessage.getToUser())
        .toUser(wxMessage.getFromUser())
        .build();
    return m;
  }

}

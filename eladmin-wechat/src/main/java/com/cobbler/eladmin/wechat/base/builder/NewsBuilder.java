package com.cobbler.eladmin.wechat.base.builder;

import com.cobbler.eladmin.wechat.base.service.WeixinService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutImageMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**  
 * @ClassName: NewsBuilder
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author CaoWenCao
 * @date 2018年6月29日 下午5:07:46 
 */
public class NewsBuilder extends AbstractBuilder{

    @Override
    public WxMpXmlOutMessage build(String content, WxMpXmlMessage wxMessage, WeixinService service) {
        WxMpXmlOutImageMessage m = WxMpXmlOutMessage.IMAGE().mediaId(content)
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .build();

            return m;
    }

}

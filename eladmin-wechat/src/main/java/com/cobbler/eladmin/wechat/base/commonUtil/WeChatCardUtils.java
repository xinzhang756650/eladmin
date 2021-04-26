package com.cobbler.eladmin.wechat.base.commonUtil;

import me.chanjar.weixin.common.bean.WxCardApiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.card.WxMpCardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * @Desc: 卡券相关接口管理
 * @ClassName: WeChatCardUtils
 * @Author: cao_wencao
 * @Date: 2019-05-06 16:10
 * @Version: 1.0
 * </pre>
 */
@Component
public class WeChatCardUtils {
    @Autowired
    private WxMpService wxMpService;

    /**
     * <pre>
     * @desc: 获得卡券api_ticket，不强制刷新卡券api_ticket
     * @auth: cao_wencao
     * @date: 2019/5/6 16:12
     * </pre>
     */
    public String getCardApiTicket() throws WxErrorException {
        String api_ticket = wxMpService.getCardService().getCardApiTicket();
        return api_ticket;
    }

    /**
     * <pre>
     * @desc: 获得卡券api_ticket
     * 获得时会检查卡券apiToken是否过期，如果过期了，那么就刷新一下，否则就什么都不干
     * 详情请见：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html#.E9.99.84.E5.BD.954-.E5.8D.A1.E5.88.B8.E6.89.A9.E5.B1.95.E5.AD.97.E6.AE.B5.E5.8F.8A.E7.AD.BE.E5.90.8D.E7.94.9F.E6.88.90.E7.AE.97.E6.B3.95
     * @auth: cao_wencao
     * @date: 2019/5/6 16:13
     * @param forceRefresh  强制刷新
     * </pre>
     */
    public String getCardApiTicket(boolean forceRefresh) throws WxErrorException {
        String api_ticket = wxMpService.getCardService().getCardApiTicket(forceRefresh);
        return api_ticket;
    }

    /**
     * <pre>
     * @desc: 创建调用卡券api时所需要的签名
     * 详情请见：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html#.E9.99.84.E5.BD
     * .954-.E5.8D.A1.E5.88.B8.E6.89.A9.E5.B1.95.E5.AD.97.E6.AE.B5.E5.8F.8A.E7.AD.BE.E5.90.8D.E7.94
     * .9F.E6.88.90.E7.AE.97.E6.B3.95
     * @auth: cao_wencao
     * @date: 2019/5/6 16:15
     * @param optionalSignParam 参与签名的参数数组。
     *    *                          可以为下列字段：app_id, card_id, card_type, code, openid, location_id
     *    *                          </br>注意：当做wx.chooseCard调用时，必须传入app_id参与签名，否则会造成签名失败导致拉取卡券列表为空
     * </pre>
     */
    public WxCardApiSignature createCardApiSignature(String... optionalSignParam) throws WxErrorException {
        WxCardApiSignature wxCardApiSignature = wxMpService.getCardService().createCardApiSignature(optionalSignParam);
        return wxCardApiSignature;
    }

    /**
     * <pre>
     * @desc: 卡券code解码
     * @auth: cao_wencao
     * @date: 2019/5/6 16:17
     * @param encryptCode  加密Code，通过JSSDK的chooseCard接口获得
     * </pre>
     */
    public String decryptCardCode(String encryptCode) throws WxErrorException {
        // 解码后的code
        String decode = wxMpService.getCardService().decryptCardCode(encryptCode);
        return decode;
    }

    /**
     * <pre>
     * @desc: 卡券Code查询
     * @auth: cao_wencao
     * @date: 2019/5/6 16:20
     * @param cardId       卡券ID代表一类卡券
     * @param code         单张卡券的唯一标准
     * @param checkConsume 是否校验code核销状态，填入true和false时的code异常状态返回数据不同
     * </pre>
     */
    public WxMpCardResult queryCardCode(String cardId, String code, boolean checkConsume) throws WxErrorException {
        WxMpCardResult wxMpCardResult = wxMpService.getCardService().queryCardCode(cardId, code, checkConsume);
        return wxMpCardResult;
    }

    /**
     * <pre>
     * @desc: 卡券Code核销。核销失败会抛出异常
     * @auth: cao_wencao
     * @date: 2019/5/6 16:21
     * @param code 单张卡券的唯一标准
     * @return 调用返回的JSON字符串。
     * <br>可用 com.google.gson.JsonParser#parse 等方法直接取JSON串中的errcode等信息。
     * </pre>
     */
    public String consumeCardCode(String code) throws WxErrorException {
        String jsonStr = wxMpService.getCardService().consumeCardCode(code);
        return jsonStr;
    }


    /**
     * <pre>
     * @desc: 卡券Code核销。核销失败会抛出异常
     * @auth: cao_wencao
     * @date: 2019/5/6 16:23
     * @param code   单张卡券的唯一标准
     * @param cardId 当自定义Code卡券时需要传入card_id
     * @return 调用返回的JSON字符串。
     * <br>可用 com.google.gson.JsonParser#parse 等方法直接取JSON串中的errcode等信息。
     * </pre>
     */
    public String consumeCardCode(String code, String cardId) throws WxErrorException {
        String jsonStr = wxMpService.getCardService().consumeCardCode(code, cardId);
        return jsonStr;
    }

    /**
     * <pre>
     * @desc: 卡券Mark接口。
     * @auth: cao_wencao
     * @date: 2019/5/6 16:24
     * @param code   卡券的code码
     * @param cardId 卡券的ID
     * @param openId 用券用户的openid
     * @param isMark 是否要mark（占用）这个code，填写true或者false，表示占用或解除占用
     * </pre>
     */
    public void markCardCode(String code, String cardId, String openId, boolean isMark) throws WxErrorException {
        wxMpService.getCardService().markCardCode(code, cardId, openId, isMark);
    }

    /**
     * <pre>
     * @desc: 查看卡券详情接口
     * @auth: cao_wencao
     * @date: 2019/5/6 16:26
     * 详见 https://mp.weixin.qq.com/wiki/14/8dd77aeaee85f922db5f8aa6386d385e.html#.E6.9F.A5.E7.9C.8B.E5.8D.A1.E5.88.B8.E8.AF.A6.E6.83.85
     *
     * @param cardId 卡券的ID
     * @return 返回的卡券详情JSON字符串
     * <br> [注] 由于返回的JSON格式过于复杂，难以定义其对应格式的Bean并且难以维护，因此只返回String格式的JSON串。
     * <br> 可由 com.google.gson.JsonParser#parse 等方法直接取JSON串中的某个字段。
     * </pre>
     */
    public String getCardDetail(String cardId) throws WxErrorException {
        String jsonStr = wxMpService.getCardService().getCardDetail(cardId);
        return jsonStr;
    }

}

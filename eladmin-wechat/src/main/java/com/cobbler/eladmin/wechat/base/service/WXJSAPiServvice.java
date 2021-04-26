package com.cobbler.eladmin.wechat.base.service;

import com.google.common.collect.Maps;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <pre>
 * @author cao_wencao
 * @date 2018年8月14日 下午5:26:23
 * </pre>
 */
@Service
public class WXJSAPiServvice {

    @Autowired
    private WxMpService wxMpService;

    public Map<String, String> getJSApiData(String url) throws WxErrorException {
        Map<String, String> paramMap = Maps.newHashMap();
        WxJsapiSignature createJsapiSignature = this.wxMpService.createJsapiSignature(url);
        if (null != createJsapiSignature) {
            paramMap.put("appId", createJsapiSignature.getAppId());
            paramMap.put("nonceStr", createJsapiSignature.getNonceStr());
            paramMap.put("timestamp", String.valueOf(createJsapiSignature.getTimestamp()));
            paramMap.put("signature", createJsapiSignature.getSignature());
        }

        return paramMap;

    }
}

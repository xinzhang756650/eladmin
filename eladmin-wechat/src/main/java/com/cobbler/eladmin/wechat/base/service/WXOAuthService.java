//package com.cobbler.eladmin.wechat.base.service;
//
//import me.chanjar.weixin.common.api.WxConsts;
//import me.chanjar.weixin.common.error.WxErrorException;
//import me.chanjar.weixin.mp.api.WxMpService;
//import me.chanjar.weixin.mp.bean.result.WxMpUser;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * 网页授权 服务
// * @author weilecai
// *
// */
//@Service
//@Transactional(readOnly = true)
//public class WXOAuthService {
//	private static final Logger logger = LoggerFactory.getLogger(WXOAuthService.class);
//	@Autowired
//    private WxMpService wxMpService;
//	/**
//	 * 网页授权
//	 * @param request
//	 * @param response
//	 * @throws IOException
//	 */
//	public void oauth(HttpServletRequest request, HttpServletResponse response) throws Exception{
//		String backUrl = request.getRequestURL().toString();  //取得当前 URL
//        String redirectUri = wxMpService.oauth2buildAuthorizationUrl(backUrl, WxConsts.OAuth2Scope.SNSAPI_USERINFO,"1");
//        logger.info("redirectUri地址={}", redirectUri);// 日志
//        response.sendRedirect(redirectUri);
//	}
//
//	/**
//	 * 获取用户信息
//	 * @param request
//	 * @param response
//	 * @throws IOException
//	 */
//	@Transactional(readOnly = false)
//	public void userInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
//		String code = request.getParameter("code");
//		if(StringUtils.isBlank(code))
//			oauth(request, response);   //继续授权
//        // 通过code换取access token
//        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
//        String openId = null;
//        try {
//            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
//            openId = wxMpOAuth2AccessToken.getOpenId();
//            logger.info("openId={}", openId);
//            String lang = "zh_CN";
//            WxMpUser wxMpUser = this.wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, lang);
//            // TODO 操作保存用户信息到数据库
//            // 保存通过授权拿到的用户信息
//            followService.saveOauthFollowInfo(wxMpUser);
////            Follow follow = followService.findFollowByOpenId(openId);
////            if (null == follow && null != wxMpUser) {
////                // 保存通过授权拿到的用户信息
////                followService.saveOauthFollowInfo(wxMpUser);
////            }
//            logger.info("网页授权获取用户信息 ：" + wxMpUser.toString());
//            FansVo fans = new FansVo();
//            // 获取粉丝账号
//            String account =  memberService.getAccountByOpenid(openId);
//            if (!StringUtils.isBlank(account)) {
//                fans.setAccount(account);
//                fans.setOpenid(openId);
//            }
//            request.getSession().setAttribute("fans", fans); //  session中设置粉丝账号和openid
//
//            request.getSession().setAttribute("openid", openId); //设置 Session Openid
//        }
//        catch (WxErrorException e) {
//            logger.error("【微信网页授权】{}", e.getMessage());
//            oauth(request, response);
//            throw new RuntimeException("网页授权失败！");
//        }
//	}
//
//}

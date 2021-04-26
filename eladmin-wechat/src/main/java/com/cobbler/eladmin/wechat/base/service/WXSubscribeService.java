//package com.cobbler.eladmin.wechat.base.service;
//
//import com.thinkgem.jeesite.common.utils.BException;
//import com.thinkgem.jeesite.common.utils.EmojiUtils;
//import com.thinkgem.jeesite.common.utils.IdGen;
//import com.thinkgem.jeesite.modules.app.dao.user.FollowDao;
//import com.thinkgem.jeesite.modules.app.entity.user.Follow;
//import com.thinkgem.jeesite.modules.app.entity.user.Member;
//import com.thinkgem.jeesite.modules.app.service.account.AccountPointService;
//import com.thinkgem.jeesite.modules.app.service.user.FollowService;
//import com.thinkgem.jeesite.modules.sys.entity.User;
//import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
//import com.thinkgem.jeesite.modules.sys.utils.SysParamUtils;
//import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
//import lombok.extern.slf4j.Slf4j;
//import me.chanjar.weixin.mp.bean.result.WxMpUser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.text.ParseException;
//import java.util.Date;
//
///**
// * 关注 服务
// *
// * @author weilecai
// *
// */
//@Service
//@Slf4j
//public class WXSubscribeService {
//    @Autowired
//    private FollowDao followDao;
//    @Autowired
//    private FollowService followService;
//    @Autowired
//    private AccountPointService accountPointService;
//
//
//    /**
//     * 粉丝关注
//     * @author cao_wencao
//     * @param wxUser
//     * @throws BException
//     * @throws ParseException
//     */
//    @SuppressWarnings("null")
//    @Transactional(readOnly = false)
//    public void subscribe(WxMpUser wxUser) throws BException, ParseException {
//        if (wxUser != null) {
//            String openId = wxUser.getOpenId();
//            Follow follow = followService.findFollowByOpenId(openId);
//            Follow firstFollow = new Follow();
//            Date date = new Date();
//            if (null == follow) {
//                // 新用户关注
//                firstFollow.setOpenId(openId);
//                firstFollow.setNickName(EmojiUtils.emojiChange(wxUser.getNickname()));
//                firstFollow.setSex(wxUser.getSexDesc());
//                firstFollow.setCountry(wxUser.getCountry());
//                firstFollow.setProvince(wxUser.getProvince());
//                firstFollow.setCity(wxUser.getCity());
//                firstFollow.setHeadimgurl(wxUser.getHeadImgUrl());
//                String unionid = IdGen.uuid();
//                firstFollow.setUnionid(unionid);
//                firstFollow.setRealName(EmojiUtils.emojiChange(wxUser.getNickname()));//关注回复取不到真实姓名
//                firstFollow.setIsSubscribe(DictUtils.getDictValue("已关注", "follow_isSubscribe", "1"));
//                firstFollow.setSubscribe(date);
//                // 获取当前登录的用户
//                // User user = UserUtils.getUser();
//                User user = UserUtils.getUser();
//                firstFollow.setCreateDate(date);
//                firstFollow.setCreateBy(user);
//                firstFollow.setUpdateBy(user);
//                firstFollow.setUpdateDate(date);
//                followService.save(firstFollow);
//                Member member = followService.saveForMemberAndAccount(firstFollow);
//                log.info("新用户关注 ： {}" , firstFollow.getOpenId());
//                // 新用户首次关注，赠送绿豆值(积分)
//                int point = Integer.parseInt(SysParamUtils.getSysParamValue("关注获取积分", "100"));
//                //业务类型
//                String transMode = DictUtils.getDictValue("关注","trans_mode","2");
//                //交易渠道
//                String channel=null;
//                //交易状态
//                String transStatus="00";
//                //交易类型
//                String transType=DictUtils.getDictValue("收入", "transType", "0");
//                accountPointService.inCome(point, member.getAccount(), transMode, channel, transStatus, transType);
//
//            }
//            else if (null != follow) {
//                // 旧用户再次关注
//                follow.setSubscribe(new Date());
//                follow.setIsSubscribe(DictUtils.getDictValue("已关注", "follow_isSubscribe", "1"));
//                // 获取当前登录的用户
//                // User user = UserUtils.getUser();
//                //follow.preInsert();
//                User user = UserUtils.getUser();
//                follow.setCreateDate(date);
//                follow.setCreateBy(user);
//                follow.setUpdateBy(user);
//                follow.setUpdateDate(date);
//                followService.save(follow);
//                log.info("旧用户关注 ： {}" , follow.getOpenId());
//            }
//        }
//    }
//
//    /**
//     * 取消关注
//     *
//     * @param openId
//     * @throws BException
//     */
//    @Transactional(readOnly = false)
//    public void unSubscribe(String openId) throws BException {
//        Follow follow = followService.findFollowByOpenId(openId);
//        Date date = new Date();
//        if (null != follow) {
//            follow.setIsSubscribe(DictUtils.getDictValue("未关注", "follow_isSubscribe", "0"));
//            follow.setSubscribe(date);
//            // newfollow.setId(follow.getId());
//            // 获取当前登录的用户
//            User user = UserUtils.getUser();
//            //follow.preInsert();
//            follow.setCreateDate(date);
//            follow.setCreateBy(user);
//            follow.setUpdateBy(user);
//            follow.setUpdateDate(date);
//            followDao.update(follow);
//            log.info("取消关注 ： {}" , follow.getOpenId());
//        }
//        else {
//            throw new BException("该openId : " + openId + "对应的粉丝资料缺失!");
//        }
//    }
//
//    /**
//     * 访问事件 （来源于 授权）
//     *
//     * @param wxUser
//     */
//    public void visit(WxMpUser wxUser) {
//
//    }
//
//}

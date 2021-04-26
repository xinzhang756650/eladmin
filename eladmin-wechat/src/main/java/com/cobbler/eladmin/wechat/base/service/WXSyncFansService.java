package com.cobbler.eladmin.wechat.base.service;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.BException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.EmojiUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.app.dao.user.FollowDao;
import com.thinkgem.jeesite.modules.app.entity.user.Follow;
import com.thinkgem.jeesite.modules.app.service.user.FollowService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.wechat.inter.SyncInterface;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <pre>
 * 同步粉丝实现类
 * @author cao_wencao
 * @date 2018年9月17日 上午11:32:21
 * </pre>
 */
@Service
@Slf4j
public class WXSyncFansService extends CrudService<FollowDao, Follow> implements SyncInterface {
    @Autowired
    private FollowService followService;
    @Autowired
    private FollowDao followDao;

    /**
     * 获取数据库中已存在的openId列表
     * 
     */
    @SuppressWarnings("unused")
    @Override
    public List<String> getExistOpenIds() {
        List<String> existOpenIdsList = Lists.newArrayList();
        return existOpenIdsList = dao.getExistOpenIds();
    }

    /**
     * 需要更新的粉丝(每次最多1w条)
     */
    @Transactional(readOnly = false)
    @Override
    public void oldWxMpUser(List<WxMpUser> wxMpUsers) {
        List<Follow> existFollowLists = dao.getFollowLists();
        for (WxMpUser wxMpUser : wxMpUsers) {
            for (Follow follow : existFollowLists) {
                if (StringUtils.equals(follow.getOpenId(), wxMpUser.getOpenId())) {
                    Follow fans = new Follow();
                    fans = followService.findFollowByOpenId(wxMpUser.getOpenId());
                    if (null == fans) {
                        throw new BException("未找到该openid对应的粉丝信息  : " + wxMpUser.getOpenId());
                    }
                    // fans.setId(follow.getId());
                    fans.setOpenId(wxMpUser.getOpenId());
                    fans.setNickName(EmojiUtils.emojiChange(wxMpUser.getNickname()));
                    fans.setSex(wxMpUser.getSexDesc());
                    fans.setCountry(wxMpUser.getCountry());
                    fans.setProvince(wxMpUser.getProvince());
                    fans.setCity(wxMpUser.getCity());
                    fans.setHeadimgurl(wxMpUser.getHeadImgUrl());
                    String unionid = IdGen.uuid();
                    fans.setUnionid(unionid);
                    fans.setRealName(EmojiUtils.emojiChange(wxMpUser.getNickname()));
                    String isSubscribe = wxMpUser.getSubscribe() == true ? DictUtils.getDictValue("已关注", "follow_isSubscribe", "1") : DictUtils.getDictValue("未关注", "follow_isSubscribe", "0");
                    fans.setIsSubscribe(isSubscribe);
                    fans.setSubscribe(DateUtils.unixTimeToDate(wxMpUser.getSubscribeTime()));

                    User user = UserUtils.getUser();
                    fans.setCreateDate(new Date());
                    fans.setCreateBy(user);
                    fans.setUpdateBy(user);
                    fans.setUpdateDate(new Date());
                    dao.update(fans);
                }
            }
        }
        log.info("有" + wxMpUsers.size() + "个粉丝被更新了");

    }

    /**
     * 需要新增的粉丝(每次最多500条返回，可以修改这个数值)
     */
    @Transactional(readOnly = false)
    @Override
    public void newWxMpUser(List<WxMpUser> newMpUsers) {
        for (WxMpUser wxMpUser : newMpUsers) {
            Follow firstFollow = new Follow();
            // 新用户关注
            firstFollow.setOpenId(wxMpUser.getOpenId());
            firstFollow.setNickName(EmojiUtils.emojiChange(wxMpUser.getNickname()));
            firstFollow.setSex(wxMpUser.getSexDesc());
            firstFollow.setCountry(wxMpUser.getCountry());
            firstFollow.setProvince(wxMpUser.getProvince());
            firstFollow.setCity(wxMpUser.getCity());
            firstFollow.setHeadimgurl(wxMpUser.getHeadImgUrl());
            String unionid = IdGen.uuid();
            firstFollow.setUnionid(unionid);
            firstFollow.setRealName(EmojiUtils.emojiChange(wxMpUser.getNickname()));// 关注回复取不到真实姓名
            firstFollow.setIsSubscribe(DictUtils.getDictValue("已关注", "follow_isSubscribe", "1"));
            firstFollow.setSubscribe(DateUtils.unixTimeToDate(wxMpUser.getSubscribeTime())); // 关注时间

            // 获取当前登录的用户
            // User user = UserUtils.getUser();
            Date date = new Date();
            User user = UserUtils.getUser();
            firstFollow.setCreateDate(date);
            firstFollow.setCreateBy(user);
            firstFollow.setUpdateBy(user);
            firstFollow.setUpdateDate(date);
            followService.save(firstFollow);
            followService.saveForMemberAndAccount(firstFollow);
        }
        log.info("有" + newMpUsers.size() + "个粉丝被新增了");

    }

    /**
     * 取消关注的粉丝
     * 
     * @throws WxErrorException
     */
    @Transactional(readOnly = false)
    @Override
    public void unSubscribeFans(List<String> notSubscribeList) {
        Date date = new Date();
        Follow follow = new Follow();
        for (String openid : notSubscribeList) {
            follow = followService.findFollowByOpenId(openid);
            if (null == follow) {
                throw new BException("未找到粉丝资料");
            }
            follow.setIsSubscribe(DictUtils.getDictValue("未关注", "follow_isSubscribe", "0"));
            follow.setSubscribe(date);
            // newfollow.setId(follow.getId());
            // 获取当前登录的用户
            User user = UserUtils.getUser();
            // follow.preInsert();
            follow.setCreateDate(date);
            follow.setCreateBy(user);
            follow.setUpdateBy(user);
            follow.setUpdateDate(date);
            followDao.update(follow);
        }
    }

//    public static Date unixTimeToDate(Long unixTime) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Long time = unixTime * 1000;
//        String d = format.format(time);
//        Date date = null;
//        try {
//            return format.parse(d);
//        }
//        catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return date;
//    }
//    
//    public static void main(String[] args) {
//        Date date =  unixTimeToDate(1537256822l);
//        System.out.println(date);
//        System.out.println(DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss"));
//    }

}

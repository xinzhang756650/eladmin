package com.cobbler.eladmin.wechat.base.commonUtil;

import com.cobbler.eladmin.wechat.base.inter.SyncInterface;
import com.cobbler.eladmin.wechat.base.service.WXSyncFansService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpUserService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 同步粉丝工具类
 * @author cao_wencao  
 * @date 2018年9月17日 下午11:27:06
 * </pre>
 */
@Slf4j
@Component
public class WeChatSyncFansUtils {
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private WXSyncFansService wXSyncFansService;

    private SyncInterface syncInterface;

    /**
     * 每次同步多少条数据，默认每次500条
     */
    private int syncCount = 500;

    @SuppressWarnings("unused")
    private WeChatSyncFansUtils() {

    }

    /**
     * <pre>
     * 用户自定义每次同步多少条
     * </pre>
     */
    public WeChatSyncFansUtils setSyncCount(int syncCount) {
        this.syncCount = syncCount;
        return this;
    }

    /**
     * <pre>
     * 初始化工具类，需传入 wxMpService 和 同步业务类
     * 同步业务类
     * @param wxMpService
     * @param syncInterface
     * @author cao_wencao
     * </pre>
     */
    public WeChatSyncFansUtils(WxMpService wxMpService, SyncInterface syncInterface) {
        this.wxMpService = wxMpService;
        this.syncInterface = syncInterface;
    }

    /**
     * <pre>
     * 同步入口
     * @author cao_wencao
     * @throws Exception
     */
    public void synchronize() throws WxErrorException {
        WxMpUserService userService = wxMpService.getUserService();
        boolean flag = true;
        // nextOpenid 为可选，第一个拉取的OPENID，null为从头开始拉取
        String nextOpenId = null;
        // 获取当前库中所有的用户openid
        List<String> existOpenIds = wXSyncFansService.getExistOpenIds() == null ? new ArrayList<>() : wXSyncFansService.getExistOpenIds();
        int i = 1;
        // 循环方式
        while (flag) {
            // 从微信拉取微信粉丝
            WxMpUserList wxMpUserList = userService.userList(nextOpenId);
            if (nextOpenId == null) {
                log.debug("开始同步粉丝,需同步粉丝总数:{}", wxMpUserList.getCount());
            }
            // 获取h用户openId列表(每次最大值为10000)
            List<String> openidsList = wxMpUserList.getOpenids();
//            log.debug("openidsList :{}", JSONArray.fromObject(openidsList));
            log.debug("openidsList :{}", JsonUtils.toJson(openidsList));
            // 获取粉丝信息列表
            List<WxMpUser> wxMpUsersInfoList = this.getWxMpUserList(openidsList);
            if (wxMpUserList.getCount() == 0 && !ObjectUtils.isEmpty(nextOpenId)) {
                wxMpUsersInfoList.add(userService.userInfo(nextOpenId));
            }
            log.info("开始第{}次同步，本次需同步:{}条", i, wxMpUsersInfoList.size());
            i++;
            // 同步业务
            this.batchSynchronize(wxMpUsersInfoList, existOpenIds);
            // 获取下次获取开始的openId
            nextOpenId = wxMpUserList.getNextOpenid();
            // 对比nextOpenId，如果相同，就是没有下一个了
            if (ObjectUtils.isEmpty(nextOpenId)) {
                // 没有下一个了
                flag = false;
                log.debug("同步结束");
            }
        }
        // 取消关注的粉丝需要修改状态位
        List<String> notSubscribeList = this.unSubscribeOpenIds();
        if(null != notSubscribeList || !ObjectUtils.isEmpty(notSubscribeList)){
            wXSyncFansService.unSubscribeFans(notSubscribeList);
        }
        log.info("有" + notSubscribeList.size() + "个粉丝被取消关注了");
    }

    /**
     * <pre>
     * 粉丝列表分批同步
     * 微信粉丝数据(每次最高1w人)
     * @author cao_wencao
     * @param wxMpUsers
     * </pre>
     */
    private void batchSynchronize(List<WxMpUser> wxMpUsersInfoList, List<String> existOpenIds) {
        // 获取库中已经存在的粉丝
        List<WxMpUser> oldWxMpUsers = this.getOldWxMpUsers(wxMpUsersInfoList, existOpenIds);
        // 库中已经存在的粉丝，做update操作
        wXSyncFansService.oldWxMpUser(oldWxMpUsers);
        // 分批次同步新粉丝
        this.synchronizeNewWxMpUsersBatches(wxMpUsersInfoList, syncCount);
    }

    /**
     * <pre>
     * 分批次同步所有不存在的粉丝，如：服务停止情况下关注的粉丝，是没有新增入库的
     * @param wxMpUsers 微信粉丝列表             
     * @param maxCount 每批最多人数，默认500            
     * @return
     * @author cao_wencao
     * </pre>
     */
    private void synchronizeNewWxMpUsersBatches(List<WxMpUser> wxMpUsers, int maxCount) {
        List<List<WxMpUser>> newFollowerList = new ArrayList<>();
        // 总数
        int count = wxMpUsers.size();
        int a = count % maxCount > 0 ? count / maxCount + 1 : count / maxCount;
        for (int i = 0; i < a; i++) {
            if (i + 1 < a) {
                List<WxMpUser> users = wxMpUsers.subList(i * maxCount, (i + 1) * maxCount);
                newFollowerList.add(users);
            }
            else {
                List<WxMpUser> users = wxMpUsers.subList(i * maxCount, count);
                newFollowerList.add(users);
            }
        }
        for (List<WxMpUser> mpUsers : newFollowerList) {
            // 库中不存在的粉丝，需要insert 由于量可能太大，每次最多insert 500条数据
            wXSyncFansService.newWxMpUser(mpUsers);
        }
    }

    /**
     * <pre>
     * 获取库中已有的粉丝
     * @param wxMpUsers 全部粉丝列表
     * @param existOpenIds 库中openId列表
     * @return 已有的粉丝列表
     * @author cao_wencao
     * </pre>
     */
    private List<WxMpUser> getOldWxMpUsers(List<WxMpUser> wxMpUsersInfoList, List<String> existOpenIds) {
        List<String> excludeOpenIds = new ArrayList<>();
        List<WxMpUser> oldWxMpUsers = new ArrayList<>();
        for (WxMpUser wxMpUser : wxMpUsersInfoList) {
            if (existOpenIds.contains(wxMpUser.getOpenId())) {
                oldWxMpUsers.add(wxMpUser);
                excludeOpenIds.add(wxMpUser.getOpenId());
            }
        }
        // 排除存在的openId列表
        existOpenIds.removeAll(excludeOpenIds);
        // 排除存在的粉丝
        wxMpUsersInfoList.removeAll(oldWxMpUsers);
        return oldWxMpUsers;
    }

    /**
     * <pre>
     * 分批次获取微信粉丝信息 每批100条
     * @param openids
     * @return
     * @throws WxErrorException
     * @author cao_wencao
     * </pre>
     */
    private List<WxMpUser> getWxMpUserList(List<String> openidsList) throws WxErrorException {
        // 粉丝openid数量
        int count = openidsList.size();
        if (count <= 0) {
            return new ArrayList<>();
        }
        List<WxMpUser> list = Lists.newArrayList();
        List<WxMpUser> followersInfoList = Lists.newArrayList();
        int a = count % 100 > 0 ? count / 100 + 1 : count / 100;
        for (int i = 0; i < a; i++) {
            if (i + 1 < a) {
                log.debug("i:{},from:{},to:{}", i, i * 100, (i + 1) * 100);
                followersInfoList = this.wxMpService.getUserService().userInfoList(openidsList.subList(i * 100, (i + 1) * 100));
                if (null != followersInfoList && !followersInfoList.isEmpty()) {
                    list.addAll(followersInfoList);
                }
            }
            else {
                log.debug("i:{},from:{},to:{}", i, i * 100, count - i * 100);
                followersInfoList = wxMpService.getUserService().userInfoList(openidsList.subList(i * 100, count - 1));
                if (null != followersInfoList && !followersInfoList.isEmpty()) {
                    list.addAll(followersInfoList);
                }
            }
        }
        return list;
    }

    /**
     * 取出取消关注的粉丝openid
     * 
     * @author cao_wencao
     * @param existOpenIds
     * @return
     * @throws WxErrorException
     */
    private List<String> unSubscribeOpenIds() {
        // 微信平台取消关注的openid，需要在数据库更新关注状态
        List<String> notSubscribeList = Lists.newArrayList();
        String openid = null;
        // 获取粉丝信息
        List<WxMpUser> followersInfoList = null;
        try {
            List<String> existOpenIds = wXSyncFansService.getExistOpenIds() == null ? new ArrayList<>() : wXSyncFansService.getExistOpenIds();
            if (null != existOpenIds && !ObjectUtils.isEmpty(existOpenIds)) {
                followersInfoList = wxMpService.getUserService().userInfoList(existOpenIds);
                if (null != followersInfoList && !ObjectUtils.isEmpty(followersInfoList)) {
                    for (WxMpUser wxMpUser : followersInfoList) {
                        // 取消关注的粉丝，更新关注状态
                        if (WxConstants.FansStatus.NOT_SUBSCRIBE.equals(wxMpUser.getSubscribe())) {
                            openid = wxMpUser.getOpenId();
                            notSubscribeList.add(openid);
                        }
                    }
                }
            }
        }
        catch (WxErrorException e) {
            log.error("获取需要修改取消关注状态位的粉丝异常", e.getMessage());
            e.printStackTrace();
        }
        return notSubscribeList;
    }

    // public static void main(String[] args) {
    // String json =
    // "{\"user_info_list\":[{\"username\":\"test\"},{\"pwd\":\"test2\"}]}";
    // JsonObject object = new Gson().fromJson(json, JsonObject.class);//这个解析不出来
    // JsonElement element = new
    // JsonParser().parse(json).getAsJsonObject().getAsJsonArray("user_info_list");
    // System.out.println(element);

    // }
}
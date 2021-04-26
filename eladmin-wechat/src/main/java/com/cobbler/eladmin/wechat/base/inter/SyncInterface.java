package com.cobbler.eladmin.wechat.base.inter;

import me.chanjar.weixin.mp.bean.result.WxMpUser;

import java.util.List;

/**
 * <pre>
 * 同步粉丝接口
 * @author cao_wencao
 * @date 2018年9月17日 上午11:16:02
 * </pre>
 */
public interface SyncInterface {
    /**
     * 获取数据库中已存在的openId列表
     *
     * @return openId列表
     */
    List<String> getExistOpenIds();

    /**
     * 需要更新的粉丝(每次最多1w条)
     *
     * @param wxMpUsers
     * 需更新的粉丝列表
     */
    void oldWxMpUser(List<WxMpUser> wxMpUsers);

    /**
     * 需要新增的粉丝(每次最多500条返回，可以修改这个数值)
     * 如：服务停止情况下关注的粉丝，数据库中是没有新增入库的
     * @param newMpUsers
     * 需新增的粉丝列表 
     */
    void newWxMpUser(List<WxMpUser> newMpUsers);

    /**
     * 取消关注的粉丝
     * 如：服务停止情况下取消关注的粉丝，数据库中是没有修改关注状态的
     * @param existOpenIds
     * 需取消关注的粉丝openId列表
     */
    void unSubscribeFans(List<String> existOpenIds);

}

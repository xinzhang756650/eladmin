package com.cobbler.eladmin.wechat.base.commonUtil;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.kefu.request.WxMpKfAccountRequest;
import me.chanjar.weixin.mp.bean.kefu.result.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

/**
 * <pre>
 * @Desc: 客服接口管理
 * @ClassName: WeChatKefuUtils
 * @Author: cao_wencao
 * @Date: 2019-05-06 15:27
 * @Version: 1.0
 * </pre>
 */
@Component
public class WeChatKefuUtils {
    @Autowired
    private WxMpService wxMpService;

    /**
     * <pre>
     * @desc: 发送客服消息
     * 详情请见: <a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140547&token=&lang=zh_CN">发送客服消息</a>
     * 接口url格式：https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN
     * @auth: cao_wencao
     * @date: 2019/5/6 15:29
     * @param message 客服消息
     * </pre>
     */
    public boolean sendKefuMessage(WxMpKefuMessage message) throws WxErrorException {
        Boolean result = wxMpService.getKefuService().sendKefuMessage(message);
        return result;
    }

    /**
     * <pre>
     * @desc: 获取客服基本信息
     * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044813&token=&lang=zh_CN">客服管理</a>
     * 接口url格式：https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token=ACCESS_TOKEN
     * @auth: cao_wencao
     * @date: 2019/5/6 15:29
     * </pre>
     */
    public WxMpKfList kfList() throws WxErrorException {
        WxMpKfList wxMpKfList = wxMpService.getKefuService().kfList();
        return wxMpKfList;
    }

    /**
     * <pre>
     * @desc: 获取在线客服接待信息
     * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044813&token=&lang=zh_CN">客服管理</a>
     * 接口url格式：https://api.weixin.qq.com/cgi-bin/customservice/getonlinekflist?access_token=ACCESS_TOKEN
     * @auth: cao_wencao
     * @date: 2019/5/6 15:34
     * </pre>
     */
    public WxMpKfOnlineList kfOnlineList() throws WxErrorException {
        WxMpKfOnlineList wxMpKfOnlineList = wxMpService.getKefuService().kfOnlineList();
        return wxMpKfOnlineList;
    }

    /**
     * <pre>
     * @desc: 添加客服账号
     * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044813&token=&lang=zh_CN">客服管理</a>
     * 接口url格式：https://api.weixin.qq.com/customservice/kfaccount/add?access_token=ACCESS_TOKEN
     * @auth: cao_wencao
     * @date: 2019/5/6 15:35
     * @param request
     * </pre>
     */
    public boolean kfAccountAdd(WxMpKfAccountRequest request) throws WxErrorException {
        Boolean result = wxMpService.getKefuService().kfAccountAdd(request);
        return result;
    }

    /**
     * <pre>
     * @desc: 设置客服信息（即更新客服信息）
     * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044813&token=&lang=zh_CN">客服管理</a>
     * 接口url格式：https://api.weixin.qq.com/customservice/kfaccount/update?access_token=ACCESS_TOKEN
     * @auth: cao_wencao
     * @date: 2019/5/6 15:37
     * @param request
     * </pre>
     */
    public boolean kfAccountUpdate(WxMpKfAccountRequest request) throws WxErrorException {
        Boolean result = wxMpService.getKefuService().kfAccountUpdate(request);
        return result;
    }

    /**
     * <pre>
     * @desc: 设置客服信息（即更新客服信息）
     * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044813&token=&lang=zh_CN">客服管理</a>
     * 接口url格式：https://api.weixin.qq.com/customservice/kfaccount/inviteworker?access_token=ACCESS_TOKEN
     * @auth: cao_wencao
     * @date: 2019/5/6 15:40
     * @param request
     * </pre>
     */
    public boolean kfAccountInviteWorker(WxMpKfAccountRequest request) throws WxErrorException {
        Boolean result = wxMpService.getKefuService().kfAccountInviteWorker(request);
        return result;
    }

    /**
     * <pre>
     * @desc: 上传客服头像
     * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044813&token=&lang=zh_CN">客服管理</a>
     * 接口url格式：https://api.weixin.qq.com/customservice/kfaccount/uploadheadimg?access_token=ACCESS_TOKEN&kf_account=KFACCOUNT
     * @auth: cao_wencao
     * @date: 2019/5/6 15:42
     * @param kfAccount 客服账号
     * @param imgFile  客服头像
     * </pre>
     */
    public boolean kfAccountUploadHeadImg(String kfAccount, File imgFile) throws WxErrorException {
        Boolean result = wxMpService.getKefuService().kfAccountUploadHeadImg(kfAccount, imgFile);
        return result;
    }

    /**
     * <pre>
     * @desc: 删除客服账号
     * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044813&token=&lang=zh_CN">客服管理</a>
     * 接口url格式：https://api.weixin.qq.com/customservice/kfaccount/del?access_token=ACCESS_TOKEN&kf_account=KFACCOUNT
     * @auth: cao_wencao
     * @date: 2019/5/6 15:44
     * @param kfAccount 客服账号
     * </pre>
     */
    public boolean kfAccountDel(String kfAccount) throws WxErrorException {
        Boolean result = wxMpService.getKefuService().kfAccountDel(kfAccount);
        return result;
    }

    /**
     * <pre>
     * @desc: 创建会话
     * 此接口在客服和用户之间创建一个会话，如果该客服和用户会话已存在，则直接返回0。指定的客服帐号必须已经绑定微信号且在线。
     * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044820&token=&lang=zh_CN">客服会话控制接口</a>
     * 接口url格式： https://api.weixin.qq.com/customservice/kfsession/create?access_token=ACCESS_TOKEN
     * @auth: cao_wencao
     * @date: 2019/5/6 15:45
     * @param openid  用户openid
     * @param kfAccount  客服账号
     * </pre>
     */
    public boolean kfSessionCreate(String openid, String kfAccount) throws WxErrorException {
        Boolean result = wxMpService.getKefuService().kfSessionCreate(openid, kfAccount);
        return result;
    }

    /**
     * <pre>
     * @desc: 关闭会话
     * 开发者可以使用本接口，关闭一个会话。
     * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044820&token=&lang=zh_CN">客服会话控制接口</a>
     * 接口url格式： https://api.weixin.qq.com/customservice/kfsession/close?access_token=ACCESS_TOKEN
     * @auth: cao_wencao
     * @date: 2019/5/6 15:47
     * @param openid  用户openid
     * @param kfAccount  客服账号
     * </pre>
     */
    public boolean kfSessionClose(String openid, String kfAccount) throws WxErrorException {
        Boolean result = wxMpService.getKefuService().kfSessionClose(openid, kfAccount);
        return result;
    }

    /**
     * <pre>
     * @desc: 获取客户的会话状态
     * 此接口获取一个客户的会话，如果不存在，则kf_account为空。
     * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044820&token=&lang=zh_CN">客服会话控制接口</a>
     * 接口url格式： https://api.weixin.qq.com/customservice/kfsession/getsession?access_token=ACCESS_TOKEN&openid=OPENID
     * @auth: cao_wencao
     * @date: 2019/5/6 15:49
     * @param openid   用户openid
     * </pre>
     */
    public WxMpKfSessionGetResult kfSessionGet(String openid) throws WxErrorException {
        WxMpKfSessionGetResult wxMpKfSessionGetResult = wxMpService.getKefuService().kfSessionGet(openid);
        return wxMpKfSessionGetResult;
    }

    /**
     * <pre>
     * @desc: 获取客服的会话列表
     * 开发者可以通过本接口获取某个客服正在接待的会话列表。
     * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044820&token=&lang=zh_CN">客服会话控制</a>
     * 接口url格式： https://api.weixin.qq.com/customservice/kfsession/getsessionlist?access_token=ACCESS_TOKEN&kf_account=KFACCOUNT
     * @auth: cao_wencao
     * @date: 2019/5/6 15:51
     * @param kfAccount   客服账号
     * </pre>
     */
    public WxMpKfSessionList kfSessionList(String kfAccount) throws WxErrorException {
        WxMpKfSessionList wxMpKfSessionList = wxMpService.getKefuService().kfSessionList(kfAccount);
        return wxMpKfSessionList;
    }

    /**
     * <pre>
     * @desc: 获取未接入会话列表
     * 开发者可以通过本接口获取当前正在等待队列中的会话列表，此接口最多返回最早进入队列的100个未接入会话。
     * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1458044820&token=&lang=zh_CN">客服会话控制</a>
     * 接口url格式： https://api.weixin.qq.com/customservice/kfsession/getwaitcase?access_token=ACCESS_TOKEN
     * @auth: cao_wencao
     * @date: 2019/5/6 15:53
     * @param null
     * </pre>
     */
    public WxMpKfSessionWaitCaseList kfSessionGetWaitCase() throws WxErrorException {
        WxMpKfSessionWaitCaseList wxMpKfSessionWaitCaseList = wxMpService.getKefuService().kfSessionGetWaitCase();
        return wxMpKfSessionWaitCaseList;
    }

    /**
     * <pre>
     * @desc: 获取聊天记录（原始接口）
     * 此接口返回的聊天记录中，对于图片、语音、视频，分别展示成文本格式的[image]、[voice]、[video]
     * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1464937269_mUtmK&token=&lang=zh_CN">获取聊天记录</a>
     * 接口url格式： https://api.weixin.qq.com/customservice/msgrecord/getmsglist?access_token=ACCESS_TOKEN
     * @auth: cao_wencao
     * @date: 2019/5/6 15:54
     * @param startTime   起始时间
     * @param endTime     结束时间
     * @param msgId       消息id顺序从小到大，从1开始
     * @param number      每次获取条数，最多10000条
     * </pre>
     */
    public WxMpKfMsgList kfMsgList(Date startTime, Date endTime, Long msgId, Integer number) throws WxErrorException {
        WxMpKfMsgList wxMpKfMsgList = wxMpService.getKefuService().kfMsgList(startTime, endTime, msgId, number);
        return wxMpKfMsgList;
    }

    /**
     * <pre>
     * @desc: 获取聊天记录（优化接口，返回指定时间段内所有的聊天记录）
     * 此接口返回的聊天记录中，对于图片、语音、视频，分别展示成文本格式的[image]、[voice]、[video]
     * 详情请见：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1464937269_mUtmK&token=&lang=zh_CN">获取聊天记录</a>
     * 接口url格式： https://api.weixin.qq.com/customservice/msgrecord/getmsglist?access_token=ACCESS_TOKEN
     * @auth: cao_wencao
     * @date: 2019/5/6 15:56
     * @param startTime 起始时间
     * @param endTime   结束时间
     * </pre>
     */
    public WxMpKfMsgList kfMsgList(Date startTime, Date endTime) throws WxErrorException {
        WxMpKfMsgList kfMsgList = wxMpService.getKefuService().kfMsgList(startTime, endTime);
        return kfMsgList;
    }
}

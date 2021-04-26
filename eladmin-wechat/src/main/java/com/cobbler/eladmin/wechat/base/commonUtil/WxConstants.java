package com.cobbler.eladmin.wechat.base.commonUtil;

/**
 * @ClassName: Constants
 * @Description: 微信常量
 * @author cao_wencao
 * @date 2018年7月3日 上午11:00:19
 */
public class WxConstants {
    public static final String FILE_JPG = "jpeg";
    public static final String FILE_MP3 = "mp3";
    public static final String FILE_AMR = "amr";
    public static final String FILE_MP4 = "mp4";
    public static final int QRCODE_EXPIRE_TIME = 2592000; //带参二维码过期时间30天
    public static final int QRCODE_EXPIRE_DAY = 30; //带参二维码过期时间30天
    public static final String QRSCENE = "qrscene"; //扫描带参数二维码关注
    
    //授权回调地址    网站根目录URL  mywx.tunnel.qydev.com/greenwx
    public static final String REDIRECT_URI = "http://thinkingcao.natapp1.cc/greenwx/wechat/userInfo";
    
    //粉丝关注状态
    public static class FansStatus{
        public static final String NOT_SUBSCRIBE = "0"; //未关注
        public static final String IS_SUBSCRIBE = "1";  //已关注
    }

    //delFlag=0未删除；delFlag=1已删除
    public static class DelFlagStatus{
        public static final String NOT_DELETE = "0"; //未删除
        public static final String IS_DELETE = "1"; //已删除
    }

    // 下载永久素材常量
    public static class DownLoadMaterial{
        public static final int OFFECT = 0;  // 从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
        public static final int COUNT = 1;   // 返回素材的数量，取值在1到20之间
    }
    
    // 素材类型
    public static class MaterialType{
        public static final String IMAGE = "image";   // 图片   
        public static final String NEWS = "news";     // 图文
        public static final String VIDEO = "video";   // 视频
        public static final String VOICE = "voice";   // 语音
        
    }

}

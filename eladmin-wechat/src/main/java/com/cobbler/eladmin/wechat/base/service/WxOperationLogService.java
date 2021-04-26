/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cobbler.eladmin.wechat.base.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.app.dao.wx.WxOperationLogDao;
import com.thinkgem.jeesite.modules.app.entity.sys.SysParam;
import com.thinkgem.jeesite.modules.app.entity.wx.WxOperationLog;
import com.thinkgem.jeesite.modules.app.service.sys.SysParamService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 微信操作日志Service
 *
 * @author caowencao
 * @version 2019-01-21
 */
@Service
@Transactional(readOnly = true)
public class WxOperationLogService extends CrudService<WxOperationLogDao, WxOperationLog> {

    @Autowired
    SysParamService sysParamService;

    public WxOperationLog get(String id) {
        return super.get(id);
    }

    public List<WxOperationLog> findList(WxOperationLog wxOperationLog) {
        return super.findList(wxOperationLog);
    }

    public Page<WxOperationLog> findPage(Page<WxOperationLog> page, WxOperationLog wxOperationLog) {
        return super.findPage(page, wxOperationLog);
    }

    @Transactional(readOnly = false)
    public void save(WxOperationLog wxOperationLog) {
        super.save(wxOperationLog);
    }

    @Transactional(readOnly = false)
    public void delete(WxOperationLog wxOperationLog) {
        super.delete(wxOperationLog);
    }

    /**
     * <pre>
     * 保存微信操作日志
     * @auther: cao_wencao
     * @date: 2019/1/21 11:03
     * </pre>
     */
    @Transactional(readOnly = false)
    public void saveWxOperationLog(HttpServletRequest request, Object handler) {
        //String menuName = ""; //菜单名
        String openid = (String) request.getSession().getAttribute("openid"); //openid
        String ip = StringUtils.getRemoteAddr(request); //操作者IP地址
//		String requestUrl = request.getServerName()+request.getRequestURI(); //完整请求URL地址
        String requestUrl = request.getRequestURI();
        String requestType = request.getMethod(); //请求类型GET、POST
        String requestMethodName = ""; //请求controller中的方法名
        if (handler instanceof HandlerMethod) {
            HandlerMethod h = (HandlerMethod) handler;
            requestMethodName = h.getMethod().getName();
        }
        WxOperationLog wxOperationLog = new WxOperationLog();
        wxOperationLog.setOpenId(openid);
        wxOperationLog.setIp(ip);
        wxOperationLog.setRequestUri(requestUrl);
        wxOperationLog.setRequestType(requestType);
        wxOperationLog.setRequestTime(new Date());
        wxOperationLog.setRequestMethod(requestMethodName + "()");
        User user = UserUtils.getUser();
        wxOperationLog.setCreateDate(new Date());
        wxOperationLog.setCreateBy(user);
        wxOperationLog.setUpdateBy(user);
        wxOperationLog.setUpdateDate(new Date());
        // 保存日志
        if (StringUtils.isNotBlank(openid)) {
            save(wxOperationLog);
        }
        //将连接和都一样的名称保存系统参数
        saveSysParm(((HandlerMethod) handler).getMethodAnnotation(RequestMapping.class).name(), requestUrl);
    }

    /**
     * @return
     * @description 保存跳转链接的功能名称和地址到系统参数里面
     * @author YiHanYuan
     * @params
     * @date 2019/1/22 9:40
     **/
    public void saveSysParm(String name, String url) {
        SysParam sysParam = sysParamService.getByValue(url);
        if (null == sysParam) {
            sysParam = new SysParam();
            sysParam.setParamKey("method_url");
            sysParam.setParamDesc(name);
            sysParam.setParamValue(url);
            sysParamService.save(sysParam);
        }
    }
}
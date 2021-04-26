///**
// * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
// */
//package com.cobbler.eladmin.wechat.base.service;
//
//import com.thinkgem.jeesite.common.persistence.Page;
//import com.thinkgem.jeesite.common.service.CrudService;
//import com.thinkgem.jeesite.modules.app.dao.wx.BindLogDao;
//import com.thinkgem.jeesite.modules.app.entity.wx.BindLog;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
///**
// * 绑定记录Service
// * @author yhy
// * @version 2018-08-16
// */
//@Service
//@Transactional(readOnly = true)
//public class BindLogService extends CrudService<BindLogDao, BindLog> {
//
//	public BindLog get(String id) {
//		return super.get(id);
//	}
//
//	public List<BindLog> findList(BindLog bindLog) {
//		return super.findList(bindLog);
//	}
//
//	public Page<BindLog> findPage(Page<BindLog> page, BindLog bindLog) {
//		return super.findPage(page, bindLog);
//	}
//
//	@Transactional(readOnly = false)
//	public void save(BindLog bindLog) {
//		super.save(bindLog);
//	}
//
//	@Transactional(readOnly = false)
//	public void delete(BindLog bindLog) {
//		super.delete(bindLog);
//	}
//
//	public Integer countByAccount(String account) {
//		return dao.countByAccount(account);
//	}
//}
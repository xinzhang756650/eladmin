/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cobbler.eladmin.wechat.base.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.app.dao.wx.WxApiDocDao;
import com.thinkgem.jeesite.modules.app.entity.wx.WxApiDoc;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 微信接口清单Service
 * @author wlc
 * @version 2018-06-08
 */
@Service
@Transactional(readOnly = true)
public class WxApiDocService extends CrudService<WxApiDocDao, WxApiDoc> {

	public WxApiDoc get(String id) {
		return super.get(id);
	}
	
	public List<WxApiDoc> findList(WxApiDoc wxApiDoc) {
		return super.findList(wxApiDoc);
	}
	
	public Page<WxApiDoc> findPage(Page<WxApiDoc> page, WxApiDoc wxApiDoc) {
		return super.findPage(page, wxApiDoc);
	}
	
	@Transactional(readOnly = false)
	public void save(WxApiDoc wxApiDoc) {
		super.save(wxApiDoc);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxApiDoc wxApiDoc) {
		super.delete(wxApiDoc);
	}
	
}
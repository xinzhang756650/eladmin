/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cobbler.eladmin.wechat.base.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.app.dao.wx.PublicConfigDao;
import com.thinkgem.jeesite.modules.app.entity.wx.PublicConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 公众号账户信息Service
 * @author lcx
 * @version 2018-06-19
 */
@Service
@Transactional(readOnly = true)
public class PublicConfigService extends CrudService<PublicConfigDao, PublicConfig> {

	public PublicConfig get(String id) {
		return super.get(id);
	}
	
	public List<PublicConfig> findList(PublicConfig publicConfig) {
		return super.findList(publicConfig);
	}
	
	public PublicConfig findFirst() {
		return super.findFirst();
	}
	
	public Page<PublicConfig> findPage(Page<PublicConfig> page, PublicConfig publicConfig) {
		return super.findPage(page, publicConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(PublicConfig publicConfig) {
		super.save(publicConfig);
	}
	
	@Transactional(readOnly = false)
	public void delete(PublicConfig publicConfig) {
		super.delete(publicConfig);
	}
	
}
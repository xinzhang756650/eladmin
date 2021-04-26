/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cobbler.eladmin.wechat.base.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.app.dao.wx.SignUpDao;
import com.thinkgem.jeesite.modules.app.entity.wx.SignUp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 报名Service
 * @author yihanyuan
 * @version 2019-02-12
 */
@Service
@Transactional(readOnly = true)
public class SignUpService extends CrudService<SignUpDao, SignUp> {

	public SignUp get(String id) {
		return super.get(id);
	}
	
	public List<SignUp> findList(SignUp signUp) {
		return super.findList(signUp);
	}
	
	public Page<SignUp> findPage(Page<SignUp> page, SignUp signUp) {
		return super.findPage(page, signUp);
	}
	
	@Transactional(readOnly = false)
	public void save(SignUp signUp) {
		super.save(signUp);
	}
	
	@Transactional(readOnly = false)
	public void delete(SignUp signUp) {
		super.delete(signUp);
	}


	
}
///**
// * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
// */
//package com.cobbler.eladmin.wechat.base.service;
//
//import com.thinkgem.jeesite.common.persistence.Page;
//import com.thinkgem.jeesite.common.service.CrudService;
//import com.thinkgem.jeesite.modules.app.dao.wx.LoveShopDao;
//import com.thinkgem.jeesite.modules.app.entity.wx.LoveShop;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
///**
// * 爱心商城Service
// * @author caowencao
// * @version 2019-07-15
// */
//@Service
//@Transactional(readOnly = true)
//public class LoveShopService extends CrudService<LoveShopDao, LoveShop> {
//
//	public LoveShop get(String id) {
//		return super.get(id);
//	}
//
//	public List<LoveShop> findList(LoveShop loveShop) {
//		return super.findList(loveShop);
//	}
//
//	public Page<LoveShop> findPage(Page<LoveShop> page, LoveShop loveShop) {
//		return super.findPage(page, loveShop);
//	}
//
//	@Transactional(readOnly = false)
//	public void save(LoveShop loveShop) {
//		super.save(loveShop);
//	}
//
//	@Transactional(readOnly = false)
//	public void delete(LoveShop loveShop) {
//		super.delete(loveShop);
//	}
//
//
//	public List<LoveShop> findLoveShopData(){
//        return dao.findLoveShopData();
//	}
//
//}
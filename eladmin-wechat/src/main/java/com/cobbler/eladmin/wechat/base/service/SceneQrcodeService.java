/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cobbler.eladmin.wechat.base.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.app.dao.wx.SceneQrcodeDao;
import com.thinkgem.jeesite.modules.app.entity.wx.SceneQrcode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 带参数二维码生成Service
 * @author caowencao
 * @version 2019-04-08
 */
@Service
@Transactional(readOnly = true)
public class SceneQrcodeService extends CrudService<SceneQrcodeDao, SceneQrcode> {


	public SceneQrcode get(String id) {
		return super.get(id);
	}
	
	public List<SceneQrcode> findList(SceneQrcode sceneQrcode) {
		return super.findList(sceneQrcode);
	}
	
	public Page<SceneQrcode> findPage(Page<SceneQrcode> page, SceneQrcode sceneQrcode) {
		return super.findPage(page, sceneQrcode);
	}
	
	@Transactional(readOnly = false)
	public void save(SceneQrcode sceneQrcode) {
		super.save(sceneQrcode);
	}
	
	@Transactional(readOnly = false)
	public void delete(SceneQrcode sceneQrcode) {
		super.delete(sceneQrcode);
	}

    /**
     * <pre>
     * @desc: 根据场景id获取该条渠道信息
     * @auth: cao_wencao
     * @date: 2019/4/9 14:17
     * </pre>
     */
	public SceneQrcode findQrcodeBySceneId(String sceneId){
		SceneQrcode sceneQrcode = new SceneQrcode();
		sceneQrcode.setSceneId(sceneId);
		SceneQrcode qrcode = dao.findQrcodeBySceneId(sceneId);
		return qrcode;
	}


	/**
	 * <pre>
	 * @desc: 批量导入渠道信息，生成带参数二维码
	 * @auth: cao_wencao
	 * @date: 2019/4/9 13:51
	 * </pre>
	 */
	@Transactional(readOnly = false)
	public void importExcelData(String sceneId,String sceneName,String expireSeconds,String qrcode){
        //保存导入的渠道信息和二维码图片地址
		SceneQrcode sceneQrcode = new SceneQrcode();
		sceneQrcode.setSceneId(sceneId);
		sceneQrcode.setSceneName(sceneName);
		sceneQrcode.setExpireSeconds(expireSeconds);
		sceneQrcode.setScanDate(null);
		sceneQrcode.setQrcodeUrl(qrcode);
		sceneQrcode.setTotalNum(0);
		this.save(sceneQrcode);
	}

    /**
     * <pre>
     * @desc: 根据SceneQrcode查询list集合
     * @auth: cao_wencao
     * @date: 2019/4/11 11:25
     * </pre>
     */
    public List<SceneQrcode> findListBySceneQrcode(SceneQrcode sceneQrcode) {
        return dao.findListBySceneQrcode(sceneQrcode);
    }

    /**
     * <pre>
     * @desc: 每个唯一渠道商二维码扫码次数统计
     * @auth: cao_wencao
     * @date: 2019/4/12 16:22
     * </pre>
     */
	@Transactional(readOnly = false)
	public void saveSceneQrcode(SceneQrcode sceneQrcode) {
		Integer total = sceneQrcode.getTotalNum();
		int totalall = total.intValue()+1;
		sceneQrcode.setTotalNum(totalall);
		sceneQrcode.setScanDate(new Date());
		super.save(sceneQrcode);
	}
}
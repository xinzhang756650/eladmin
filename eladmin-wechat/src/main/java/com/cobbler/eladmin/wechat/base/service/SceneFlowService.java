/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cobbler.eladmin.wechat.base.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.EmojiUtils;
import com.thinkgem.jeesite.modules.app.dao.wx.SceneFlowDao;
import com.thinkgem.jeesite.modules.app.entity.wx.SceneFlow;
import com.thinkgem.jeesite.modules.app.entity.wx.SceneQrcode;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 扫码关注场景统计Service
 * @author caowencao
 * @version 2019-04-12
 */
@Service
@Transactional(readOnly = true)
public class SceneFlowService extends CrudService<SceneFlowDao, SceneFlow> {

	public SceneFlow get(String id) {
		return super.get(id);
	}
	
	public List<SceneFlow> findList(SceneFlow sceneFlow) {
		return super.findList(sceneFlow);
	}
	
	public Page<SceneFlow> findPage(Page<SceneFlow> page, SceneFlow sceneFlow) {
		return super.findPage(page, sceneFlow);
	}
	
	@Transactional(readOnly = false)
	public void save(SceneFlow sceneFlow) {
		super.save(sceneFlow);
	}
	
	@Transactional(readOnly = false)
	public void delete(SceneFlow sceneFlow) {
		super.delete(sceneFlow);
	}


	/**
	 * <pre>
	 * @desc: 扫码关注场景统计
	 * @auth: cao_wencao
	 * @date: 2019/4/12 16:13
	 * </pre>
	 */
	@Transactional(readOnly = false)
	public void saveSceneFlow(SceneQrcode sceneQrcode, WxMpUser userWxInfo) {
		SceneFlow sceneFlow = new SceneFlow();
		sceneFlow.setSceneId(sceneQrcode.getSceneId());
		sceneFlow.setOpenId(userWxInfo.getOpenId());
		sceneFlow.setNickName(EmojiUtils.emojiChange(userWxInfo.getNickname()));
		sceneFlow.setHeadimgurl(userWxInfo.getHeadImgUrl());
		sceneFlow.setSceneName(sceneQrcode.getSceneName());
		sceneFlow.setScanDate(new Date());
		super.save(sceneFlow);
	}

	/**
	 * <pre>
	 * @desc: 查询SceneFlow集合
	 * @auth: cao_wencao
	 * @date: 2019/4/15 10:37
	 * </pre>
	 */
	public List<SceneFlow> findListBySceneFlow(SceneFlow sceneFlow){
      return dao.findListBySceneFlow(sceneFlow);
    }

}
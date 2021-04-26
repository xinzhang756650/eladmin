/**
 * Copyright &copy; 2012-2014 <a
 * href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cobbler.eladmin.wechat.base.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.BException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.modules.app.dao.wx.MassMessageDao;
import com.thinkgem.jeesite.modules.app.entity.sucai.Material;
import com.thinkgem.jeesite.modules.app.entity.wx.MassMessage;
import com.thinkgem.jeesite.modules.app.service.sucai.MaterialService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 群发消息Service
 * 
 * @author yhy
 * @version 2018-06-28
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class MassMessageService extends CrudService<MassMessageDao, MassMessage> {
    @Autowired
    private MaterialService materialService;

    public MassMessage get(String id) {
        return super.get(id);
    }

    public List<MassMessage> findList(MassMessage massMessage) {
        return super.findList(massMessage);
    }

    public Page<MassMessage> findPage(Page<MassMessage> page, MassMessage massMessage) {
        return super.findPage(page, massMessage);
    }

    @Transactional(readOnly = false)
    public void save(MassMessage massMessage) {
        super.save(massMessage);
    }

    @Transactional(readOnly = false)
    public void delete(MassMessage massMessage) {
        super.delete(massMessage);
    }

    @Transactional(readOnly = false)
    public void updataStaus(MassMessage massMessage) {
        dao.updataStaus(massMessage);
    }

    @Transactional(readOnly = false)
    public void saveMassMessageService(MassMessage massMessage) {
        Material material = materialService.get(massMessage.getMaterialId());
        if ("text".equals(massMessage.getType())) {
             // 新增文本，在素材库添加文本信息
            if (material == null) {
                material = new Material();
                material.setContent(massMessage.getContent());
                material.setType("text");
                materialService.save(material);
            }
            else {
            	//如果素材库存在文本素材，则修改
            	if("text".equals(material.getType())){
            		 material.setContent(massMessage.getContent());
                     materialService.save(material);
            	}
              	else{
            		//如果素材库存在不为文本素材,则在素材库新增一条文本素材
            		 material = new Material();
                     material.setContent(massMessage.getContent());
                     material.setType("text");
                     materialService.save(material);
                     massMessage.setMaterialId(material.getId());
            	}
               
            }
        }
        if (massMessage.getIsNewRecord()) {
            massMessage.setMaterialId(material.getId());
        }
        save(massMessage);

        /*
         * if (massMessage.getIsNewRecord()){ massMessage.preInsert(); if
         * ("text".equals(massMessage.getType())) {
         * dao.addTextList(massMessage); } dao.addList(massMessage); }else{
         * massMessage.preUpdate(); if ("text".equals(massMessage.getType())) {
         * dao.updateTextList(massMessage); } dao.updateList(massMessage);
         * 
         * }
         */
    }

    @Transactional(readOnly = false)
    public List<MassMessage> getAllList() {
        return dao.getAllList();
        // TODO Auto-generated method stub
    }

    // 查询所删除的图片ID是否被使用
    public int getImgIsUseCount(String id) {
        return dao.getImgIsUseCount(id);
    }
  //查询所删除的图片ID是否被使用
  		public int getNewsIsUseCount(String id){
  			return dao.getNewsIsUseCount(id);
  	}

    // 获取当月群发记录
    public List<MassMessage> getMonthMassCount() {
        MassMessage massMessage = new MassMessage();
        massMessage.setMonthStartTimeStr(DateUtils.getMonthFristDay()); // 当月开始时间
        massMessage.setMonthEndTimeStr(DateUtils.getMonthLastDay()); // 当月结束时间
        List<MassMessage> getMonthMassCount = dao.getMonthMassCount(massMessage);
        return getMonthMassCount;

    }

    // 获取上一次数据库中群发状态
    public String getMpMassStatus(String material_id) {
        MassMessage massMessage = dao.getMpMassStatus(material_id);
        if (null != massMessage && !StringUtils.isBlank(massMessage.getSendStatus())) {
            return massMessage.getSendStatus();
        }
        else {
            log.debug("素材material_id : ", material_id);
            throw new BException("根据素材id，没有找到对应的群发记录！");
        }
    }

}
///**
// * Copyright &copy; 2012-2014 <a
// * href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
// */
//package com.cobbler.eladmin.wechat.base.service;
//
//import com.thinkgem.jeesite.common.persistence.Page;
//import com.thinkgem.jeesite.common.service.CrudService;
//import com.thinkgem.jeesite.modules.app.dao.wx.KeywordReplyDao;
//import com.thinkgem.jeesite.modules.app.entity.sucai.Material;
//import com.thinkgem.jeesite.modules.app.entity.wx.KeywordReply;
//import com.thinkgem.jeesite.modules.app.service.sucai.MaterialService;
//import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
///**
// * 关键字回复Service
// *
// * @author yhy
// * @version 2018-06-28
// */
//@Service
//@Transactional(readOnly = true)
//public class KeywordReplyService extends CrudService<KeywordReplyDao, KeywordReply> {
//
//    @Autowired
//    private MaterialService materialService;
//
//    public KeywordReply get(String id) {
//        return super.get(id);
//    }
//
//    public List<KeywordReply> findList(KeywordReply keywordReply) {
//        return super.findList(keywordReply);
//    }
//
//    public Page<KeywordReply> findPage(Page<KeywordReply> page, KeywordReply keywordReply) {
//        return super.findPage(page, keywordReply);
//    }
//
//    @Transactional(readOnly = false)
//    public void delete(KeywordReply keywordReply) {
//        super.delete(keywordReply);
//    }
//
//    /**
//     *
//     * 查询匹配匹配到的关键字，支持半匹配和全匹配
//     *
//     * @param: content
//     * @return: KeywordReply
//     */
//    @SuppressWarnings("null")
//    public KeywordReply getKeyword(String word) {
//        KeywordReply reply = new KeywordReply();
//        reply.setWord(word);
//        reply.setStatus("1");
//        // 如果微信传过来的文本不是关键字，那么return null,走下一步自动回复、不影响业务
//        // matchType 匹配方式(1:全匹配 2:半匹配)
//        List<Object> keywordList = dao.queryKeyWordList(reply);
//        if (null != keywordList && keywordList.size() > 0) {
//            KeywordReply result = (KeywordReply) keywordList.get(0);
//            return result;
//        }
//        return null;
//    }
//
//    @Transactional(readOnly = false)
//    public void saveKeywordReply(KeywordReply keywordReply) {
//        Material material = materialService.get(keywordReply.getMaterialId());
//        if ("text".equals(keywordReply.getType())) {
//            // 新增文本，在素材库添加文本信息
//            if (material == null) {
//                material = new Material();
//                material.setContent(keywordReply.getContent());
//                material.setType("text");
//                materialService.save(material);
//            }
//            else {
//                // 如果素材库存在文本素材，则修改
//                if ("text".equals(material.getType())) {
//                    material.setContent(keywordReply.getContent());
//                    materialService.save(material);
//                }
//                else {
//                    // 如果素材库存在不为文本素材,则在素材库新增一条文本素材
//                    material = new Material();
//                    material.setContent(keywordReply.getContent());
//                    material.setType("text");
//                    materialService.save(material);
//                    keywordReply.setMaterialId(material.getId());
//                }
//            }
//        }
//        if (keywordReply.getIsNewRecord()) {
//            if (keywordReply.getMatchType() == null) {
//                keywordReply.setMatchType("2");
//            }
//            keywordReply.setMaterialId(material.getId());
//        }
//        save(keywordReply);
//
//        /*
//         * if (keywordReply.getIsNewRecord()){ keywordReply.preInsert(); if
//         * ("text".equals(keywordReply.getType())) {
//         * dao.addText1List(keywordReply); } dao.addList(keywordReply); }else{
//         * keywordReply.preUpdate(); if ("text".equals(keywordReply.getType()))
//         * { dao.updateTextList(keywordReply); } dao.updateList(keywordReply); }
//         */
//
//    }
//
//    @Transactional(readOnly = false)
//    public void updataStaus(KeywordReply keywordReply) {
//        dao.updataStaus(keywordReply);
//    }
//
//    // 查询所删除的图片ID是否被使用
//    public int getImgIsUseCount(String id) {
//        return dao.getImgIsUseCount(id);
//    }
//
//    // 查询所删除的图片ID是否被使用
//    public int getNewsIsUseCount(String id) {
//        return dao.getNewsIsUseCount(id);
//    }
//
//    public KeywordReply getKeywordReply() {
//        KeywordReply keywordReply = new KeywordReply();
//        keywordReply.setStatus(DictUtils.getDictValue("可用", "material_status", "1"));
//        KeywordReply keyReply = dao.getKeywordReplyByMaterialId(keywordReply);
//        if (null == keyReply) {
//            return null;
//        }
//        else {
//            return keyReply;
//        }
//    }
//
//    // 校验关键字是否存在 1表示为true,0表示为false (该方法暂且废弃掉)
//    public Boolean checkWordExist(String word) {
//        Boolean result = false;
//        int count = dao.checkWordExist(word);
//        if (count >= 1) {
//            result = true;
//            return result;
//        }
//        return result;
//
//    }
//
//}
/**
 * Copyright &copy; 2012-2014 <a
 * href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cobbler.eladmin.wechat.base.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.BException;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.app.dao.wx.WxCustomMenuDao;
import com.thinkgem.jeesite.modules.app.entity.sucai.Material;
import com.thinkgem.jeesite.modules.app.entity.sucai.NewsArticleOut;
import com.thinkgem.jeesite.modules.app.entity.sucai.Picture;
import com.thinkgem.jeesite.modules.app.entity.wx.WxCustomMenu;
import com.thinkgem.jeesite.modules.app.entity.wx.WxCustomMenu.Menu;
import com.thinkgem.jeesite.modules.app.entity.wx.WxCustomMenu.SubMenu;
import com.thinkgem.jeesite.modules.app.service.sucai.MaterialService;
import com.thinkgem.jeesite.modules.app.service.sucai.NewsService;
import com.thinkgem.jeesite.modules.app.service.sucai.PictureService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 微信菜单名称Service
 *
 * @author yhy
 * @version 2018-07-06
 */
@Service
@Transactional(readOnly = true)
public class WxCustomMenuService extends CrudService<WxCustomMenuDao, WxCustomMenu> {

    @Autowired
    MaterialService materialService;
    @Autowired
    PictureService pictureService;
    @Autowired
    NewsService newsService;

    public WxCustomMenu get(String id) {
        return super.get(id);
    }

    public List<WxCustomMenu> findList(WxCustomMenu wxCustomMenu) {
        return super.findList(wxCustomMenu);
    }

    public Page<WxCustomMenu> findPage(Page<WxCustomMenu> page, WxCustomMenu wxCustomMenu) {
        return super.findPage(page, wxCustomMenu);
    }

    @Transactional(readOnly = false)
    public void save(WxCustomMenu wxCustomMenu) {
        super.save(wxCustomMenu);
    }

    @Transactional(readOnly = false)
    public void delete(WxCustomMenu wxCustomMenu) {
        super.delete(wxCustomMenu);
    }

    /**
     * 保存微信菜单
     *
     * @param list
     * @throws Exception
     * @author YiHanYuan
     * @date 2018年6月22日
     */
    @Transactional(readOnly = false)
    public void saveMenu(List<Menu> list) throws Exception {
        // 添加之前先删除原有的数据
        dao.findList(new WxCustomMenu()).forEach((menu) -> {
            dao.delete(menu);
        });
        // 对主菜单排序
        int j = 1;
        Date date = new Date();
        // 获取当前登录的用户
        User user = UserUtils.getUser();
        for (Menu menu : list) {
            WxCustomMenu wxMenu = new WxCustomMenu();
            wxMenu.setMenuName(menu.getName());
            wxMenu.setSortIndex(j++);
            // 保存主菜单，得到父菜单id
            wxMenu.setCreateDate(date);
            wxMenu.setCreateBy(user);
            wxMenu.setUpdateBy(user);
            wxMenu.setUpdateDate(date);
            // 循环赋值子菜单
            int i = 1;
            if (menu.getSub_button().size() > 5) {
                throw new Exception("最多只能有五个子菜单");
            }
            // 当没有子菜单，主菜单就是一个事件的情况,直接保存完就继续下一次循环
            else if (menu.getSub_button().size() == 0) {
                // 为了方便,可直接把主菜单当子菜单插入
                Material material = new Material();
                // if (StringUtils.isBlank(menu.getMsgId())) {
                // // 不存在id，说明是url或者文本,文本需要新增素材
                if ("text".equals(menu.getMsgType())) {
//					if (StringUtils.isNotBlank(menu.getMsgId())) {
//						material = assertMaterialExist(menu.getMsgId());
//					}
                    material.setContent(menu.getContent());
                    material.setType("text");
                    materialService.save(material);
                    wxMenu.setMaterialId(material.getId());
                } else if ("url".equals(menu.getMsgType())) {
                    wxMenu.setViewUrl(menu.getUrl().trim());
                }
                // 只有是url或者text是存在msgID不存在的情况
                else if ("news".equals(menu.getMsgType())) {
                    // 为图文的时候
                    if (StringUtils.isBlank(menu.getMsgId())) {
                        throw new BException("主菜单消息id为空");
                    }
                    material = assertMaterialExist(menu.getMsgId());
                } else if ("image".equals(menu.getMsgType())) {
                    // 为图文的时候
                    if (StringUtils.isBlank(menu.getMsgId())) {
                        throw new BException("主菜单消息id为空");
                    }
                    material = assertMaterialExist(menu.getMsgId());
                } else if ("miniprogram".equals(menu.getMsgType())) {
                    // 为小程序的时候
                    if (StringUtils.isAnyBlank(menu.getAppid(), menu.getPagepath(), menu.getUrl())) {
                        throw new BException("小程序必填信息不能为空");
                    }
                    wxMenu.setPagepath(menu.getPagepath().trim());
                    wxMenu.setAppid(menu.getAppid().trim());
                    wxMenu.setViewUrl(menu.getUrl().trim());
                } else {
                    // 如果都不是当前类型，则报错。不让保存
                    throw new BException("菜单类型不存在！");
                }
                // }
                // 对子菜单赋值
                wxMenu.setMaterialType(material.getType());
                wxMenu.setMaterialId(material.getId());
                wxMenu.setKeyValue(material.getId());
                wxMenu.setMenuName(menu.getName());
                wxMenu.setParent(wxMenu);
                // 目前在后台做判断，仅限于当前只有click和view的情况
                if ("url".equals(menu.getMsgType())) {
                    wxMenu.setType("view");
                } else if ("miniprogram".equals(menu.getMsgType())) {
                    wxMenu.setType("miniprogram");
                } else {
                    wxMenu.setType("click");
                }
            }
            dao.insert(wxMenu);
            // 对子菜单做新增操作
            for (WxCustomMenu.SubMenu menu2 : menu.getSub_button()) {
                // 类型(text:文本，image:图片, voice:语音,video:视频，music:音乐,news:图文)
                Material material = new Material();
                WxCustomMenu sub = new WxCustomMenu();
                if ("text".equals(menu2.getMsgType())) {
//					if (StringUtils.isBlank(menu2.getMsgId())) {
                    //只要是文本素材，就新增，不对id作为判断条件
                    material.setContent(menu2.getContent());
                    // 素材类型
                    material.setType("text");
                    // 保存媒体表
                    materialService.save(material);
                    // 对素材id赋值
                    menu2.setMsgId(material.getId());
//					} else {
//						// 当只是修改内容时，则值修改素材表的内容，不对其进行其他操作
//						material = assertMaterialExist(menu2.getMsgId());
//						material.setContent(menu2.getContent());
//						// 更新媒体表
//						materialService.save(material);
//					}
                } else if ("image".equals(menu2.getMsgType()) && StringUtils.isNoneBlank(menu2.getMsgId())) {
                    // 当为图片的时候,判断素材是否存在
                    material = assertMaterialExist(menu2.getMsgId());
                } else if ("news".equals(menu2.getMsgType()) && StringUtils.isNoneBlank(menu2.getMsgId())) {
                    // 为图文的时候
                    material = assertMaterialExist(menu2.getMsgId());
                } else if ("url".equals(menu2.getMsgType())) {
                    sub.setViewUrl(menu2.getUrl().trim());
                } else if ("miniprogram".equals(menu2.getMsgType())) {
                    //为小程序类型的时候
                    if (StringUtils.isAnyBlank(menu2.getAppid(), menu2.getPagepath(), menu2.getUrl())) {
                        throw new BException("小程序必填信息不能为空");
                    }
                    sub.setAppid(menu2.getAppid().trim());
                    sub.setPagepath(menu2.getPagepath().trim());
                    sub.setViewUrl(menu2.getUrl().trim());
                } else {
                    // 如果都不是当前类型，则报错。不让保存
                    throw new BException("菜单类型不存在！");
                }
                // 对子菜单赋值
                sub.setMaterialType(material.getType());
                sub.setMaterialId(material.getId());
                sub.setKeyValue(material.getId());
                sub.setMenuName(menu2.getName());
                sub.setParent(wxMenu);
                sub.setSortIndex(i++);
                // 目前在后台做判断，仅限于当前只有click和view的情况
                if ("url".equals(menu2.getMsgType())) {
                    sub.setType("view");
                } else if ("miniprogram".equals(menu2.getMsgType())) {
                    sub.setType("miniprogram");
                } else {
                    sub.setType("click");
                }
                // sub.setViewUrl(viewUrl);
                sub.setCreateBy(user);
                sub.setUpdateBy(user);
                sub.setCreateDate(date);
                sub.setUpdateDate(date);
                dao.insert(sub);
                // WxCustomMenu sub = new WxCustomMenu();
                // // 当素材不是文本的时候获取素材类型
                // Material material = new Material();
                // if (StringUtils.isNotBlank(menu2.getMsgId())) {
                // material = materialService.get(menu2.getId());
                // if (material == null) {
                // throw new Exception("素材不存在");
                // }
                // } else {
                // // 当素材是文本的时候，先新增文本素材，
                // material.setContent(menu2.getContent());
                // // TODO 赋值文本内容
                // material.setType("text");
                // // DictUtils.getDictValue(label, type, defaultLabel)换成从数字字典中取
                // material.setStatus("1");
                // materialService.save(material);
                // }
                // // 对子菜单赋值
                // sub.setMaterialType(material.getType());
                // sub.setMaterialId(material.getId());
                // sub.setMenuName(menu2.getName());
                // sub.setParent(wxMenu);
                // sub.setSortIndex(i++);
                // sub.setViewUrl(menu2.getUrl());
                // sub.setType(menu2.getType());
                // // sub.setViewUrl(viewUrl);
                // sub.setCreateBy(user);
                // sub.setUpdateBy(user);
                // sub.setCreateDate(date);
                // sub.setUpdateDate(date);
                // dao.insert(sub);
            }
        }
    }

    /**
     * 获取菜单列表
     *
     * @return
     * @throws Exception
     * @author YiHanYuan
     * @date 2018年7月9日
     */
    public JSONObject getAllMenu() throws Exception {
        List<Menu> menus = dao.getAllMenu();
        for (Menu menu : menus) {
            // 当没有子菜单的时候，把主菜单当作子菜单去查找出来，然后赋值给主菜单
            if (menu.getSub_button().size() == 0) {
                // 该方法只适用于传子菜单，需要将主菜单的数据转换到子菜单对象
                SubMenu subMenu = new SubMenu();
                // 赋值给子菜单
                BeanUtils.copyProperties(subMenu, menu);
                getSubMenuDetail(subMenu);
                // 查出数据后赋值给主菜单
                BeanUtils.copyProperties(menu, subMenu);
                continue;
            }
            // 循环对子菜单赋值
            for (SubMenu sub : menu.getSub_button()) {
                getSubMenuDetail(sub);
            }
        }

        String json = new JSONObject().toJSONString(menus);
        JSONObject button = new JSONObject();
        button.put("button", JSONArray.parseArray(json));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", button);
        jsonObject.put("success", "true");
        jsonObject.put("msg", "信息操作成功!");
        jsonObject.put("page", null);
        return jsonObject;
    }

    /****
     * 对子菜单赋值，为了重复利用，因为存在有子菜单和吴子菜单的情况，当子菜单存在时，无需返回数据，直接赋值，当无子菜单时，以内对象不一致，需要返回数据然后在做调整
     *
     * @author YiHanYuan
     * @date 2018年8月21日
     * @param sub
     * @return
     * @throws Exception
     */
    public SubMenu getSubMenuDetail(SubMenu sub) throws Exception {
        // 类型(text:文本，image:图片, voice:语音,video:视频，music:音乐,news:图文,miniprogram ,小程序)
        // 当不为素材是url时
        if (StringUtils.isBlank(sub.getMsgType())) {
            if ("miniprogram".equals(sub.getType())) {
                sub.setMsgType("miniprogram");
            } else {
                sub.setMsgType("url");
            }
            return sub;
        } else if ("image".equals(sub.getMsgType())) {
            // 当素材类型为图片的时候，去素材库取相应的素材
            Material material = materialService.get(sub.getMsgId());
            if (material == null) {
                throw new BException("图片素材未找到");
            }
            Picture picture = pictureService.get(material.getPictureId());
            if (picture == null) {
                throw new BException("图片未找到");
            }
            sub.setPictureUrl(picture.getPictureUrl());
        } else if ("news".equals(sub.getMsgType())) {
            // 当素材为图文的时候
            List<NewsArticleOut> datas = newsService.getNewsByMaterialIdForWeb(sub.getMsgId());
            if (datas == null) {
                throw new BException("图文未找到");
            }
            sub.setArticles(datas);
        } else if ("text".equals(sub.getMsgType())) {
            // 当素材为文本的时候
            Material material = materialService.get(sub.getMsgId());
            if (material == null) {
                throw new BException("素材未找到");
            }
            sub.setContent(material.getContent());
        }
        return sub;
    }

    /**
     * 获取菜单列表---提供数据给微信
     *
     * @return
     * @author YiHanYuan
     * @date 2018年7月9日
     */
    public JSONObject getAllMenuForWX() {
        List<Menu> menus = dao.getAllMenu();
        String json = JSON.toJSONString(menus);
        JSONObject button = new JSONObject();
        button.put("button", JSONArray.parseArray(json));
        // JSONObject jsonObject = new JSONObject();
        // jsonObject.put("data", button);
        return button;
    }

    /**
     * 根据id判断素材是否存在,并返回对象
     *
     * @param id
     * @author YiHanYuan
     * @date 2018年8月21日
     */
    public Material assertMaterialExist(String id) {
        Material material = materialService.get(id);
        if (material == null) {
            throw new BException("素材不存在！");
        }
        return material;
    }

    /**
     * 通过eventKey获取对应菜单数据
     *
     * @param: eventKey
     * @return: WxCustomMenu
     */
    public WxCustomMenu getMenuByKey(String eventKey) {
        List<WxCustomMenu> customMenuList = dao.getMenuByKey(eventKey);
        if (null == customMenuList || customMenuList.size() < 1) {
            throw BException.E("未找到eventKey对应的菜单数据");
        }
        return customMenuList.get(0);
    }

}
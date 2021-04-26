/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.modules.test.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.test.domain.TestUser;
import me.zhengjie.modules.test.repository.TestUserRepository;
import me.zhengjie.modules.test.service.TestUserService;
import me.zhengjie.modules.test.service.dto.TestUserDto;
import me.zhengjie.modules.test.service.dto.TestUserQueryCriteria;
import me.zhengjie.modules.test.service.mapstruct.TestUserMapper;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author songxinzhang
* @date 2021-04-21
**/
@Service
@RequiredArgsConstructor
public class TestUserServiceImpl implements TestUserService {

    private final TestUserRepository testUserRepository;
    private final TestUserMapper testUserMapper;

    @Override
    public Map<String,Object> queryAll(TestUserQueryCriteria criteria, Pageable pageable){
        Page<TestUser> page = testUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(testUserMapper::toDto));
    }

    @Override
    public List<TestUserDto> queryAll(TestUserQueryCriteria criteria){
        return testUserMapper.toDto(testUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TestUserDto findById(Long userId) {
        TestUser testUser = testUserRepository.findById(userId).orElseGet(TestUser::new);
        ValidationUtil.isNull(testUser.getUserId(),"TestUser","userId",userId);
        return testUserMapper.toDto(testUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TestUserDto create(TestUser resources) {
        return testUserMapper.toDto(testUserRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TestUser resources) {
        TestUser testUser = testUserRepository.findById(resources.getUserId()).orElseGet(TestUser::new);
        ValidationUtil.isNull( testUser.getUserId(),"TestUser","id",resources.getUserId());
        testUser.copy(resources);
        testUserRepository.save(testUser);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long userId : ids) {
            testUserRepository.deleteById(userId);
        }
    }

    @Override
    public void download(List<TestUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TestUserDto testUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户名", testUser.getUsername());
            map.put("性别", testUser.getGender());
            map.put("手机号码", testUser.getPhone());
            map.put("邮箱", testUser.getEmail());
            map.put("创建者", testUser.getCreateBy());
            map.put("更新者", testUser.getUpdateBy());
            map.put("修改密码的时间", testUser.getPwdResetTime());
            map.put("创建日期", testUser.getCreateTime());
            map.put("更新时间", testUser.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
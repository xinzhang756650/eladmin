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
package test.rest;

import me.zhengjie.annotation.Log;
import test.domain.TestUser;
import test.service.TestUserService;
import test.service.dto.TestUserQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author songxinzhang
* @date 2021-04-21
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "test管理")
@RequestMapping("/api/testUser")
public class TestUserController {

    private final TestUserService testUserService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('testUser:list')")
    public void download(HttpServletResponse response, TestUserQueryCriteria criteria) throws IOException {
        testUserService.download(testUserService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询test")
    @ApiOperation("查询test")
    @PreAuthorize("@el.check('testUser:list')")
    public ResponseEntity<Object> query(TestUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(testUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增test")
    @ApiOperation("新增test")
    @PreAuthorize("@el.check('testUser:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody TestUser resources){
        return new ResponseEntity<>(testUserService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改test")
    @ApiOperation("修改test")
    @PreAuthorize("@el.check('testUser:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody TestUser resources){
        testUserService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除test")
    @ApiOperation("删除test")
    @PreAuthorize("@el.check('testUser:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        testUserService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
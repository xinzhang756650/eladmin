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
package me.zhengjie.modules.test.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @website https://el-admin.vip
* @author songxinzhang
* @date 2021-04-21
**/
@Data
public class TestUserQueryCriteria{

    /** 精确 */
    @Query
    private Long userId;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String username;

    /** 精确 */
    @Query
    private String gender;

    /** 小于等于 */
    @Query(type = Query.Type.LESS_THAN)
    private String phone;

    /** 大于等于 */
    @Query(type = Query.Type.GREATER_THAN)
    private String email;
}
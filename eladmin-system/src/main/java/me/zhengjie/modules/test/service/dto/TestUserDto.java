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
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author songxinzhang
* @date 2021-04-21
**/
@Data
public class TestUserDto implements Serializable {

    /** ID */
    private Long userId;

    /** 用户名 */
    private String username;

    /** 性别 */
    private String gender;

    /** 手机号码 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 创建者 */
    private String createBy;

    /** 更新者 */
    private String updateBy;

    /** 修改密码的时间 */
    private Timestamp pwdResetTime;

    /** 创建日期 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;
}
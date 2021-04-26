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
package me.zhengjie.modules.test.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;

/**
* @website https://el-admin.vip
* @description /
* @author songxinzhang
* @date 2021-04-21
**/
@Entity
@Data
@Table(name="test_user")
public class TestUser extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @ApiModelProperty(value = "ID")
    private Long userId;

    @Column(name = "username",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "用户名")
    private String username;

    @Column(name = "gender")
    @ApiModelProperty(value = "性别")
    private String gender;

    @Column(name = "phone",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "手机号码")
    private String phone;

    @Column(name = "email")
    @ApiModelProperty(value = "邮箱")
    private String email;

    @Column(name = "pwd_reset_time")
    @ApiModelProperty(value = "修改密码的时间")
    private Timestamp pwdResetTime;

    public void copy(TestUser source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
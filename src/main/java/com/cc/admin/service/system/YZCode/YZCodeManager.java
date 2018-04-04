package com.cc.admin.service.system.YZCode;

import org.apache.ibatis.annotations.Select;

/**
 * 教务系统验证码管理
 */
public interface YZCodeManager {

    @Select("SELECT code FROM ")
    public String getNameByCode(String code);
}

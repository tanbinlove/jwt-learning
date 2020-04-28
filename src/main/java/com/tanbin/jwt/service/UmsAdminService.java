package com.tanbin.jwt.service;

import com.tanbin.jwt.model.UmsAdmin;
import com.tanbin.jwt.model.UmsPermission;

import java.util.List;

/**
 * 后台管理员service
 */
public interface UmsAdminService {
    /**
     * 根据用户名获取后台管理员
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * 注册功能
     */
    UmsAdmin register(UmsAdmin umsAdminParam);

    /**
     * 登陆功能,返回token
     */
    String login(String username, String password);

    /**
     * 获取用户所有权限
     */
    List<UmsPermission> getPermissionList(Long adminId);
}

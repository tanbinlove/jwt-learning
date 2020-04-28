package com.tanbin.jwt.service.impl;

import com.tanbin.jwt.model.UmsAdmin;
import com.tanbin.jwt.model.UmsPermission;
import com.tanbin.jwt.service.UmsAdminService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UmsAdminServiceImpl implements UmsAdminService {
    @Override
    public UmsAdmin getAdminByUsername(String username) {
        return null;
    }

    @Override
    public UmsAdmin register(UmsAdmin umsAdminParam) {
        return null;
    }

    @Override
    public String login(String username, String password) {
        return null;
    }

    @Override
    public List<UmsPermission> getPermissionList(Long adminId) {
        return null;
    }
}

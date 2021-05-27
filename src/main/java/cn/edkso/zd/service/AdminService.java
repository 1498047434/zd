package cn.edkso.zd.service;

import cn.edkso.zd.entry.Admin;
import cn.edkso.zd.entry.User;

public interface AdminService {

    public Admin login(String username, String password);

    Admin update(Admin admin);

    Admin add(Admin admin);
}

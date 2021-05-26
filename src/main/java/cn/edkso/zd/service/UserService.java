package cn.edkso.zd.service;

import cn.edkso.zd.entry.User;

public interface UserService {

    public User login(String username, String password);

    User register(String username, String password, String name);

    User update(User oldUser);

}

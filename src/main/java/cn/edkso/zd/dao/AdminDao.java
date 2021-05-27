package cn.edkso.zd.dao;

import cn.edkso.zd.entry.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDao extends JpaRepository<Admin, Integer>{
    Admin findByUsernameAndPassword(String username, String md5);

    Admin findByUsername(String name);
}

package cn.edkso.zd.dao;


import cn.edkso.zd.entry.Land;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LandDao extends JpaRepository<Land, Integer>, JpaSpecificationExecutor<Land> {
}

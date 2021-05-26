package cn.edkso.zd.dao;

import cn.edkso.zd.entry.Collect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectDao extends JpaRepository<Collect, Integer> {
    void deleteLandId(Integer landId);

    List<Collect> findAllByUserId();
}

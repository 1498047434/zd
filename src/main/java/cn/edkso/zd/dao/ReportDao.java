package cn.edkso.zd.dao;

import cn.edkso.zd.entry.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ReportDao extends JpaRepository<Report, Integer>, JpaSpecificationExecutor<Report> {

}

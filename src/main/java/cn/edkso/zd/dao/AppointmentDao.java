package cn.edkso.zd.dao;

import cn.edkso.zd.entry.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface AppointmentDao extends JpaRepository<Appointment, Integer>, JpaSpecificationExecutor<Appointment> {

}

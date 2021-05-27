package cn.edkso.zd.service;


import cn.edkso.zd.entry.Appointment;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface AppointmentService {

    Page<Appointment> listByPage(Integer page, Integer limit, ThreadLocal<Map<String, Object>> tl);

    public Appointment add(Appointment appointment);

    public Appointment update(Appointment appointment);
}

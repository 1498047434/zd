package cn.edkso.zd.entry;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer landId;
    private String landAddress;
    private Integer landClassify;

    private Integer userId;
    private String userName;
    private String userTel;

    private Integer adminId;
    private String adminName;
    private String adminTel;

    private Integer state;
    private Timestamp appointedTime;
    private String appointedAddress;


}

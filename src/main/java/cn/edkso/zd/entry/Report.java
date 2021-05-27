package cn.edkso.zd.entry;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer landId;
    private String landName;
    private Integer landClassify;
    private Integer userId;
    private String userName;
    private Integer adminId;
    private String adminName;
}

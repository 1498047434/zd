package cn.edkso.zd.entry;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
public class Land {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String address;
    private Integer classify; //1林地，2鱼塘，3果园，4菜棚，5耕地
    private Integer area;
    private BigDecimal price;
    private Integer contractPeriod;
    private String introduction;
    private Integer adminId;
    private String adminName;
    private Integer userId;
    private String userName;
    private Integer isBidding; //1竞价中,0未竟价
    private BigDecimal bidPrice;
    private Integer bidUserId;
    private String bidUserName;
    private Integer state = 1;

    @Transient //不进行持久化（与数据库非关）
    private Integer isCollect = 0; //0未收藏，1收藏
}

package com.ruoyi.ticket.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 景点预约
 *
 * @Author fanjaixing
 * @Date 2024/4/25 23:02
 */
@TableName("etp_reservation")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AttractionsReservation implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long reservationId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 景点ID
     */
    private Long attractionsId;

    /**
     * 预约起始时间
     */
    private LocalDateTime startTime;

    /**
     * 预约终止时间
     */
    private LocalDateTime endTime;

    /**
     * 预约状态，0预约，1取消
     */
    private Integer reservationStatus;

    private static final long serialVersionUID = 1L;
}

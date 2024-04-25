package com.ruoyi.ticket.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购票记录
 *
 * @Author fanjaixing
 * @Date 2024/4/25 23:04
 */
@TableName("etp_ticket")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Ticket implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long ticketId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 景点ID
     */
    private Long attractionsId;

    /**
     * 门票类型：0 收入，1 全价票，2 优惠票
     */
    private Integer ticketType;

    /**
     * 门票状态：0已购票，1已退票
     */
    private Integer ticketStatus;

    /**
     * 交易金额
     */
    private BigDecimal ticketAmount;

    /**
     * 交易时间
     */
    private LocalDateTime transactionTime;

    /**
     * 余额
     */
    private BigDecimal balance;

    private static final long serialVersionUID = 1L;
}

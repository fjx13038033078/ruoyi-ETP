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

/**
 * 景点表
 *
 * @Author fanjaixing
 * @Date 2024/4/25 23:00
 */
@TableName("etp_attractions")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Attractions implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long attractionsId;

    /**
     * 景点名称
     */
    private String attractionsName;

    /**
     * 景点位置
     */
    private String location;

    /**
     * 景点描述
     */
    private String description;

    /**
     * 景点票价
     */
    private BigDecimal ticketPrice;

    /**
     * 景点图片
     */
    private String image;

    private static final long serialVersionUID = 1L;
}

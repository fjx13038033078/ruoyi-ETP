package com.ruoyi.ticket.service;

import com.ruoyi.ticket.domain.AttractionsReservation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 景点预约 Service 接口
 *
 * @Author fanjaixing
 * @Date 2024/4/25 23:20
 */
public interface AttractionsReservationService {
    /**
     * 获取所有预约记录
     * @return 所有预约记录列表
     */
    List<AttractionsReservation> getAllReservations();

    /**
     * 根据预约ID获取预约信息
     * @param reservationId 预约ID
     * @return 预约信息
     */
    AttractionsReservation getReservationById(Long reservationId);

    /**
     * 取消预约
     * @param reservation 待添加的预约信息
     * @return 添加成功返回 true，否则返回 false
     */
    boolean addReservation(AttractionsReservation reservation);

    /**
     * 更新预约记录
     * @param reservationId 待更新的预约信息ID
     */
    void cancelReservation(Long reservationId);

    /**
     * 根据预约ID获取应支付票价
     * @param reservationId 预约ID
     * @return 实际票价，应付票价
     */
    Map<BigDecimal,BigDecimal> getTicketPriceByReservation(Long reservationId);

    /**
     * 获取每个景点被预约的次数
     *
     * @return 每个景点被预约的次数的映射，键为景点名称，值为预约次数
     */
    Map<String, Integer> getReservationCountsByAttractions();
}

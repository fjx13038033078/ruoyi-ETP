package com.ruoyi.ticket.service;

import com.ruoyi.ticket.domain.Reservation;

import java.util.List;

/**
 * 景点预约 Service 接口
 *
 * @Author fanjaixing
 * @Date 2024/4/25 23:20
 */
public interface ReservationService {
    /**
     * 获取所有预约记录
     * @return 所有预约记录列表
     */
    List<Reservation> getAllReservations();

    /**
     * 根据预约ID获取预约信息
     * @param reservationId 预约ID
     * @return 预约信息
     */
    Reservation getReservationById(Long reservationId);

    /**
     * 添加预约记录
     * @param reservation 待添加的预约信息
     * @return 添加成功返回 true，否则返回 false
     */
    boolean addReservation(Reservation reservation);
}

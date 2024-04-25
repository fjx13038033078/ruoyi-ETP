package com.ruoyi.ticket.service.impl;

import com.ruoyi.ticket.domain.Reservation;
import com.ruoyi.ticket.mapper.ReservationMapper;
import com.ruoyi.ticket.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @Author fanjaixing
 * @Date 2024/4/25 23:27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationMapper reservationMapper;

    /**
     * 获取所有预约记录
     * @return 所有预约记录列表
     */
    @Override
    public List<Reservation> getAllReservations() {
        return reservationMapper.getAllReservations();
    }

    /**
     * 根据预约ID获取预约信息
     * @param reservationId 预约ID
     * @return 预约信息
     */
    @Override
    public Reservation getReservationById(Long reservationId) {
        return reservationMapper.getReservationById(reservationId);
    }

    /**
     * 添加预约记录
     * @param reservation 待添加的预约信息
     * @return 添加成功返回 true，否则返回 false
     */
    @Override
    public boolean addReservation(Reservation reservation) {
        int rows = reservationMapper.addReservation(reservation);
        return rows > 0;
    }
}

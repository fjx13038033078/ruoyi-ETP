package com.ruoyi.ticket.service.impl;

import com.ruoyi.ticket.domain.AttractionsReservation;
import com.ruoyi.ticket.mapper.AttractionsReservationMapper;
import com.ruoyi.ticket.service.AttractionsReservationService;
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
public class AttractionsReservationServiceImpl implements AttractionsReservationService {

    private final AttractionsReservationMapper reservationMapper;

    /**
     * 获取所有预约记录
     * @return 所有预约记录列表
     */
    @Override
    public List<AttractionsReservation> getAllReservations() {
        return reservationMapper.getAllReservations();
    }

    /**
     * 根据预约ID获取预约信息
     * @param reservationId 预约ID
     * @return 预约信息
     */
    @Override
    public AttractionsReservation getReservationById(Long reservationId) {
        return reservationMapper.getReservationById(reservationId);
    }

    /**
     * 添加预约记录
     * @param reservation 待添加的预约信息
     * @return 添加成功返回 true，否则返回 false
     */
    @Override
    public boolean addReservation(AttractionsReservation reservation) {
        int rows = reservationMapper.addReservation(reservation);
        return rows > 0;
    }

    /**
     * 更新预约记录
     * @param reservation 待更新的预约信息
     * @return 更新成功返回 true，否则返回 false
     */
    @Override
    public boolean updateReservation(AttractionsReservation reservation) {
        int rows = reservationMapper.updateReservation(reservation);
        return rows > 0;
    }

}

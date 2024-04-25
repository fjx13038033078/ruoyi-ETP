package com.ruoyi.ticket.mapper;

import com.ruoyi.ticket.domain.Reservation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 景点预约Mapper接口
 *
 * @Author fanjaixing
 * @Date 2024/4/25 23:11
 */
@Mapper
public interface ReservationMapper {
    /**
     * 查询所有预约记录
     *
     * @return 预约记录列表
     */
    List<Reservation> getAllReservations();

    /**
     * 根据ID查询预约记录
     *
     * @param reservationId 预约ID
     * @return 对应ID的预约记录
     */
    Reservation getReservationById(Long reservationId);

    /**
     * 根据用户ID查询预约记录
     *
     * @param userId 用户ID
     * @return 对应用户ID的预约记录列表
     */
    List<Reservation> getReservationsByUserId(Long userId);

    /**
     * 根据景点ID查询预约记录
     *
     * @param attractionsId 景点ID
     * @return 对应景点ID的预约记录列表
     */
    List<Reservation> getReservationsByAttractionsId(Long attractionsId);

    /**
     * 添加预约记录
     *
     * @param reservation 待添加的预约记录
     * @return 添加成功返回影响的行数，否则返回0
     */
    int addReservation(Reservation reservation);
}

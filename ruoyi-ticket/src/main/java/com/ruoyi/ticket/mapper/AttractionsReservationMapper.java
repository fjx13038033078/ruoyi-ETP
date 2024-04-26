package com.ruoyi.ticket.mapper;

import com.ruoyi.ticket.domain.AttractionsReservation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 景点预约Mapper接口
 *
 * @Author fanjaixing
 * @Date 2024/4/25 23:11
 */
@Mapper
public interface AttractionsReservationMapper {
    /**
     * 查询所有预约记录
     *
     * @return 预约记录列表
     */
    List<AttractionsReservation> getAllReservations();

    /**
     * 根据ID查询预约记录
     *
     * @param reservationId 预约ID
     * @return 对应ID的预约记录
     */
    AttractionsReservation getReservationById(Long reservationId);

    /**
     * 根据用户ID查询预约记录
     *
     * @param userId 用户ID
     * @return 对应用户ID的预约记录列表
     */
    List<AttractionsReservation> getReservationsByUserId(Long userId);

    /**
     * 根据景点ID查询预约记录
     *
     * @param attractionsId 景点ID
     * @return 对应景点ID的预约记录列表
     */
    List<AttractionsReservation> getReservationsByAttractionsId(Long attractionsId);

    /**
     * 添加预约记录
     *
     * @param reservation 待添加的预约记录
     * @return 添加成功返回影响的行数，否则返回0
     */
    int addReservation(AttractionsReservation reservation);

    /**
     * 更新预约记录
     *
     * @param reservation 待更新的预约记录
     * @return 更新成功返回影响的行数
     */
    int updateReservation(AttractionsReservation reservation);
}

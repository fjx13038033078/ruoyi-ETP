package com.ruoyi.ticket.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.ticket.domain.Attractions;
import com.ruoyi.ticket.domain.AttractionsReservation;
import com.ruoyi.ticket.mapper.AttractionsMapper;
import com.ruoyi.ticket.mapper.AttractionsReservationMapper;
import com.ruoyi.ticket.service.AttractionsReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.ruoyi.common.utils.PageUtils.startPage;

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

    private final AttractionsMapper attractionsMapper;

    private final ISysUserService iSysUserService;

    private final ISysRoleService iSysRoleService;

    /**
     * 获取所有预约记录
     * @return 所有预约记录列表
     */
    @Override
    public List<AttractionsReservation> getAllReservations() {
        // 获取当前登录用户ID
        Long userId = SecurityUtils.getUserId();
        String role = iSysRoleService.selectStringRoleByUserId(userId);
        if (role.equalsIgnoreCase("admin")) {
            startPage();
            List<AttractionsReservation> allReservations = reservationMapper.getAllReservations();
            fillAttractionsAndUserName(allReservations);
            return allReservations;
        } else {
            startPage();
            List<AttractionsReservation> reservationsByUserId = reservationMapper.getReservationsByUserId(userId);
            fillAttractionsAndUserName(reservationsByUserId);
            return reservationsByUserId;
        }
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
        checkReservationTime(reservation);
        // 获取当前登录用户ID
        Long userId = SecurityUtils.getUserId();
        reservation.setUserId(userId);
        reservation.setReservationStatus(0);
        int rows = reservationMapper.addReservation(reservation);
        return rows > 0;
    }

    /**
     * 取消预约
     * @param reservationId 待更新的预约信息Id
     * @return 更新成功返回 true，否则返回 false
     */
    @Override
    public void cancelReservation(Long reservationId) {
        AttractionsReservation reservation = reservationMapper.getReservationById(reservationId);
        if (reservation != null && reservation.getReservationStatus() == 1){
            throw new RuntimeException("已经是取消状态，请勿重复取消");
        }
        else { // 如果预约状态为预约
            assert reservation != null;
            reservation.setReservationStatus(1); // 设置预约状态为取消
            reservationMapper.updateReservation(reservation); // 更新预约信息
        }

    }

    /**
     * 检查当前预约时间是否合理
     * @param reservation
     */
    private void checkReservationTime(AttractionsReservation reservation){
        LocalDateTime startTime = reservation.getStartTime();
        LocalDateTime endTime = reservation.getEndTime();

        // 判断开始时间是否早于早八点
        LocalTime startTimeLimit = LocalTime.of(8, 0);
        if (startTime.toLocalTime().isBefore(startTimeLimit)) {
            throw new RuntimeException("开始时间不能早于早上8点！");
        }

        // 判断结束时间是否晚于晚10点
        LocalTime endTimeLimit = LocalTime.of(22, 0);
        if (endTime.toLocalTime().isAfter(endTimeLimit)) {
            throw new RuntimeException("结束时间不能晚于晚上10点！");
        }

        // 检查开始时间和结束时间之间的间隔是否超过三小时
        long durationHours = Duration.between(startTime, endTime).toHours();
        if (durationHours > 3) {
            throw new RuntimeException("预约时间段不能超过三小时！");
        }
    }

    /**
     * 填充交易记录中的用户和景点名称
     */
    private void fillAttractionsAndUserName(List<AttractionsReservation> attractionsReservations){
        for (AttractionsReservation attractionsReservation : attractionsReservations){
            //获取景点实体
            Long attractionsId = attractionsReservation.getAttractionsId();
            Attractions attractions = attractionsMapper.getAttractionsById(attractionsId);

            //获取用户实体
            Long userId = attractionsReservation.getUserId();
            SysUser sysUser = iSysUserService.selectUserById(userId);

            if (attractions != null){
                attractionsReservation.setAttractionsName(attractions.getAttractionsName());
            } else {
                attractionsReservation.setAttractionsName("该景点目前不在订票名单中");
            }
            if (sysUser != null) {
                attractionsReservation.setUserName(sysUser.getNickName());
            } else {
                attractionsReservation.setUserName("用户已注销");
            }
        }
    }
}

package com.ruoyi.ticket.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.ticket.domain.Attractions;
import com.ruoyi.ticket.domain.AttractionsReservation;
import com.ruoyi.ticket.domain.Ticket;
import com.ruoyi.ticket.mapper.TicketMapper;
import com.ruoyi.ticket.service.AttractionsReservationService;
import com.ruoyi.ticket.service.AttractionsService;
import com.ruoyi.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author fanjaixing
 * @Date 2024/4/25 23:28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketMapper ticketMapper;

    private final AttractionsReservationService attractionsReservationService;

    private final AttractionsService attractionsService;

    private final ISysUserService iSysUserService;

    private final ISysRoleService iSysRoleService;

    /**
     * 获取所有购票记录
     *
     * @return 所有购票记录列表
     */
    @Override
    public List<Ticket> getAllTickets() {
        return ticketMapper.getAllTickets();
    }

    /**
     * 根据购票记录ID获取购票信息
     * @param ticketId 购票记录ID
     * @return 购票信息
     */
    @Override
    public Ticket getTicketById(Long ticketId) {
        return ticketMapper.getTicketById(ticketId);
    }

    /**
     * 添加购票记录
     * @param ticket 待添加的购票信息
     * @return 添加成功返回 true，否则返回 false
     */
    @Override
    public boolean addTicket(Ticket ticket) {
        // 获取当前登录用户ID
        Long userId = SecurityUtils.getUserId();
        // 获取当前登录用户姓名
        String userName = SecurityUtils.getUsername();
        // 获取当前用户的余额
        BigDecimal userBalance = iSysUserService.selectUserById(userId).getBalance();
        // 获取当前用户的角色
        String role = iSysRoleService.selectStringRoleByUserId(userId);
        if ("admin".equals(role)) {
            throw new RuntimeException("管理员无法购买");
        }
        Long reservationId = ticket.getReservationId();
        AttractionsReservation reservation = attractionsReservationService.getReservationById(reservationId);
        if (reservation.getReservationStatus() == 1){
            throw new RuntimeException("预约已取消，无法支付，请重新预约");
        }

        Attractions attractions = attractionsService.getAttractionsById(reservation.getAttractionsId());
        BigDecimal transactionAmount = attractions.getTicketPrice();

        // 判断余额是否足够支付课程费用
        if (userBalance.compareTo(transactionAmount) < 0) {
            throw new RuntimeException("您的余额不足");
        }
        // 获取管理员当前余额
        BigDecimal adminBalance = iSysUserService.selectUserById(1L).getBalance();
        // 计算用户实际交易金额（考虑VIP用户）
        BigDecimal actualTransactionAmount = "common".equals(role) ? transactionAmount : transactionAmount.multiply(
                BigDecimal.valueOf(0.5)).setScale(2, RoundingMode.HALF_UP);
        // 将用户实际消费的金额从用户余额中扣除
        BigDecimal newBalance = userBalance.subtract(actualTransactionAmount);
        // 更新用户余额信息
        SysUser currentUser = iSysUserService.selectUserById(userId);
        currentUser.setBalance(newBalance);
        iSysUserService.updateUserBalance(currentUser);
        // 设置用户消费记录信息
        ticket.setReservationId(reservationId);
        ticket.setAttractionsId(reservation.getAttractionsId());
        ticket.setUserId(userId);
        ticket.setTicketType("common".equals(role) ? 1 : 2);
        ticket.setTicketAmount(actualTransactionAmount);
        ticket.setTransactionTime(LocalDateTime.now());
        ticket.setBalance(newBalance);
        ticket.setTicketStatus(0);
        // 添加用户消费记录
        ticketMapper.addTicket(ticket);

        // 更新管理员的余额信息
        BigDecimal newAdminBalance = adminBalance.add(actualTransactionAmount);
        SysUser admin = iSysUserService.selectUserById(1L);
        admin.setBalance(newAdminBalance);
        iSysUserService.updateUserBalance(admin);
        return true;
    }

    /**
     * 更新购票记录
     * @param ticket 待更新的购票信息
     * @return 更新成功返回 true，否则返回 false
     */
    @Override
    public boolean updateTicket(Ticket ticket) {
        int rows = ticketMapper.updateTicket(ticket);
        return rows > 0;
    }
}

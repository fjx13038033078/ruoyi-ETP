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

import static com.ruoyi.common.utils.PageUtils.startPage;

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
        // 获取当前登录用户ID
        Long userId = SecurityUtils.getUserId();
        String role = iSysRoleService.selectStringRoleByUserId(userId);
        if ("admin".equals(role)) {
            startPage();
            List<Ticket> allTickets = ticketMapper.getAllTickets();
            fillUserAndAttractionsName(allTickets);
            return allTickets;
        } else {
            startPage();
            List<Ticket> ticketsByUserId = ticketMapper.getTicketsByUserId(userId);
            fillUserAndAttractionsName(ticketsByUserId);
            return ticketsByUserId;
        }
    }

    /**
     * 根据购票记录ID获取购票信息
     *
     * @param ticketId 购票记录ID
     * @return 购票信息
     */
    @Override
    public Ticket getTicketById(Long ticketId) {
        return ticketMapper.getTicketById(ticketId);
    }

    /**
     * 添加购票记录
     *
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
//        if ("admin".equals(role)) {
//            throw new RuntimeException("管理员无法购买");
//        }
        Long reservationId = ticket.getReservationId();
        AttractionsReservation reservation = attractionsReservationService.getReservationById(reservationId);
        if (reservation.getReservationStatus() == 1) {
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
     * 处理退款操作
     *
     * @param ticketId 待退款的购票记录ID
     */
    @Override
    public void refundTicket(Long ticketId) {
        // 获取购票信息
        Ticket ticket = ticketMapper.getTicketById(ticketId);

        // 判断购票记录是否存在
        if (ticket == null) {
            throw new RuntimeException("购票记录不存在，无法退款");
        }

        // 判断购票记录的状态是否为已购买
        if (ticket.getTicketStatus() == 1) {
            throw new RuntimeException("请勿重复退款");
        }

        // 获取购票金额
        BigDecimal ticketAmount = ticket.getTicketAmount();

        // 获取当前用户的余额
        Long userId = ticket.getUserId();
        BigDecimal userBalance = iSysUserService.selectUserById(userId).getBalance();

        // 更新用户余额
        BigDecimal newBalance = userBalance.add(ticketAmount);
        SysUser currentUser = iSysUserService.selectUserById(userId);
        currentUser.setBalance(newBalance);
        iSysUserService.updateUserBalance(currentUser);

        // 更新购票记录状态为已退款
        ticket.setTicketStatus(1);
        ticketMapper.updateTicket(ticket);

        // 获取管理员的余额
        Long adminId = 1L; // 管理员用户ID
        BigDecimal adminBalance = iSysUserService.selectUserById(adminId).getBalance();

        // 更新管理员余额
        BigDecimal newAdminBalance = adminBalance.subtract(ticketAmount);
        SysUser admin = iSysUserService.selectUserById(adminId);
        admin.setBalance(newAdminBalance);
        iSysUserService.updateUserBalance(admin);
    }

    /**
     * 更新购票记录
     *
     * @param ticket 待更新的购票信息
     * @return 更新成功返回 true，否则返回 false
     */
    @Override
    public boolean updateTicket(Ticket ticket) {
        int rows = ticketMapper.updateTicket(ticket);
        return rows > 0;
    }

    /**
     * 填充用户和景点名称
     *
     * @param tickets 待填充的购票记录列表
     */
    private void fillUserAndAttractionsName(List<Ticket> tickets) {
        tickets.forEach(ticket -> {
            Long userId = ticket.getUserId();
            Long attractionsId = ticket.getAttractionsId();
            SysUser user = iSysUserService.selectUserById(userId);
            Attractions attractions = attractionsService.getAttractionsById(attractionsId);
            if (user != null){
                ticket.setUserName(user.getNickName());
            } else {
                ticket.setUserName("用户已注销");
            }
            if (attractions != null){
                ticket.setAttractionsName(attractions.getAttractionsName());
            } else {
                ticket.setAttractionsName("景点已删除");
            }

        });
    }

}

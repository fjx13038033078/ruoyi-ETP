package com.ruoyi.ticket.service;

import com.ruoyi.ticket.domain.Ticket;

import java.util.List;

/**
 * 购票记录 Service 接口
 *
 * @Author fanjaixing
 * @Date 2024/4/25 23:20
 */
public interface TicketService {
    /**
     * 获取所有购票记录
     * @return 所有购票记录列表
     */
    List<Ticket> getAllTickets();

    /**
     * 根据购票记录ID获取购票信息
     * @param ticketId 购票记录ID
     * @return 购票信息
     */
    Ticket getTicketById(Long ticketId);

    /**
     * 添加购票记录
     * @param ticket 待添加的购票信息
     * @return 添加成功返回 true，否则返回 false
     */
    boolean addTicket(Ticket ticket);

    /**
     * 处理退款操作
     * @param ticketId 待退款的购票记录ID
     */
    public void refundTicket(Long ticketId);

    /**
     * 更新购票记录
     * @param ticket 待更新的购票信息
     * @return 更新成功返回 true，否则返回 false
     */
    boolean updateTicket(Ticket ticket);
}

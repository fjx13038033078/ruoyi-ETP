package com.ruoyi.ticket.service.impl;

import com.ruoyi.ticket.domain.Ticket;
import com.ruoyi.ticket.mapper.TicketMapper;
import com.ruoyi.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        int rows = ticketMapper.addTicket(ticket);
        return rows > 0;
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

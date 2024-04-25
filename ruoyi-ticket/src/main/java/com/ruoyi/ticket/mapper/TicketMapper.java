package com.ruoyi.ticket.mapper;

import com.ruoyi.ticket.domain.Ticket;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 购票记录Mapper接口
 *
 * @Author fanjaixing
 * @Date 2024/4/25 23:12
 */
@Mapper
public interface TicketMapper {
    /**
     * 查询所有购票记录
     *
     * @return 购票记录列表
     */
    List<Ticket> getAllTickets();

    /**
     * 根据ID查询购票记录
     *
     * @param ticketId 购票记录ID
     * @return 对应ID的购票记录
     */
    Ticket getTicketById(Long ticketId);

    /**
     * 根据用户ID查询购票记录
     *
     * @param userId 用户ID
     * @return 对应用户ID的购票记录列表
     */
    List<Ticket> getTicketsByUserId(Long userId);

    /**
     * 根据景点ID查询购票记录
     *
     * @param attractionsId 景点ID
     * @return 对应景点ID的购票记录列表
     */
    List<Ticket> getTicketsByAttractionsId(Long attractionsId);

    /**
     * 添加购票记录
     *
     * @param ticket 待添加的购票记录
     * @return 添加成功返回影响的行数，否则返回0
     */
    int addTicket(Ticket ticket);
}

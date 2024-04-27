package com.ruoyi.web.controller.ticket;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.ticket.domain.Attractions;
import com.ruoyi.ticket.domain.Ticket;
import com.ruoyi.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2024/4/26 11:41
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/ticket/ticket")
public class TicketController extends BaseController {

    private final TicketService ticketService;

    /**
     * 获取所有门票
     * @return
     */
    @GetMapping("/listAll")
    public TableDataInfo listAllTickets() {
        startPage();
        List<Ticket> allTickets = ticketService.getAllTickets();
        return getDataTable(allTickets);
    }

    /**
     * 获取门票详情
     * @param ticketId
     * @return
     */
    @GetMapping("/detail")
    public AjaxResult getTicketById(@RequestParam Long ticketId) {
        Ticket ticket = ticketService.getTicketById(ticketId);
        return success(ticket);
    }

    /**
     * 添加门票
     * @param ticket
     * @return
     */
    @PostMapping("/add")
    public AjaxResult addTicket(@RequestBody Ticket ticket) {
        return toAjax(ticketService.addTicket(ticket));
    }

    /**
     * 更新门票
     * @param ticket
     * @return
     */
    @PostMapping("/update")
    public AjaxResult updateTicket(@RequestBody Ticket ticket) {
        return toAjax( ticketService.updateTicket(ticket));
    }
}

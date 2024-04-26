package com.ruoyi.web.controller.ticket;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.ticket.domain.Attractions;
import com.ruoyi.ticket.domain.AttractionsReservation;
import com.ruoyi.ticket.service.AttractionsReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2024/4/26 11:24
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/ticket/reservation")
public class AttractionsReservationController extends BaseController {

    private final AttractionsReservationService reservationService;

    /**
     * 获取所有预约
     * @return
     */
    @GetMapping("/listAll")
    public TableDataInfo listAllReservations() {
        startPage();
        List<AttractionsReservation> allAttractions = reservationService.getAllReservations();
        return getDataTable(allAttractions);
    }

    /**
     * 获取预约详情
     *
     * @param reservationId
     * @return
     */
    @GetMapping("/detail")
    public AjaxResult getReservationById(Long reservationId) {
        return success(reservationService.getReservationById(reservationId));
    }

    /**
     * 添加预约
     *
     * @param reservation
     * @return
     */
    @GetMapping("/add")
    public AjaxResult addReservation(AttractionsReservation reservation) {
        return toAjax(reservationService.addReservation(reservation));
    }

    /**
     * 修改预约
     *
     * @param reservation
     * @return
     */
    @GetMapping("/update")
    public AjaxResult updateReservation(AttractionsReservation reservation) {
        return toAjax(reservationService.updateReservation(reservation));
    }

}

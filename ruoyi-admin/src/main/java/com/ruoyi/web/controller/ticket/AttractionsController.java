package com.ruoyi.web.controller.ticket;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.featherball.domain.Court;
import com.ruoyi.ticket.domain.Attractions;
import com.ruoyi.ticket.service.AttractionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2024/4/26 9:15
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/ticket/attractions")
public class AttractionsController extends BaseController {

    private final AttractionsService attractionsService;

    /**
     * 获取所有景点
     * @return
     */
    @GetMapping("/listAll")
    public TableDataInfo listAllAttractions() {
        startPage();
        List<Attractions> allAttractions = attractionsService.getAllAttractions();
        return getDataTable(allAttractions);
    }

    /**
     * 获取景点详情
     * @param attractionsId
     * @return
     */
    @GetMapping("/detail")
    public AjaxResult getCourtById(@RequestParam Long attractionsId) {
        return success(attractionsService.getAttractionsById(attractionsId));
    }

    /**
     * 添加景点
     * @param attractions
     * @return
     */
    @PostMapping("/add")
    public AjaxResult addAttractions(@RequestBody Attractions attractions) {
        return toAjax(attractionsService.addAttractions(attractions));
    }

    /**
     * 更新景点
     * @param attractions
     * @return
     */
    @PostMapping("/update")
    public AjaxResult updateAttractions(@RequestBody Attractions attractions) {
        return toAjax(attractionsService.updateAttractions(attractions));
    }

    /**
     * 删除景点
     * @param attractionsId
     * @return
     */
    @GetMapping("/delete")
    public AjaxResult deleteAttractionsById(Long attractionsId) {
        return toAjax(attractionsService.deleteAttractionsById(attractionsId));
    }
}

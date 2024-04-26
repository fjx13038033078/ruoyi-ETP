package com.ruoyi.ticket.service;

import com.ruoyi.ticket.domain.Attractions;

import java.util.List;

/**
 * 景点管理 Service 接口
 *
 * @Author fanjaixing
 * @Date 2024/4/25 23:19
 */
public interface AttractionsService {
    /**
     * 获取所有景点
     * @return 所有景点列表
     */
    List<Attractions> getAllAttractions();

    /**
     * 根据景点ID获取景点信息
     * @param attractionsId 景点ID
     * @return 景点信息
     */
    Attractions getAttractionsById(Long attractionsId);

    /**
     * 添加景点
     * @param attractions 待添加的景点信息
     * @return 添加成功返回 true，否则返回 false
     */
    boolean addAttractions(Attractions attractions);

    /**
     * 删除景点
     * @param attractionsId 待删除的景点ID
     * @return 删除成功返回 true，否则返回 false
     */
    boolean deleteAttractionsById(Long attractionsId);

    /**
     * 更新景点
     * @param attractions 待更新的景点信息
     * @return 更新成功返回 true，否则返回 false
     */
    boolean updateAttractions(Attractions attractions);
}

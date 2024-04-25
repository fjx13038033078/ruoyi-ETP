package com.ruoyi.ticket.service.impl;

import com.ruoyi.ticket.domain.Attractions;
import com.ruoyi.ticket.mapper.AttractionsMapper;
import com.ruoyi.ticket.service.AttractionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author fanjaixing
 * @Date 2024/4/25 23:26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AttractionsServiceImpl implements AttractionsService {

    private final AttractionsMapper attractionsMapper;

    /**
     * 获取所有景点
     * @return 所有景点列表
     */
    @Override
    public List<Attractions> getAllAttractions() {
        return attractionsMapper.getAllAttractions();
    }

    /**
     * 根据景点ID获取景点信息
     * @param attractionsId 景点ID
     * @return 景点信息
     */
    @Override
    public Attractions getAttractionsById(Long attractionsId) {
        return attractionsMapper.getAttractionsById(attractionsId);
    }

    /**
     * 添加景点
     * @param attractions 待添加的景点信息
     * @return 添加成功返回 true，否则返回 false
     */
    @Override
    public boolean addAttractions(Attractions attractions) {
        int rows = attractionsMapper.addAttractions(attractions);
        return rows > 0;
    }
}

package com.ruoyi.ticket.mapper;

import com.ruoyi.ticket.domain.Attractions;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 景点Mapper接口
 *
 * @Author fanjaixing
 * @Date 2024/4/25 23:10
 */
@Mapper
public interface AttractionsMapper {
    /**
     * 查询所有景点
     *
     * @return 景点列表
     */
    List<Attractions> getAllAttractions();

    /**
     * 根据ID查询景点
     *
     * @param attractionsId 景点ID
     * @return 对应ID的景点
     */
    Attractions getAttractionsById(Long attractionsId);

    /**
     * 添加景点
     *
     * @param attractions 待添加的景点
     * @return 添加成功返回影响的行数，否则返回0
     */
    int addAttractions(Attractions attractions);
}

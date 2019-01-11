package com.typeng.hydrology.mapper;

import com.typeng.hydrology.model.HydrologicalInfo;
import com.typeng.hydrology.model.qo.HydrologicalInfoQo;

import java.util.List;

/**
 * @author Tianying Peng
 * @date 2019/1/10 10:29
 */
public interface HnswMapper {

    /**
     * 通过id查新水文信息记录
     *
     * @param id
     * @return
     */
    HydrologicalInfo selectById(Integer id);

    /**
     * 通过 Query Object 条件查询对应的水文信息记录
     *
     * @param qo
     * @return
     */
    List<HydrologicalInfo> selectByQo(HydrologicalInfoQo qo);

    /**
     * 添加记录
     *
     * @param info
     * @return
     */
    int insertInfo(HydrologicalInfo info);

    /**
     * 批量添加
     *
     * @param infos
     * @return
     */
    int insertInfoBatch(List<HydrologicalInfo> infos);

}

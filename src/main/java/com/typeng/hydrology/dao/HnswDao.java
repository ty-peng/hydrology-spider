package com.typeng.hydrology.dao;

import com.typeng.hydrology.model.HydrologicalInfo;
import com.typeng.hydrology.model.qo.HydrologicalInfoQo;

import java.util.List;

/**
 * @author Tianying Peng
 * @date 2018-12-25 14:46
 */
public interface HnswDao {
    /**
     * 插入记录.
     *
     * @param info
     * @return
     */
    Integer insertHydrologicalInfo(HydrologicalInfo info);

    /**
     * 批量插入.
     *
     * @param infos
     * @return
     */
    Integer insertHydrologicalInfoBatch(List<HydrologicalInfo> infos);

    /**
     * 条件查询水文信息记录.
     *
     * @param qo
     * @return
     */
    List<HydrologicalInfo> getHydrologicalInfoList(HydrologicalInfoQo qo);
}

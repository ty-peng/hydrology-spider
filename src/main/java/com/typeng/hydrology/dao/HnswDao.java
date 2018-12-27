package com.typeng.hydrology.dao;

import com.typeng.hydrology.model.HydrologicalInfo;
import com.typeng.hydrology.model.qo.HydrologicalInfoQo;

import java.sql.SQLException;
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
    Integer insertHydrologicalInfo(HydrologicalInfo info) throws SQLException;

    /**
     * 批量插入.
     *
     * @param infos
     * @return
     */
    void insertHydrologicalInfoBatch(List<HydrologicalInfo> infos) throws SQLException;

    /**
     * 条件查询水文信息记录.
     *
     * @param qo
     * @return
     */
    List<HydrologicalInfo> getHydrologicalInfoList(HydrologicalInfoQo qo) throws SQLException;
}

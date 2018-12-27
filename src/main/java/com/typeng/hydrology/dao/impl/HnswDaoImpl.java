package com.typeng.hydrology.dao.impl;

import com.typeng.hydrology.dao.HnswDao;
import com.typeng.hydrology.model.HydrologicalInfo;
import com.typeng.hydrology.model.qo.HydrologicalInfoQo;
import com.typeng.hydrology.utils.C3P0Utils;
import com.typeng.hydrology.utils.DBConnection;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Tianying Peng
 * @date 2018/12/27 13:25
 */
public class HnswDaoImpl implements HnswDao {
    private QueryRunner runner = null;
    private Connection con;

    public HnswDaoImpl() {
        runner = new QueryRunner();
    }

    public HnswDaoImpl(Connection con) {
        this.con = con;
        runner = new QueryRunner();
    }

    @Override
    public Integer insertHydrologicalInfo(HydrologicalInfo info) throws SQLException {
        String sql = "insert into hnsw(river_name, station_name, date, time, water_level, fluctuation, flow) values(?,?,?,?,?,?,?)";
        int result = runner.update(con, sql,
                info.getRiverName(), info.getStationName(), info.getDate(), info.getTime(),
                info.getWaterLevel(), info.getFluctuation(), info.getFlow());
        return result;
    }

    @Override
    public void insertHydrologicalInfoBatch(List<HydrologicalInfo> infos) throws SQLException {
        for (HydrologicalInfo info : infos) {
            insertHydrologicalInfo(info);
        }
    }

    @Override
    public List<HydrologicalInfo> getHydrologicalInfoList(HydrologicalInfoQo qo) throws SQLException {
        return null;
    }
}

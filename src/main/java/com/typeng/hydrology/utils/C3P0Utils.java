package com.typeng.hydrology.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Tianying Peng
 * @date 2018/12/27 15:57
 */
public class C3P0Utils {

    public static Connection getConnection() throws SQLException {
        DataSource ds = new ComboPooledDataSource();// c3p0自己去读配置文件
        return ds.getConnection();
    }

}

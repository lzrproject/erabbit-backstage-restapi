package erabbit.oauth.web.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author 111
 * @Date 2022/6/30 15:39
 * @Description
 */
public class JDBCTest {
    public static void main(String[] args) {
        Connection conn = JdbcUtils.getConnection();
        String sqlStr = "select * from `user`";

        try {
            PreparedStatement pst = conn.prepareStatement(sqlStr);
            ResultSet resultSet = pst.executeQuery(sqlStr);
            String tableName = resultSet.getMetaData().getTableName(1);
            System.out.println(tableName);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

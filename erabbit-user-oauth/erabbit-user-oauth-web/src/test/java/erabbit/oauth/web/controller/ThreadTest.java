package erabbit.oauth.web.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author 111
 * @Date 2022/6/15 20:45
 * @Description
 */
public class ThreadTest implements Runnable{

    public static void main(String[] args) {
        new Thread(new ThreadTest(),"1").start();
        new Thread(new ThreadTest(),"1001").start();
        new Thread(new ThreadTest(),"2001").start();
        new Thread(new ThreadTest(),"3001").start();
        new Thread(new ThreadTest(),"4001").start();
    }


    @Override
    public void run() {
        long start = System.currentTimeMillis();
        int num = Integer.parseInt(Thread.currentThread().getName());
        int endNum = num+999;
        try {
            Connection conn = JdbcUtils.getConnection();
            String sqlStr = "INSERT INTO `user`(`username`,`password`) VALUES(?,?) ";
            conn.setAutoCommit(false);
            PreparedStatement pst = conn.prepareStatement(sqlStr);
//            for (int i = 1;i <= 10;i++) {
//                StringBuffer stringBuffer = new StringBuffer();

            for (int j = num; j <= endNum; j++) {
//                    stringBuffer.append("('user"+j+"','admins'),");
                pst.setString(1,"user"+j);
                pst.setString(2,"admins");
                pst.addBatch();  //添加sql
                if (j % 500 == 0){
                    pst.executeBatch();
                    pst.clearBatch();
                }
                pst.executeBatch(); //执行操作
            }

//                String sql = sqlStr + stringBuffer.substring(0, stringBuffer.length() - 1);

            conn.commit();      //提交事务

//            }

            pst.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("1000条数据插入时间为："+(end-start)+"ms");
    }
}
class JdbcUtils{
    static String url = "jdbc:mysql://118.25.242.174:3306/erabbit_data?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowMultiQueries=true&serverTimezone=UTC";
    static String driverName = "com.mysql.cj.jdbc.Driver";
    static String username = "root";
    static String password = "123456";

    public static Connection getConnection(){
        Connection conn = null;
        try {
//            Class.forName(driverName);
            conn = DriverManager.getConnection(url, username, password);
            conn.setAutoCommit(false);  //关闭自动提交

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }
}

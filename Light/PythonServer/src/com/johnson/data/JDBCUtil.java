package com.johnson.data;
import java.sql.*;

public class JDBCUtil {
    private static final String URL="jdbc:mysql://localhost:3306/mysql?characterEncoding=UTF-8";
    private static final String NAME="root";
    private static final String PASSWORD="zqs@@740848126";
    private static Connection con;
    //静态代码块（将加载驱动、连接数据库放入静态块中）
    static {
        try {
            //1.加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            //2.获得数据库的连接
            con = DriverManager.getConnection(URL, NAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void openJDBC(){

    }

    public static Connection getConnection() {
        return con;
    }

    public static void createDB(String dbName) throws SQLException {
        String dataBaseName=dbName;
        Statement stat = con.createStatement();
        //创建数据库hello
        stat.executeUpdate("CREATE DATABASE IF NOT EXISTS "+dataBaseName+" default charset utf8 COLLATE utf8_general_ci; ");
        stat.close();
        con.close();
    }

    public static void createTable(String dataBaseName , String tableName) throws SQLException {
        String url=URL.replace("mysql", dataBaseName);
        con = DriverManager.getConnection(url, NAME, PASSWORD);
        Statement statement = con.createStatement();
        //创建表test
        statement.executeUpdate("create table "+dataBaseName+"(id int, name varchar(80))");
    }
}

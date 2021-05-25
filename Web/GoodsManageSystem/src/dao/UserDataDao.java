package dao;

import entity.User;
import entity.UserData;
import utils.SQLUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UserDataDao {
    public UserData getDataByName(String name) throws Exception {
        UserData userData = new UserData();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Integer> bloodList = new ArrayList<Integer>();
        ArrayList<Integer> heartList = new ArrayList<Integer>();
        ArrayList<String> timeList = new ArrayList<String>();
        try {
            con = SQLUtils.getConnection();
            ps = con.prepareStatement("select * from t_user_data where name=?");
            ps.setString(1, name);
            rs=ps.executeQuery();
            while (rs.next()) {
                int blood = rs.getInt("blood");
                int heart  = rs.getInt("heart");
                String time = rs.getString("time");
                bloodList.add(blood);
                heartList.add(heart);
                timeList.add(time);
//                user = new User();
//                user.setId(rs.getInt("id"));
//                user.setRole(rs.getString("role"));
//                user.setName(rs.getString("name"));
//                user.setPwd(rs.getString("pwd"));

//                System.out.println("这里！！！！！！！！！！"+ blood  + "  + " +heart + " ++ "+time);
            }
            userData.setBloodList(bloodList);
            userData.setHeartList(heartList);
            userData.setTimeList(timeList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("数据库访问出现异常:" + e);
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
            if (con != null)
                con.close();
        }
        return userData;
    }

    public UserData getDataByID(int id) throws Exception {
        UserData userData = new UserData();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Integer> bloodList = new ArrayList<Integer>();
        ArrayList<Integer> heartList = new ArrayList<Integer>();
        ArrayList<String> timeList = new ArrayList<String>();
        try {
            con = SQLUtils.getConnection();
//            select * from (select * from 表名 order by 字段 desc limit 10) 临时表 order by 字段
            ps = con.prepareStatement("select * from (select * from t_user_data where id=? order by time desc limit 10) temp order by time asc");
            ps.setInt(1, id);
            rs=ps.executeQuery();
            while (rs.next()) {
                int blood = rs.getInt("blood");
                int heart  = rs.getInt("heart");
                String time = rs.getString("time");
                bloodList.add(blood);
                heartList.add(heart);
                timeList.add(time);
//                System.out.println("这里！！！！！！！！！！"+ blood  + "  + " +heart + " ++ "+time);
            }
            userData.setBloodList(bloodList);
            userData.setHeartList(heartList);
            userData.setTimeList(timeList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("数据库访问出现异常:" + e);
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
            if (con != null)
                con.close();
        }
        return userData;
    }
}

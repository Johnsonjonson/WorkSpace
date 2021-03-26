package dao;

import entity.InfoData;
import utils.SQLUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DataDao {
	public InfoData getData() throws Exception {
		InfoData infoData = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = SQLUtils.getConnection();
			ps = con.prepareStatement("select * from t_data");
			rs=ps.executeQuery();
			if (rs.next()) {
				infoData = new InfoData();
				infoData.setBlood(rs.getInt("blood"));
				infoData.setIsAlarm(rs.getInt("isAlarm"));
				infoData.setTemp(rs.getFloat("temp"));
				infoData.setHeart(rs.getInt("heart"));
				infoData.setHavePeople(rs.getInt("havePeople"));

//				System.out.println("这里！！！！！！！！！！"+infoData);
			}
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
		return infoData;
	}
}

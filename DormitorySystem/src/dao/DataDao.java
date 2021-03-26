package dao;

import entity.InfoData;
import entity.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class DataDao {
	public InfoData getData() throws Exception {
		InfoData infoData = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dormitory?useUnicode=true&characterEncoding=utf8",
					"root", "zqs@@740848126");
			ps = con.prepareStatement("select * from t_data");
			rs=ps.executeQuery();
			if (rs.next()) {
				infoData = new InfoData();
				infoData.setHumidity(rs.getInt("humdity"));
				infoData.setIsFire(rs.getInt("isFire"));
				infoData.setTemp(rs.getFloat("temp"));
				infoData.setRealTimeNum(rs.getInt("realTimeNum"));

				System.out.println("这里！！！！！！！！！！"+infoData);
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

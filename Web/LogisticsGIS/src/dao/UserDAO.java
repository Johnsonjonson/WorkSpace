package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.User;
import utils.SQLUtils;


public class UserDAO {
	public User getByName(String name) throws Exception {
		User user = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = SQLUtils.getConnection();
			ps = con.prepareStatement("select * from t_user where name=?");
			ps.setString(1, name);
			rs=ps.executeQuery();
			if (rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setRole(rs.getString("role"));
				user.setName(rs.getString("name"));
				user.setPwd(rs.getString("pwd"));
				user.setSclass(rs.getString("sclass"));
				user.setNo(rs.getString("no"));
				user.setSex(rs.getString("sex"));

				System.out.println("这里！！！！！！！！！！");
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
		return user;
	}
	
	public User getById(int	id) throws Exception {
		User user = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = SQLUtils.getConnection();
			ps = con.prepareStatement("select * from t_user as u where u.id=?");
			ps.setInt(1, id);
			rs=ps.executeQuery();
			if (rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setRole(rs.getString("role"));
				user.setName(rs.getString("name"));
				user.setPwd(rs.getString("pwd"));
				user.setSclass(rs.getString("sclass"));
				user.setNo(rs.getString("no"));
				user.setSex(rs.getString("sex"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("数据库访问异常:" + e);
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (con != null)
				con.close();
		}
		return user;
	}
	
	public List<User> searchCustomer(String customerName) throws Exception{
		List<User> users=new ArrayList<User>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = SQLUtils.getConnection();
			System.out.println("==========searchCustomer=========  " + customerName);
			ps = con.prepareStatement("select * from t_user as u where u.name like ? and u.role='customer'");
			ps.setString(1, "%"+customerName+"%");
			rs=ps.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setRole(rs.getString("role"));
				user.setName(rs.getString("name"));
				user.setPwd(rs.getString("pwd"));
				users.add(user);
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
		return users;
	}

	public List<User> searchAllStudent() throws Exception{
		List<User> users=new ArrayList<User>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = SQLUtils.getConnection();
			ps = con.prepareStatement("select * from t_user as u where u.role='student'");
//			ps.setString(1, "%"+customerName+"%");
			rs=ps.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setRole(rs.getString("role"));
				user.setName(rs.getString("name"));
				user.setPwd(rs.getString("pwd"));
				users.add(user);
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
		return users;
	}


	public void save(User user) throws Exception{
		Connection con = null;
		PreparedStatement ps = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = SQLUtils.getConnection();
			ps = con.prepareStatement("insert into t_user value(null,?,?,?,null,null,null)");
			ps.setString(1, user.getName());
			ps.setString(2, user.getPwd());
			ps.setString(3, user.getRole());
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("数据库访问出现异常:" + e);
		} finally {

			if (ps != null)
				ps.close();
			if (con != null)
				con.close();
		}
	}

	public void add(String name,String sclass,String no,String sex) throws Exception{
		Connection con = null;
		PreparedStatement ps = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = SQLUtils.getConnection();
			ps = con.prepareStatement("insert into t_user value(null,?,?,?,?,?,?)");
			ps.setString(1, name);
			ps.setString(2, "123456");
			ps.setString(3, "student");
			ps.setString(4, sclass);
			ps.setString(5, no);
			ps.setString(6, sex);
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("数据库访问出现异常:" + e);
		} finally {

			if (ps != null)
				ps.close();
			if (con != null)
				con.close();
		}
	}

	public void deleteByID(int id) throws Exception{
		Connection con = null;
		PreparedStatement ps = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = SQLUtils.getConnection();
			ps = con.prepareStatement("delete from t_user as u where u.id= ? ");
			ps.setInt(1, id);
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("数据库访问出现异常:" + e);
		} finally {

			if (ps != null)
				ps.close();
			if (con != null)
				con.close();
		}
	}

	public List<User> searchAllUser() throws Exception{
		List<User> users=new ArrayList<User>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = SQLUtils.getConnection();
			ps = con.prepareStatement("select * from t_user");
//			ps.setString(1, "%"+customerName+"%");
			rs=ps.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setRole(rs.getString("role"));
				user.setName(rs.getString("name"));
				user.setPwd(rs.getString("pwd"));
				user.setSclass(rs.getString("sclass"));
				user.setNo(rs.getString("no"));
				user.setSex(rs.getString("sex"));
				if(user.getRole().equals("student"))
					users.add(user);
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
		return users;
	}
	public void modifyPwd(int id,String newPwd) throws Exception{
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = SQLUtils.getConnection();
			ps = con.prepareStatement("UPDATE t_user SET pwd=? WHERE id=?");
			ps.setString(1, newPwd);
			ps.setInt(2, id);
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("数据库访问出现异常:" + e);
		} finally {

			if (ps != null)
				ps.close();
			if (con != null)
				con.close();
		}
	}

	public void modifyInfo(int id,String name,String sclass,String no,String sex) throws Exception{
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = SQLUtils.getConnection();
			ps = con.prepareStatement("UPDATE t_user SET name=?,sclass=?,no=?,sex=? WHERE id=?");
			ps.setString(1, name);
			ps.setString(2, sclass);
			ps.setString(3, no);
			ps.setString(4, sex);
			ps.setInt(5, id);
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("数据库访问出现异常:" + e);
		} finally {

			if (ps != null)
				ps.close();
			if (con != null)
				con.close();
		}
	}


}

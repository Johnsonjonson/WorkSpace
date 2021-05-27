package dao;

import entity.Product;
import entity.User;
import utils.SQLUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DataDAO {

    public List<Product> searchAllGoods() throws Exception{
        List<Product> products=new ArrayList<Product>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = SQLUtils.getConnection();
            ps = con.prepareStatement("select * from t_goods");
//			ps.setString(1, "%"+customerName+"%");
            rs=ps.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setNum(rs.getInt("num"));
                products.add(product);
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
        return products;
    }
}

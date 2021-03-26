package dao;

import entity.InfoData;
import entity.Marker;
import entity.User;
import sun.reflect.generics.tree.Tree;
import utils.SQLUtils;

import java.sql.*;
import java.util.*;

// select distinct level_one from lgis_conditions
//select distinct level_two from lgis_conditions where level_one='大陆'
//select distinct level_three from lgis_conditions where level_two='亚洲'
//select distinct level_four from lgis_conditions where level_three='中华人民共和国'
//select distinct level_five from lgis_conditions where level_four='广东省'

//select distinct name from lgis_conditions where level_three='中华人民共和国'
public class DataDao {

	private HashMap<String,HashMap<String,String>> idList = new HashMap<String,HashMap<String,String>>();
	private HashMap<String,String> imgList = new HashMap<String,String>();
	{
//		"{[type:'机场','level_two'],[],[p]}"
		HashMap map = new HashMap<String,String>();
		map.put("type","机场");
		map.put("level_three","中华人民共和国");
		idList.put("1",map);
		map = new HashMap<String,String>();
		map.put("type","港口");
		map.put("level_three","中华人民共和国");
		idList.put("2",map);
		map = new HashMap<String,String>();
		map.put("type","机场");
		map.put("level_three","美国");
		idList.put("3",map);
		map = new HashMap<String,String>();
		map.put("type","港口");
		map.put("level_three","美国");
		idList.put("4",map);

		map = new HashMap<String,String>();
		map.put("type","机场");
		map.put("level_two","亚洲");
		idList.put("5",map);
		map = new HashMap<String,String>();
		map.put("type","机场");
		map.put("level_two","欧洲");
		idList.put("6",map);
		map = new HashMap<String,String>();
		map.put("type","机场");
		map.put("level_two","美洲");
		idList.put("7",map);
		map = new HashMap<String,String>();
		map.put("type","机场");
		map.put("level_two","大洋洲");
		idList.put("8",map);

		map = new HashMap<String,String>();
		map.put("type","港口");
		map.put("level_two","亚洲");
		idList.put("9",map);
		map = new HashMap<String,String>();
		map.put("type","港口");
		map.put("level_two","欧洲");
		idList.put("10",map);
		map = new HashMap<String,String>();
		map.put("type","港口");
		map.put("level_two","美洲");
		idList.put("11",map);
		map = new HashMap<String,String>();
		map.put("type","港口");
		map.put("level_two","大洋洲");
		idList.put("12",map);
		map = new HashMap<String,String>();
		map.put("type","港口");
		map.put("level_two","非洲");
		idList.put("13",map);

		// 图片配置
		imgList.put("上海港","shanghaigang");
		imgList.put("不来梅港","bulaimeigang");
		imgList.put("东京港","dongjinggang");
		imgList.put("仁川港","renchuangang");
		imgList.put("北京首都机场","beijingshoudujichang");
		imgList.put("吉达港","jidagang");
		imgList.put("宁波-舟山港","ningbo-zhoushangang");
		imgList.put("安特卫普港","anteweipugang");
		imgList.put("广州港","guangzhougang");
		imgList.put("新加坡港","xinjiapogang");
		imgList.put("林查班港","linchabangang");
		imgList.put("桑托斯港","santuosigang");
		imgList.put("汉堡港","hanbaogang");
		imgList.put("洛杉矶港","luoshanjigang");
		imgList.put("深圳港","shenzhengang");
		imgList.put("科伦坡港","kelunpogang");
		imgList.put("纽约港","niuyuegang");
		imgList.put("营口港","yingkougang");
		imgList.put("迪拜港","dibaigang");
		imgList.put("釜山港","fushangang");
		imgList.put("雅加达港","yajiadagang");
		imgList.put("青岛港","qingdaogang");
		imgList.put("高雄港","gaoxionggang");
		imgList.put("鹿特丹港","lutedangang");

	}
	public InfoData getData() throws Exception {
		InfoData infoData = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = SQLUtils.getConnection();
			ps = con.prepareStatement("select distinct name from lgis_conditions");
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

	// level_three 中华人民共和国 机场
	public InfoData getDataByLevelAndType(String level,String levelName,String type) throws Exception {
		InfoData infoData = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = SQLUtils.getConnection();
//			ps = con.prepareStatement("UPDATE t_user SET pwd=? WHERE id=? ");
//			ps.setString(1, newPwd);
//			ps.setInt(1, id);
			ps = con.prepareStatement("select distinct name from lgis_conditions where ?=? and type=?");
			ps.setString(1, level);
			ps.setString(2, levelName);
			ps.setString(3, type);
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

	// level_three 中华人民共和国 机场
	public ArrayList<Marker> getDataById(String id) throws Exception {
		InfoData infoData = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		System.out.println(idList.toString());
		System.out.println(id);
		HashMap<String, String> stringStringHashMap = idList.get(id);
		HashMap<String, Marker> allMarker = getAllMarker();
		ArrayList<Marker> searchResult = new ArrayList<>();
		Set<String> keySet = stringStringHashMap.keySet();
		try {
			con = SQLUtils.getConnection();
			int i = 0;
			String[] keyArr = new String[3];
			String[] valueArr = new String[3];
			for (String key : keySet) {
				String value = stringStringHashMap.get(key);
				keyArr[i] = key;
				valueArr[i] = value;
				System.out.println("key = "+key +"  value = "+value);
				i++;
			}
			String sql = "select distinct name from lgis_conditions where "+keyArr[0]+"=?"+" and "+keyArr[1]+"=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, valueArr[0]);
			ps.setString(2, valueArr[1]);

			rs=ps.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
				Marker marker = allMarker.get(name);
				searchResult.add(marker);
//				System.out.println("查询数据 "+name.toString());
			}
//			System.out.println("查询数据 结果 "+searchResult.toString());
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
		return searchResult;
	}

	public HashMap<String,Marker>getAllMarker() throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		HashMap<String,Marker> markers = new HashMap<>();
		try {
			con = SQLUtils.getConnection();
			ps = con.prepareStatement("select * from lgis_marker");
			rs=ps.executeQuery();
			while (rs.next()) {
				Marker marker = new Marker();
				marker.setId(rs.getInt("id"));
				marker.setName(rs.getString("name"));
				marker.setLng(rs.getDouble("lng"));
				marker.setLat(rs.getDouble("lat"));
				marker.setTitle(rs.getString("title"));
				marker.setHtmlData(rs.getString("html_data"));
				marker.setTransferTime(rs.getDouble("transfer_time"));
				marker.setTransferFeeRate(rs.getDouble("transfer_fee_rate"));
				marker.setDrawingType(rs.getString("drawing_type"));
				marker.setFlag(rs.getInt("flag"));
				marker.setSubType(rs.getString("subtype"));
				marker.setType(rs.getString("type"));
				String imgName = imgList.get(rs.getString("name"));
				if (imgName != null){
					marker.setImgName(imgName);
				}
				markers.put(rs.getString("name"),marker);
//				System.out.println("这里！！！！！！！！！！"+marker.toString());
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
		return markers;
	}

    public Marker getDataByName(String name) throws Exception {
		HashMap<String, Marker> allMarker = getAllMarker();
		Marker marker = allMarker.get(name);
		return marker;
    }
}

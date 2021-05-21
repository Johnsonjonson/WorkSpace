package servlet;

import com.google.gson.Gson;
import dao.DataDao;
import dao.UserDAO;
import entity.InfoData;
import entity.Marker;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet("/DataServlet")
public class DataServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String m = request.getParameter("m");
		if("querybyid".equals(m)){
			queryById(request, response);
		}else if("querybyname".equals(m)) {
			queryByName(request, response);
		}else{
			getData(request,response);
		}
	}

	private void queryById(HttpServletRequest request, HttpServletResponse response) {
		DataDao dataDao = new DataDao();
		try {
			String index = request.getParameter("index");
			ArrayList<Marker> markers = dataDao.getDataById(index);
			Gson gson2 = new Gson();
			String json = gson2.toJson(markers);
			System.out.println(json);
			// 将json字符串数据返回给前端
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void queryByName(HttpServletRequest request, HttpServletResponse response) {
		DataDao dataDao = new DataDao();
		try {
			String name = request.getParameter("name");
			name = URLDecoder.decode(name, "utf-8");
			Marker marker = dataDao.getDataByName(name);
			Gson gson2 = new Gson();
			String json = gson2.toJson(marker);
			System.out.println(json);
			// 将json字符串数据返回给前端
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			DataDao dataDao = new DataDao();
			HashMap<String,Marker> data = dataDao.getAllMarker();

			Gson gson2 = new Gson();
			String json = gson2.toJson(data);
			System.out.println(json);
			// 将json字符串数据返回给前端
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().write(json);
//			if (data==null) {
//				request.setAttribute("msg", "没有找到相关信息");
//				request.setAttribute("data", data);
//				request.getRequestDispatcher("/detailInfo.jsp").forward(
//						request, response);
//			}else{
//				request.setAttribute("data", data);
//				request.getRequestDispatcher("/detailInfo.jsp").forward(
//						request, response);
//			}
		} catch (Exception e) {

			response.setContentType("text/html; charset=utf-8");
			response.getWriter().write("{}");
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
}

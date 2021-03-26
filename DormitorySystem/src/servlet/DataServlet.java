package servlet;

import com.google.gson.Gson;
import dao.DataDao;
import dao.UserDAO;
import entity.InfoData;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/DataServlet")
public class DataServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		getData(request,response);
	}

	private void getData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			DataDao dataDao = new DataDao();
			InfoData data = dataDao.getData();

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

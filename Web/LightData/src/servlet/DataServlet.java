package servlet;

import com.google.gson.Gson;
import dao.DataDao;
import entity.InfoData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
			// 将json字符串private void getData(HttpServletRequest request, HttpServletResponse response) throws IOException {
			//		try {
			//			DataDao dataDao = new DataDao();
			//			InfoData data = dataDao.getData();
			//
			//			Gson gson2 = new Gson();
			//			String json = gson2.toJson(data);
			//			System.out.println(json);
			//			// 将json字符串数据返回给前端
			//			response.setContentType("text/html; charset=utf-8");
			//			response.getWriter().write(json);
			////			}
			//		} catch (Exception e) {
			//
			//			response.setContentType("text/html; charset=utf-8");
			//			response.getWriter().write("{}");
			//		}
			//	}数据返回给前端
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().write(json);
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

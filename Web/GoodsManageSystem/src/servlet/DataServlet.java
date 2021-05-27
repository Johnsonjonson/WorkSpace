package servlet;

import com.google.gson.Gson;
import dao.DataDAO;
import dao.UserDAO;
import entity.Product;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/UserServlet")
public class DataServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String m = request.getParameter("m");
		if("update".equals(m)){
			delete(request, response);
		}else{
			search(request,response);
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

	private void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			int ownerId=Integer.valueOf(request.getParameter("cid"));
			UserDAO userDAO = new UserDAO();
			userDAO.deleteByID(ownerId);
			request.setAttribute("msg", "删除用户成功");
			request.setAttribute("m","search");
			search(request, response);
		} catch (Exception e) {
			request.setAttribute("msg", e.getMessage());
		}
	}

	private void search(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			DataDAO dataDAO = new DataDAO();
			List<Product> products = dataDAO.searchAllGoods();
			Gson gson2 = new Gson();
			String json = gson2.toJson(products);
			System.out.println(json);
			// 将json字符串数据返回给前端
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().write(json);
			
		} catch (Exception e) {
//			request.setAttribute("users", users);
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().write("{}");
		}

	}
}

package servlet;

import dao.UserDAO;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String m = request.getParameter("m");
		if("delete".equals(m)){
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
//			request.getRequestDispatcher("/customersearch.jsp").forward(
//					request, response);
		} catch (Exception e) {
			request.setAttribute("msg", e.getMessage());
//			request.getRequestDispatcher("/warehousing.jsp").forward(request,
//					response);
		}
	}

	private void search(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			UserDAO userDAO = new UserDAO();
			List<User> users = userDAO.searchAllUser();
			if (users.size() == 0) {
				request.setAttribute("msg", "没有找到相关用户信息");
				request.setAttribute("users", users);
				request.getRequestDispatcher("/userSearchResult.jsp").forward(
						request, response);
			}else{
				request.setAttribute("users", users);
				request.getRequestDispatcher("/userSearchResult.jsp").forward(
						request, response);
			}
			
		} catch (Exception e) {
//			request.setAttribute("users", users);
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("/userSearchResult.jsp").forward(
					request, response);
		}

	}
}

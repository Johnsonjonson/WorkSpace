package servlet;

import com.google.gson.Gson;
import dao.UserDAO;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;


/**
 * Servlet implementation class ModifyServlet
 */
@WebServlet("/ModifyServlet")
public class ModifyServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String errorno = "0";
		String modifymsg = null;
		User user = null;
		HashMap<String,Object> set = new HashMap<>();
		UserDAO userDAO = new UserDAO();
		try {
			int id = Integer.valueOf(request.getParameter("id"));
			String pwd = request.getParameter("pwd");
			String newpwd = request.getParameter("newpwd");
			user = userDAO.getById(id);
			if (user == null) {
				errorno = "0";
				modifymsg = "用户名不存在";
			} else if (!user.getPwd().equals(pwd)) {
				errorno = "0";
				modifymsg = "旧密码输入错误";
			} else{
				userDAO.modifyPwd(id,newpwd);

				request.getSession(true).setAttribute("user", user);
				errorno = "1";
				modifymsg = "修改成功";
			}
		} catch (Exception e) {
			errorno = "0";
			modifymsg = e.toString();
		}
		set.put("modifymsg",modifymsg);
		set.put("errorcode",errorno);
		Gson gson2 = new Gson();
		String json = gson2.toJson(set);
		System.out.println(json);
		// 将json字符串数据返回给前端
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().write(json);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}

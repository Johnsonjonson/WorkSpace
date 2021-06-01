package servlet;

import com.google.gson.Gson;
import dao.DataDAO;
import dao.UserDAO;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;


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
		DataDAO dataDAO = new DataDAO();
		try {
//			int id = Integer.valueOf(request.getParameter("id"));
//			String name = request.getParameter("name");
//			name = URLDecoder.decode(name, "utf-8");
//			String sclass = request.getParameter("sclass");
//			sclass = URLDecoder.decode(sclass, "utf-8");
//			String no = request.getParameter("no");
//			no = URLDecoder.decode(no, "utf-8");
//			String sex = request.getParameter("sex");
//			sex = URLDecoder.decode(sex, "utf-8");
			int id = Integer.valueOf(request.getParameter("id"));
			System.out.println("id = "+id);
			String name = request.getParameter("name");
			name = URLDecoder.decode(name, "utf-8");
			System.out.println("id = "+id+" name = "+name);
			double price = Double.valueOf(request.getParameter("price"));
			System.out.println("id = "+id+" name = "+name+" "+ " price = " +price);
			int num = Integer.valueOf(request.getParameter("num"));
			System.out.println("id = "+id+" name = "+name+" "+ " price = " +price+" num = "+num);
			dataDAO.modifyInfo(id,name,price,num);
			errorno = "1";
			modifymsg = "修改成功";
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

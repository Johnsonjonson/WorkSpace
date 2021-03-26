package servlet;

import com.google.gson.*;
import dao.UserDAO;
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

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String m = request.getParameter("m");
		if("delete".equals(m)) {
			delete(request, response);
		}else if("querybyid".equals(m)){
			queryById(request,response);
		}else if ("modify_info".equals(m)){
			modifyInfo(request,response);
		}else if ("insert_student".equals(m)){
			insertStudent(request,response);
		}else{
			search(request,response);
		}
	}

	private void insertStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HashMap<String,Object> set = new HashMap<>();
		String errorno = "0";
		String modifymsg = null;
		try {
			String students = request.getParameter("students");
			students = URLDecoder.decode(students, "utf-8");
//		Gson gson = new Gson();
//		// 将json 转化成 java 对象
//		Student stu = gson.fromJson(json, Student.class);
			System.out.println("=========="+students);
			JsonParser parser = new JsonParser();
			JsonElement root = parser.parse(students);
			JsonArray asJsonArray = root.getAsJsonArray();

			int size = asJsonArray.size();
			UserDAO dao = new UserDAO();
			for(int i = 0; i < size; i++) {
				//获得每一个json 元素
				JsonElement e = asJsonArray.get(i);


				JsonObject asJsonObject = e.getAsJsonObject();
				JsonPrimitive nameJson = asJsonObject.getAsJsonPrimitive("姓名");
				String name = nameJson.getAsString();
				JsonPrimitive noJson = asJsonObject.getAsJsonPrimitive("学号");
				String no = noJson.getAsString();
				JsonPrimitive clsJson = asJsonObject.getAsJsonPrimitive("班级");
				String cls = clsJson.getAsString();
				JsonPrimitive sexJson = asJsonObject.getAsJsonPrimitive("性别");
				String sex = sexJson.getAsString();

				dao.add(name,cls,no,sex);
				errorno = "1";
				modifymsg = "修改成功";
				// 通过元素 得到需要的节点值 TODO:
			}
		}catch (Exception e){
			errorno = "0";
			modifymsg = e.toString();
			e.printStackTrace();
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

	private void modifyInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HashMap<String,Object> set = new HashMap<>();
		String errorno = "0";
		String modifymsg = null;
		try {
			UserDAO userDAO = new UserDAO();
			int id = Integer.valueOf(request.getParameter("id"));
			String name = request.getParameter("name");
			name = URLDecoder.decode(name, "utf-8");
			String sclass = request.getParameter("sclass");
			sclass = URLDecoder.decode(sclass, "utf-8");
			String no = request.getParameter("no");
			no = URLDecoder.decode(no, "utf-8");
			String sex = request.getParameter("sex");
			sex = URLDecoder.decode(sex, "utf-8");
			userDAO.modifyInfo(id,name,sclass,no,sex);
			errorno = "1";
			modifymsg = "修改成功";
		} catch (Exception e) {
			errorno = "0";
			modifymsg = e.toString();
			e.printStackTrace();
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
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

	private void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HashMap<String,Object> set = new HashMap<>();
		String errorno = "0";
		String modifymsg = null;
		try {
			int ownerId=Integer.valueOf(request.getParameter("cid"));
			UserDAO userDAO = new UserDAO();
			userDAO.deleteByID(ownerId);
//			request.setAttribute("msg", "删除用户成功");
//			request.setAttribute("m","search");
//			response.setHeader("refresh", "3");
//			search(request, response);
//			request.getRequestDispatcher("/customersearch.jsp").forward(
//					request, response);
			errorno = "1";
			modifymsg = "删除用户成功";
		} catch (Exception e) {
			errorno = "1";
			modifymsg = "删除用户成功";
			request.setAttribute("msg", e.getMessage());
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

	private void queryById(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			UserDAO userDAO = new UserDAO();
			int id = Integer.valueOf(request.getParameter("id"));
			User user = userDAO.getById(id);
			Gson gson2 = new Gson();
			String json = gson2.toJson(user);
			System.out.println(json);
			// 将json字符串数据返回给前端
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}

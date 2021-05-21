package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;
import entity.User;
import utils.DateUtil;


/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String url = null;
		String msg = null;
		User user = null;
		UserDAO userDAO = new UserDAO();
		try {
			user = userDAO.getByName(request.getParameter("name"));
			System.out.println("   ==== +"+ user.getIsSwipe());
			if (user == null) {
				url = "/index.jsp";
				msg = "用户名不存在";
			} else if (!user.getPwd().equals(request.getParameter("pwd"))) {
				url = "/index.jsp";
				msg = "密码错误";
			}else if (user.getIsSwipe()!=1||(user.getIsSwipe()==1 && "None".equals(user.getSwipeTime()))) {  //未刷卡  刷卡了但是刷卡时间是空
				url = "/index.jsp";
				msg = "未刷卡";
			}else {  //已经刷卡
				String swipeTimeStamp = DateUtil.date2TimeStamp(user.getSwipeTime(), "yyyy-MM-dd HH:mm:ss");
				String curTimeStamp = DateUtil.timeStamp();
				long swipeTime = Long.valueOf(swipeTimeStamp);
				long curTime = Long.valueOf(curTimeStamp);
				long diffTime = curTime - swipeTime;
				if (diffTime > 3600){
					msg = "刷卡过期,请重刷";
					url = "/index.jsp";
				}else{
					request.getSession(true).setAttribute("user", user);
					System.out.println("登陆玩家角色  " + user.getRole());
					if (user.getRole().equals("dormitory")) {
						url = "/detailInfo.jsp";
//						request.setAttribute("m", "search");
//						UserServlet uerServlet = new UserServlet();
//						uerServlet.doGet(request, response);
					} else if (user.getRole().equals("student")) {
						url = "/detailInfo.jsp";
					}
					msg = "登录成功";
				}

			}
		} catch (Exception e) {
			url = "/index.jsp";
			msg = e.toString();
		}
		if (user != null) {
			user.setId(1);
		}
		request.setAttribute("msg", msg);
		request.setAttribute("user", user);
		request.getRequestDispatcher(url).forward(request, response);
	}

}

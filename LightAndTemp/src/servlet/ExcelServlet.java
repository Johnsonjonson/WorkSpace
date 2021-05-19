package servlet;

import com.google.gson.Gson;
import dao.DataDao;
import entity.InfoData;
import utils.ExcelHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@WebServlet("/ExcelServlet")
public class ExcelServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
//		getAllData(request,response);
		DataDao sku = new DataDao();
		List<InfoData> skuList = null;
		try {
			skuList = sku.getAllData();
			ExcelHelper.createExcel(skuList); //查询的结果，便于插入Excel填充数据
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		//用户下载的保存路径
		if (request.getParameter("file") != null) {
			OutputStream os = null;
			FileInputStream fis = null;
			try {
				String file = request.getParameter("file");
				if (!(new File(file)).exists()) {
					System.out.println("没有文件");
					return;
				}
				System.out.println("这个文件名为："+file);
				os = response.getOutputStream();
				response.setHeader("content-disposition", "attachment;filename=" + file);
				response.setContentType("application/vnd.ms-excel");//此项内容随文件类型而异
				byte temp[] = new byte[1000];
				fis = new FileInputStream(file);
				int n = 0;
				while ((n = fis.read(temp)) != -1) {
					os.write(temp, 0, n);
				}
			} catch (Exception e) {
//				out.print("出错");
			} finally {
				if (os != null)
					os.close();
				if (fis != null)
					fis.close();
			}
//			out.clear();
//			out = pageContext.pushBody();

		}
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

	private void getAllData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			DataDao dataDao = new DataDao();
			List<InfoData> data = dataDao.getAllData();

			ExcelHelper.createExcel(data);
//			Gson gson2 = new Gson();
//			String json = gson2.toJson(data);
//			System.out.println(json);
//			// 将json字符串数据返回给前端
//			response.setContentType("text/html; charset=utf-8");
//			response.getWriter().write(json);
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

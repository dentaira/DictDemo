package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.MyWord;
import model.MyWordDAO;
import model.User;

/**
 * Servlet implementation class Favorite
 */
@WebServlet("/favorite")
public class Favorite extends HttpServlet {
	private static final long serialVersionUID = 1L;
	MyWordDAO myDao;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Favorite() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		myDao = new MyWordDAO();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		ArrayList<MyWord> favList = null;
		String msg = null;

		// 検索ワードを取得
		String title = request.getParameter("title");

		//TODO ごちゃごちゃしている。例外処理を何とかしたい
		//検索ワードなし
		if (title == null) {
			try {
				favList = myDao.findAll(user);
			} catch (NullPointerException e) {
				msg = "<p>ログインしてください</p>";
			}

		//検索ワードあり
		} else {
			String matchMode = "pm";
			if (request.getParameter("match") != null) {
				matchMode = request.getParameter("match");
			}
			// sqlのLIKE以降の文字列作成
			String match = "";
			switch (matchMode) {
			case "pfm":
				match = title;
				break;
			case "lm":
				match = title + "%";
				break;
			case "rm":
				match = "%" + title;
				break;
			case "pm":
				match = "%" + title + "%";
				break;
			}

			try {
				favList = myDao.findAll(user);
			} catch (NullPointerException e) {
				msg = "<p>ログインしてください</p>";
			}

			if (title != null) {
				// タブを生成して詰める
				int pageNo = 1;
				if (request.getParameter("page") != null) {
					pageNo = Integer.parseInt(request.getParameter("page"));
				}
				request.setAttribute("title", title);
			}
		}

		request.setAttribute("msg", msg);
		request.setAttribute("favList", favList);
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/view/favorite.jsp");
		rd.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}

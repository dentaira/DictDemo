package controller;

import java.io.IOException;

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
import model.Word;
import model.WordDAO;

/**
 * Servlet implementation class Register
 */
@WebServlet("/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	WordDAO dao;
	MyWordDAO myDao;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Register() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		dao = new WordDAO();
		myDao = new MyWordDAO();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Word word = dao.findOne(request.getParameter("regId"));
		request.setAttribute("word", word);
		RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/view/register.jsp");
		rd.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		//お気に入りをDBに登録
		request.setCharacterEncoding("utf-8");
		String title = (String) request.getParameter("title");
		String body = (String) request.getParameter("body");
		String id = (String) request.getParameter("id");
		MyWord myWord = new MyWord(id, user, title, body);
		myDao.insertOne(myWord);

		//元の検索結果に戻る
		String retUrl = session.getAttribute("retUrl").toString();
		session.removeAttribute("retUrl");
		response.sendRedirect(retUrl);
	}

}

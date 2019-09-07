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

/**
 * Servlet implementation class Edit
 */
@WebServlet("/Edit")
public class Edit extends HttpServlet {
	private static final long serialVersionUID = 1L;
	MyWordDAO myDao;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Edit() {
        super();
    }

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		myDao = new MyWordDAO();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String id = request.getParameter("id");
		MyWord myWord = myDao.findOne(user.getEmail(), id);
		request.setAttribute("myWord", myWord);
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/view/edit.jsp");
		rd.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		MyWord myWord = new MyWord();
		if(request.getParameter("edit") != null) {
			myWord.setId(request.getParameter("id"));
			myWord.setBody(request.getParameter("body"));
			myWord.setUser(user);
			myDao.updateOne(myWord);
		} else if(request.getParameter("delete") != null) {
			myWord.setId(request.getParameter("id"));
			myWord.setUser(user);
			myDao.deleteOne(myWord);
		}
		response.sendRedirect("/DictDemo/favorite");
	}

}

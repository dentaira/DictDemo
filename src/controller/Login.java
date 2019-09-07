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

import model.User;
import model.UserDAO;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserDAO userDao;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		userDao = new UserDAO();
	}



	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/view/login.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String email = request.getParameter("email");
		String password = request.getParameter("password");
		User user = userDao.findOne(email, password);
		if(user != null) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			RequestDispatcher rd = request.getRequestDispatcher("/home");
			rd.forward(request, response);
		} else {
			request.setAttribute("msg", "ユーザーが見つかりません");
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/view/login.jsp");
			rd.forward(request, response);

		}

	}

}

package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import model.UserDAO;

/**
 * Servlet implementation class SignUp
 */
@WebServlet("/SignUp")
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserDAO userDao;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUp() {
        super();
        // TODO Auto-generated constructor stub
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
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/view/sign_up.jsp");
		rd.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user;
		String email = (String) request.getParameter("email");
		String password = (String) request.getParameter("password");
		user = new User();
		user.setEmail((String) request.getParameter("email"));
		user.setPassword((String) request.getParameter("password"));
//		try {
			userDao.insertOne(user);

			// TODO mysql-connector-java-5
//		} catch (MySQLIntegrityConstraintViolationException e) {
//			request.setAttribute("msg", "そのアドレスは既に登録されています");
//			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/view/sign_up.jsp");
//			rd.forward(request, response);
//		}
		RequestDispatcher rd = request.getRequestDispatcher("/home");
		rd.forward(request, response);
	}

}

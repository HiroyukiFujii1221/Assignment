package board.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.beans.User;
import board.service.UserService;

@WebServlet(urlPatterns = { "/user-information" })
public class UserInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		List<User> usersInformation = new UserService().getUsersInformation();

		request.setAttribute("usersInformation", usersInformation);
		request.getRequestDispatcher("/user-information.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
		HttpServletResponse response) throws IOException, ServletException {

		User user = new User();
		user.setId(Integer.parseInt(request.getParameter("user_id")));
		user.setIs_Working(Integer.parseInt(request.getParameter("is_Working")));
		new UserService().updateIs_Working(user);

		response.sendRedirect("./user-information");
	}

}

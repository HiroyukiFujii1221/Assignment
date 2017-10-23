package board.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.beans.UserComment;
import board.beans.UserMessage;
import board.service.PostService;

@WebServlet(urlPatterns = { "/ajax" })
public class UserMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

	String user_id = request.getParameter("user_id");

	List<UserMessage> messages = null;
	List<UserComment> comments = null;
	List<UserMessage> userMessages = new PostService().getUserMessages(user_id);

	System.out.println(userMessages);
	System.out.println("ユーザーIDは"+ request.getParameter("user_id"));

	request.setAttribute("userMessages", userMessages);
	request.setAttribute("messages", messages);
	request.setAttribute("comments", comments);
	request.getRequestDispatcher("/ajax.jsp").forward(request, response);
	}
}
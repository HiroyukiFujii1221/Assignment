package board.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.beans.UserMessage;
import board.service.PostService;

@WebServlet(urlPatterns = { "/deletePost" })
public class DeletePostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request,
		HttpServletResponse response) throws IOException, ServletException {

		new PostService().deleteMessage(request.getParameter("id"));

		List<UserMessage> messages = new PostService().getMessage();
		request.setAttribute("messages", messages);

		request.getRequestDispatcher("/top.jsp").forward(request, response);
	}
}
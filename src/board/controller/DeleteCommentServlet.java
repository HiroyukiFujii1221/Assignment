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
import board.service.CommentService;
import board.service.PostService;

@WebServlet(urlPatterns = { "/deleteComment" })
public class DeleteCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request,
		HttpServletResponse response) throws IOException, ServletException {

		new CommentService().deleteComment(request.getParameter("id"));
		new CommentService().deleteComment(request.getParameter("post_id"));

		List<UserMessage> messages = new PostService().getMessage();
		request.setAttribute("messages", messages);
		List<UserComment> comments = new CommentService().getComment();
		request.setAttribute("comments", comments);

		response.sendRedirect("./");
	}

}
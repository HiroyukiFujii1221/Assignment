package board.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import board.beans.Comment;
import board.beans.User;
import board.beans.UserComment;
import board.service.CommentService;

@WebServlet(urlPatterns = { "/newComment" })

public class NewCommentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		List<UserComment> comments = new CommentService().getComment();

		request.setAttribute("comments", comments);
		response.sendRedirect("./");
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();

		List<String> messages = new ArrayList<String>();

		User user = (User) session.getAttribute("loginUser");

		Comment comment = new Comment();
		comment.setText(request.getParameter("comment"));
		comment.setPost_id(Integer.parseInt(request.getParameter("post_id")));
		comment.setBranch_id(user.getBranch_id());
		comment.setDepartment_id(user.getDepartment_id());
		comment.setUser_id(user.getId());

		System.out.println(user.getBranch_id());

		if (isValid(request, messages)) {
			new CommentService().register(comment);
			response.sendRedirect("./");
		} else {
			session.setAttribute("errorMessages",messages);
			request.setAttribute("comment", comment);
			response.sendRedirect("./");
		}
	}

	private boolean isValid(HttpServletRequest request, List<String> messages) {

		String comment = request.getParameter("comment");

		if (StringUtils.isBlank(comment)) {
			messages.add("コメントを入力してください");
		}

		if (500 < comment.length()) {
			messages.add("コメントは500文字以下で入力してください");
		}
		if (messages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}
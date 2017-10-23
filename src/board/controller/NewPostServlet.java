

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

import board.beans.Message;
import board.beans.User;
import board.beans.UserMessage;
import board.service.PostService;

@WebServlet(urlPatterns = { "/newPost" })
public class NewPostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		List<UserMessage> messages = new PostService().getMessage();

		request.setAttribute("messages", messages);
		request.getRequestDispatcher("/newpost.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("loginUser");

		List<String> messages = new ArrayList<String>();

			Message message = new Message();

			message.setTitle(request.getParameter("title"));
			message.setCategory(request.getParameter("category"));
			message.setText(request.getParameter("text"));
			message.setBranch_id(user.getBranch_id());
			message.setDepartment_id(user.getDepartment_id());
			message.setUser_id(user.getId());

		if (isValid(request, messages)) {
			new PostService().register(message);

			response.sendRedirect("./");
		} else {
			request.setAttribute("errorMessages", messages);
			request.setAttribute("message", message);
			request.getRequestDispatcher("/newpost.jsp").forward(request, response);
		}
	}

	private boolean isValid(HttpServletRequest request, List<String> messages) {

		String title = request.getParameter("title");
		String category = request.getParameter("category");
		String text = request.getParameter("text");

		if (StringUtils.isBlank(title)) {
			messages.add("件名を入力してください");
		}
		if(30 < title.length()){
			messages.add("件名は30文字以下で入力してください");
		}
		if (StringUtils.isBlank(category)) {
			messages.add("カテゴリーを入力してください");
		}
		if(10 < category.length()){
			messages.add("カテゴリーは10文字以下で入力してください");
		}
		if (StringUtils.isBlank(text)) {
			messages.add("本文を入力してください");
		}
		if (1000 < text.length()) {
			messages.add("本文は1000文字以下で入力してください");
		}
		if (messages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

}

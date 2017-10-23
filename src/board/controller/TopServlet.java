package board.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import board.beans.UserComment;
import board.beans.UserMessage;
import board.service.CommentService;
import board.service.PostService;

@WebServlet(urlPatterns = { "/index.jsp" })
public class TopServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		String startDate = "2001/1/1";
		String endDate = "2100/1/1";
		String category = request.getParameter("category");
		String noMessages = null;

		if(!StringUtils.isEmpty(request.getParameter("startDate"))) {
			startDate = request.getParameter("startDate");
		}

		if(!StringUtils.isEmpty(request.getParameter("endDate"))){
			endDate = request.getParameter("endDate");
		}

		List<UserMessage> messages = new PostService().getNarrowedMessage(startDate, endDate, category);
		List<UserComment> comments = new CommentService().getComment();
		List<String> categoryNames = new ArrayList<String>();

		for(UserMessage message : messages){
			boolean boo = true;
			for(String str : categoryNames){
				if(message.getCategory().equals(str)){
					boo = false;
				}
			}
			if(boo){
				categoryNames.add(message.getCategory());
			}
		}

		if(messages.size() == 0) {
			noMessages = "指定された条件では投稿が見つかりませんでした。";
		}
		request.setAttribute("noMessages", noMessages);
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("categoryNames", categoryNames);
		request.setAttribute("messages", messages);
		request.setAttribute("comments", comments);
		request.getRequestDispatcher("/top.jsp").forward(request, response);
	}
}
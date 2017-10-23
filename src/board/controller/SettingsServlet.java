package board.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import board.beans.User;
import board.exception.NoRowsUpdatedRuntimeException;
import board.service.UserService;

@WebServlet(urlPatterns = { "/settings" })
@MultipartConfig(maxFileSize = 100000)
public class SettingsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		List<String> messages = new ArrayList<String>();

		if(StringUtils.isBlank(request.getParameter("id"))) {
			messages.add("ユーザーのIDが不正です");
			session.setAttribute("errorMessages", messages);
			response.sendRedirect("user-information");

		} else if(!request.getParameter("id").matches("^[0-9]+$")){
			messages.add("ユーザーのIDが不正です");
			session.setAttribute("errorMessages", messages);
			response.sendRedirect("user-information");

		} else if(new UserService().getUser(Integer.parseInt(request.getParameter("id"))) == null) {
			messages.add("ユーザーのIDが存在しません");
			session.setAttribute("errorMessages", messages);
			response.sendRedirect("user-information");

		} else {
			User editUser = new UserService().getUser(Integer.parseInt(request.getParameter("id")));

			List<User> branchNames = new UserService().getUserBranches();
			List<User> departmentNames = new UserService().getUserDepartments();

			request.setAttribute("editUser", editUser);
			request.setAttribute("branchNames", branchNames);
			request.setAttribute("departmentNames", departmentNames);
			request.getRequestDispatcher("settings.jsp").forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		User editUser = getEditUser(request);
		request.setAttribute("editUser", editUser);

		List<String> messages = new ArrayList<String>();

		if (isValid(request, messages)) {
			try {
				new UserService().update(editUser);

			} catch (NoRowsUpdatedRuntimeException e) {
				request.removeAttribute("editUser");
				messages.add("データを更新できませんでした");
				request.setAttribute("errorMessages", messages);
				request.getRequestDispatcher("settings.jsp").forward(request, response);
			}

			request.setAttribute("editUser", editUser );
			request.removeAttribute("editUser");
			response.sendRedirect("./user-information");
		} else {
			List<User> branchNames = new UserService().getUserBranches();
			List<User> departmentNames = new UserService().getUserDepartments();

			request.setAttribute("errorMessages", messages);
			request.setAttribute("branchNames", branchNames);
			request.setAttribute("departmentNames", departmentNames);
			request.getRequestDispatcher("settings.jsp").forward(request, response);
		}
	}

	private User getEditUser(HttpServletRequest request)
		throws IOException, ServletException {

		User user = new User();

		user.setName(request.getParameter("name"));
		user.setLogin_id(request.getParameter("login_id"));
		user.setPassword(request.getParameter("newPassword1"));
		user.setBranch_id(Integer.parseInt(request.getParameter("branch_id")));
		user.setDepartment_id(Integer.parseInt(request.getParameter("department_id")));
		user.setId(Integer.parseInt(request.getParameter("id")));
		return user;
	}

	private boolean isValid(HttpServletRequest request, List<String> messages) {

		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String login_id = request.getParameter("login_id");
		int branch_id = Integer.parseInt(request.getParameter("branch_id"));
		int department_id = Integer.parseInt(request.getParameter("department_id"));

		User userLoginId = new UserService().getUserIDs(login_id);

		if (StringUtils.isEmpty(name)) {
			messages.add("名前を入力してください。");
		}
		if (StringUtils.isEmpty(login_id)) {
			messages.add("ログインIDを入力してください。");
		} else if(!request.getParameter("login_id").matches("^[a-zA-Z0-9]{6,20}$")) {
			messages.add("ログインIDは半角英数字6文字以上20文字以内で入力してください。");
		}
		if(userLoginId != null) {
			if(userLoginId.getId() != id) {
			messages.add("そのログインIDはすでに使用されています");
			}
		}
		if(request.getParameter("password1") != null) {
			if(!request.getParameter("newPassword1").matches("^[a-zA-Z0-9 -/:@`{-~]{6,20}$")) {
				messages.add("パスワードは半角文字で入力してください");
			}
		}
		if(!(request.getParameter("newPassword1").equals(request.getParameter("newPassword2")))){
			messages.add("パスワードが一致しません。入力を確認の上、再度入力してください。");
		}
		if((branch_id == 1 && department_id >= 3) || (branch_id > 2 && department_id <= 3)) {
			messages.add("支店と部署・役職の組み合わせが不正です");
		}
		if (messages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

}

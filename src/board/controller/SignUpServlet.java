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

import board.beans.User;
import board.service.UserService;

@WebServlet(urlPatterns = { "/signup" })
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID
	= 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		List<User> branchNames = new UserService().getUserBranches();
		List<User> departmentNames = new UserService().getUserDepartments();

		request.setAttribute("branchNames", branchNames);
		request.setAttribute("departmentNames", departmentNames);
		request.getRequestDispatcher("signup.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		List<String> errorMessages = new ArrayList<String>();
		User user = new User();
		user.setName(request.getParameter("name"));
		user.setLogin_id(request.getParameter("login_id"));
		user.setPassword(request.getParameter("password1"));
		user.setBranch_id(Integer.parseInt(request.getParameter("branch_id")));
		user.setDepartment_id(Integer.parseInt(request.getParameter("department_id")));

		if (isValid(request, errorMessages)) {
			new UserService().register(user);
			response.sendRedirect("user-information");

		} else {
			List<User> branchNames = new UserService().getUserBranches();
			List<User> departmentNames = new UserService().getUserDepartments();

			request.setAttribute("errorMessages", errorMessages);
			request.setAttribute("user", user);
			request.setAttribute("branchNames", branchNames);
			request.setAttribute("departmentNames", departmentNames);
			request.getRequestDispatcher("signup.jsp").forward(request, response);
		}

	}

	private boolean isValid(HttpServletRequest request, List<String> errorMessages) {
		String name = request.getParameter("name");
		String login_id = request.getParameter("login_id");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		String branchId = request.getParameter("branch_id");
		String departmentId = request.getParameter("department_id");

		User userLoginId = new UserService().getUserIDs(login_id);

		if (StringUtils.isBlank(name)) {
			errorMessages.add("名前を入力してください");
		} else if(name.length() >= 10) {
			errorMessages.add("名前は10文字以内で入力してください");
		}
		if (StringUtils.isBlank(login_id)) {
			errorMessages.add("ログインIDを入力してください");
		} else if(!login_id.matches("^[a-zA-Z0-9]{6,20}$")) {
			errorMessages.add("ログインIDは半角英数字6文字以上20文字以内で入力してください");
		}
		if(userLoginId != null) {
			errorMessages.add("そのログインIDは既に使われています");
		}
		if (StringUtils.isBlank(password1)) {
			errorMessages.add("パスワードを入力してください");
		} else if(!password1.matches("^[a-zA-Z0-9 -/:@`{-~]{6,20}$")) {
			errorMessages.add("パスワードは半角文字6文字以上20文字以内で入力してください");
		}
		if((!StringUtils.isBlank(password1) || !StringUtils.isBlank(password2)) && !(password1.equals(password2))){
			errorMessages.add("パスワードが一致しません。入力を確認の上、再度入力してください。");
		}
		if((Integer.parseInt(branchId) > 1 && Integer.parseInt(departmentId) < 4)
				|| (Integer.parseInt(branchId) == 1 && Integer.parseInt(departmentId) > 4)){
			errorMessages.add("支店と部署・役職の組み合わせが正しくありません。入力を確認の上、再度入力してください。");
		}
		if (errorMessages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

}

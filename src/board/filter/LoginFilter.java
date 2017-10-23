package board.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import board.beans.User;

@WebFilter("/*")
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpSession session = ((HttpServletRequest)request).getSession();
		User user = (User)session.getAttribute("loginUser");

		String servletPath = ((HttpServletRequest) request).getServletPath();

		if(user == null || user.getIs_Working() == 0) {
			if(!(servletPath.equals("/login") || servletPath.matches(".*css.*"))) {

				((HttpServletResponse)response).sendRedirect("login");
				return;
			}
		} else if(user.getDepartment_id() != 1 ) {
			if(servletPath.equals("/user-information") || servletPath.equals("/signup")
					|| servletPath.equals("/settings")){
				((HttpServletResponse) response).sendRedirect("./");
				return;
			}
		}
		chain.doFilter(request, response); // サーブレットを実行
	}

	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void destroy() {
	}

}
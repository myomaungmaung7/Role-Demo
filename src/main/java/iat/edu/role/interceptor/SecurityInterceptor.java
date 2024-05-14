package iat.edu.role.interceptor;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import iat.edu.role.controller.exception.UnauthorizedException;
import iat.edu.role.model.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class SecurityInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle (HttpServletRequest request, HttpServletResponse response,
			Object handler) throws IOException, UnauthorizedException{
				System.out.println("Intercepting" + request.getRequestURI());
				String uri = request.getRequestURI();
				if (uri.startsWith("/css/") || uri.startsWith("/image/")) {
					return true;
				}
				
				if (uri.equalsIgnoreCase("/") || uri.equalsIgnoreCase("/home")
						|| uri.equalsIgnoreCase("/login") || uri.equalsIgnoreCase("/about")) {
					return true;
				}
				
				if (uri.startsWith("/home/")) {
					return true;
				}
				
				HttpSession session = request.getSession();
				UserSession userSession = (UserSession)
				session.getAttribute("userSession");
				
				if(userSession == null) {
					response.sendRedirect("/login");
					return false;
				}
				
				List<String> userRoles = userSession.getUser().getRoles();
				if (uri.startsWith("/admin") && !userRoles.contains("ADMIN")) {
					throw new UnauthorizedException();
				}
				
				if (uri.startsWith("/faculty") && !userRoles.contains("FACULTY")) {
					throw new UnauthorizedException();
				}
				return true;
	}
}

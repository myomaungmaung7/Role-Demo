package iat.edu.role.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import iat.edu.role.model.User;
import iat.edu.role.model.UserSession;
import iat.edu.role.service.UserService;
import jakarta.servlet.http.HttpSession;


@Controller
public class CommonController {
	
	@Autowired
	UserService userService;
	
	@GetMapping({"/", "/login", "/home"})
	public String login(Model model) {
		model.addAttribute("user",new User());
		return "login";
	}
	
	@PostMapping("/home/authenticate")
	public String authenticate (@ModelAttribute("user") @Validated User user, 
			BindingResult bindingResult, Model model, HttpSession session) {
		String retStr = null;
		if (bindingResult.hasErrors()) {
			return "login";
		}
		
		User u = userService.authenticate(user.getUserName(), user.getPassword());
				if (u == null) {
					model.addAttribute("loginMessage", "Incorrect username/password");
					return "login";
				}
				
		UserSession uSession = new UserSession();
		uSession.setUser(u);
		session.setAttribute("userSession", uSession);
		
		List<String> roles = u.getRoles();
		
		if (roles.contains("ADMIN")) {
			session.setAttribute("role", "ADMIN");
			retStr = "redirect:/admin/user/list";
		}
		
		if (roles.contains("FACULTY")) {
			session.setAttribute("role", "FACULTY");
			retStr = "redirect:/staff/profile";
		}
		
		if (roles.contains("STUDENT")) {
			session.setAttribute("role", "STUDENT");
			retStr = "redirect:/student/editProfile";
		}
		
		return retStr;
	}
	
	@GetMapping("/logout")
	public String logout (HttpSession session) {
		session.invalidate();
		return "redirect:/home";
	}
}

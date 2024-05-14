package iat.edu.role.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import iat.edu.role.model.User;
import iat.edu.role.model.UserSession;
import iat.edu.role.service.UserService;
import iat.edu.role.util.FileUploadUtil;

@Controller
@RequestMapping(value = "/admin/user")
@SessionAttributes(value = { "userSession" }, types = { UserSession.class })
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping(value = "/list")
	public String userListPage(Model model, UserSession userSession) {
		List<User> userList = userService.findAllUsers();
		model.addAttribute("userList", userList);
		return "user-list";
	}

	@GetMapping(value = "/create")
	public String showUserForm(Model model, UserSession userSession) {
		User user = new User();
		model.addAttribute("user", user);
		return "user-new";
	}

	@PostMapping(value = "/save")
	 public String saveUser(Model model,@ModelAttribute @Validated User user, @RequestParam("file") MultipartFile file)
	 throws IOException  {
	  String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	  user.setPhoto(fileName);
	  user.setEnabled(true);
	  User savedUser = userService.createUser(user);
	  String uploadDir = "user-photos/" + savedUser.getUserId();
	  FileUploadUtil.saveFile(uploadDir, fileName, file);
	  return "redirect:/admin/user/list" ;

	}
}

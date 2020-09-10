package com.llingwei.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.llingwei.service.UsersService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.llingwei.bean.AdminUser;
import com.llingwei.pojo.Users;
import com.llingwei.utils.IMoocJSONResult;
import com.llingwei.utils.PagedResult;

@Controller
@RequestMapping("users")
public class UsersController extends BasicController{
	
	@Autowired
	private UsersService usersService;

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("sessionUser");
		return "login";
	}
	
	@GetMapping("/showList")
	public String showList() {
		return "users/usersList";
	}


	@PostMapping("login")
	@ResponseBody
	public IMoocJSONResult userLogin(String username, String password,
			HttpServletRequest request, HttpServletResponse response) {

		// TODO 模拟登陆
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return IMoocJSONResult.errorMap("用户名和密码不能为空");
		} else if (username.equals("123") && password.equals("123")) {

			String token = UUID.randomUUID().toString();
			AdminUser user = new AdminUser(username, password, token);
			request.getSession().setAttribute("sessionUser", user);
			return IMoocJSONResult.ok();
		}

		return IMoocJSONResult.errorMsg("登陆失败，请重试...");
	}

	@PostMapping("/usersList")
	@ResponseBody
	public PagedResult list(Users user , Integer page) {

		if (page == null){
			page = 1;
		}

		PagedResult result = usersService.queryUsers(user, page, PAGE_SIZE);
		return result;
	}


	
}

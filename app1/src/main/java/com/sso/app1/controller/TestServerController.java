package com.sso.app1.controller;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


import com.sso.app1.client.until.CookieUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestServerController {


	@RequestMapping("/hello")
	@ResponseBody
	public String hello() {
		return "hello";
	}

	@RequestMapping("/index")
	public String Index(HttpServletRequest request) {
		Cookie token= CookieUtils.getCookie("token");
		if(token!=null){
			request.setAttribute("cookie", token.getValue());
			request.setAttribute("text", "注销");
			request.setAttribute("link", "/logout");
		}else {
			request.setAttribute("text", "登录");
			request.setAttribute("link", "/hello");

		}
		return "hello";
	}
}
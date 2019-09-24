package com.sso.client.controller;

import javax.servlet.http.HttpServletRequest;

import com.sso.client.service.UserAccessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class SsoClientController{
    @Autowired
    private RestTemplate restTemplate;
	
	@Value("${sso.server.url}")
	String ssoServerPath;
	
	@Autowired
	private UserAccessService userAccessService;
	
	@RequestMapping("/receiveToken")
	@ResponseBody
	public String receiveToken(HttpServletRequest request, String ssoToken,String userName) {
		if(ssoToken!=null && ssoToken.toString().trim().length()>0) {
			String realUrl = request.getRequestURL().toString();
			String[] paths = realUrl.split("/");
			String realUrlUrls = paths[2];
			String returnUrl = ssoServerPath+"/varifyToken?address="+realUrlUrls+"&token="+ssoToken;
			//http://127.0.0.1:8080/varifyToken?address=127.0.0.1:9001&token=7d162c08-76b5-48b5-aec2-cd6917cb5c36
			String resultStr =  restTemplate.getForObject(returnUrl, String.class);
			if("true".equals(resultStr)) {
				//创建局部会话，保存用户状态为已登陆
				userAccessService.putUserStatus(userName, ssoToken);
				return "success";
			}
		}
		return "error";
	}
	
	@RequestMapping("/ssoLogout")
	@ResponseBody
	public String ssoLogout(String userName) {
		String userToken = userAccessService.getUserToken(userName);
		if(userToken!=null) {
			String returnUrl = ssoServerPath+"/logoutByToken?ssoToken="+userToken;
			return restTemplate.getForObject(returnUrl, String.class);
		}
		return "None Token";
	}
	
	@RequestMapping("/ssoDeleteToken")
	@ResponseBody
	public String ssoDeleteToken(String ssoToken) {
		userAccessService.deleteToken(ssoToken);
		return "success";
	}
	@RequestMapping("/testLogin")
	@ResponseBody
	public String testLogin(){
		return "login sso-client success!";
	}
}
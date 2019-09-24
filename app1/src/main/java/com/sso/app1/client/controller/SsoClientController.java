package com.sso.app1.client.controller;
import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sso.app1.client.service.UserAccessService;
import com.sso.app1.client.until.CookieUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class SsoClientController{
    @Autowired
    private RestTemplate restTemplate;
	@Value("${sso.server.returnUrl}")
	String ssoServerReturnUrl;// 成功后返回的Url

	@Value("${sso.server.url}")
	String ssoServerPath;
	
	@Autowired
	private UserAccessService userAccessService;
	
	@RequestMapping("/receiveToken")
	@ResponseBody
	public String receiveToken(HttpServletRequest request,HttpServletResponse resPonse, String ssoToken,String userName) {
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
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public void logout(ServletRequest request, ServletResponse response) {
		Cookie token = CookieUtils.getCookie("token");
		restTemplate.getForObject(ssoServerPath + "/logoutByToken?ssoToken=" + token.getValue(), String.class);
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		try {
			CookieUtils.removeCookie("token");
			httpResponse.sendRedirect(ssoServerReturnUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	} 
	@RequestMapping("/ssoDeleteToken")
	@ResponseBody
	public String ssoDeleteToken(String ssoToken) {
		userAccessService.deleteToken(ssoToken);
		return "success";
	}

}
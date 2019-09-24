package com.sso.app3.client.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sso.app3.client.service.UserAccessService;
import com.sso.app3.client.until.CookieUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.web.client.RestTemplate;

@Order(1)
@WebFilter(filterName = "ssoFilter", urlPatterns = "/hello", initParams ={@WebInitParam(name ="EXCLUDED_PAGES" , value = "/receiveToken,/ssoLogout,/ssoDeleteToken")})
public class CommonAuthFilter implements Filter{
    @Autowired
	private UserAccessService userAccessService;
	@Autowired
    private RestTemplate restTemplate;
	
	private String excludedPages;
	private String[] excludedPageArray;
	
	@Value("${sso.server.url}")
	String ssoServerPath;
	@Value("${sso.server.returnUrl}")
	String ssoServerReturnUrl;//成功后返回的Url	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		excludedPages = filterConfig.getInitParameter("EXCLUDED_PAGES");
		if(excludedPages!=null) {
			excludedPageArray = excludedPages.split(",");
		}
		
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		Object userName = req.getParameter("ssoUser");
		if(userName!=null 
				&& String.valueOf(userName).trim().length()>0 
				&& userAccessService.getUserToken(userName.toString())!=null) {
			chain.doFilter(req, response);
		}else {
			boolean containtFlag = false;
			//http://peer1:8088/sendToken?ssoToken=c2ce29be-5adb-4aaf-82cc-2ba24330176e&userName=6677
			if(excludedPageArray!=null) {
				for(String excludeStr : excludedPageArray) {
					if(excludeStr.equals(req.getServletPath())) {
						containtFlag = true;
						break;
					}
				}
			}
			if(containtFlag) {
				chain.doFilter(req, response);
			}else {
				//其他情况都丢给SSO中心去处理
				 HttpServletResponse httpResponse = (HttpServletResponse)response;
				// String originalUrl = req.getRequestURL().toString();
				  Cookie token= CookieUtils.getCookie(req,"token");
				 if(token!=null){
					String returnUrl = ssoServerPath+"/varifyToken?address="+ssoServerReturnUrl+"&token="+token.getValue();
					String resultStr =  restTemplate.getForObject(returnUrl, String.class);
					if("true".equals(resultStr)) {
						httpResponse.sendRedirect(ssoServerReturnUrl);
						return;
					}
				 }
				 //http://127.0.0.1:8080/varifyToken?address=127.0.0.1:9001&token=7d162c08-76b5-48b5-aec2-cd6917cb5c36
				
				 httpResponse.sendRedirect(ssoServerPath+"/index?originalUrl="+ssoServerReturnUrl+"&ssoUser="+userName);
			}
		}
	}

	@Override
	public void destroy() {
	
		
	}
}
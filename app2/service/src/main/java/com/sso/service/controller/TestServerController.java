package com.sso.service.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestServerController {

	@RequestMapping("/hello")
	public String hello() {
		return "hello,app2";
	}
}
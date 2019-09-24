package com.sso.app3.client.service;

import org.springframework.stereotype.Service;

@Service
public interface UserAccessService{
    String getUserToken(String user);
	
	void putUserStatus(String user, String flag);
	
	void deleteToken(String user);
}
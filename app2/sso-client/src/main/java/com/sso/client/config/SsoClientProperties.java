package com.sso.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sso.server")
public class SsoClientProperties{
    private String url;
    public String getUrl(){
        return this.url;
    }
    public void setUrl(String url){
        this.url=url;
    }
    @Override
    public String toString() {
       return "SsoClientProperties [url=" + url+ "]";
    }
}   
package com.sso.app3.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sso.server")
public class SsoClientProperties{
    private String url;
    private String returnUrl;
    public String getUrl(){
        return this.url;
    }
    public void setUrl(String url){
        this.url=url;
    }
    public String getReturnUrl(){
        return this.returnUrl;
    }
    /**
     * @param returnUrl the returnUrl to set
     */
    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
    @Override
    public String toString() {
       return "SsoClientProperties [url=" + url+ "]";
    }
}   
package com.tanbin.jwt.config;

import com.tanbin.jwt.filter.JwtAuthenticationTokenFilter;
import com.tanbin.jwt.service.UmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UmsAdminService adminService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()   //使用的是JWT，所以不需要跨域
                .disable()
                .sessionManagement()   //使用token，不需要session管理
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v2/api-docs/**"
                )
                .permitAll()
                .antMatchers("/admin/login", "/admin/register")  // 对登录注册要允许匿名访问.
                .permitAll()
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .anyRequest()
                .authenticated(); // 除上面外的所有请求全部需要鉴权认证

        //禁用缓存
        http.headers().cacheControl();

        //添加JWT filter
        // http.addFilterBefore(jwtAuthenticationTokenFilter())



    }

    @Bean
    public Object jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }
}

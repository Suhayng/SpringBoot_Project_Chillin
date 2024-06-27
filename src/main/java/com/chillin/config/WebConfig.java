package com.chillin.config;

import com.chillin.interceptor.LoginInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()) // LogInterceptor 등록
                .order(1)  // 적용할 필터 순서 설정
                .addPathPatterns("/**") //인터셉터 적용할 패턴
                .excludePathPatterns(  // 인터셉터에서 제외할 패턴
                        "", "/", "/notice", "/community", "/info", "/community/*", "/notice/*"
                        , "/login","/login_result", "loginfail", "/join", "/logout"
                        , "/joinresult", "/loginfail", "/id_check", "/nick_check"
                        , "/getImage/*", "/community/board/get_boom/*", "/getReps/*", "/getReps2/*"
                        , "/community/rep_boomup/*", "/community/rep_boomdown/*"
                        , "/css/**", "/ckeditor5/**", "/images/**", "/js/**");
    }
}

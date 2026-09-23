package com.fineui.java.quickstart;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** 给不经过 FineUI 页面控制器的错误响应补充脚本策略。 */
@Configuration(proxyBeanMethods = false)
public class StrictCspWebConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ErrorResponseCspInterceptor());
    }

    private static final class ErrorResponseCspInterceptor implements HandlerInterceptor {

        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response,
                               Object handler, ModelAndView modelAndView) {
            applyErrorPolicy(response);
        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                    Object handler, Exception exception) {
            // 未登记的页面路由可能以异常结束，完成请求时再检查一次。
            applyErrorPolicy(response);
        }

        private void applyErrorPolicy(HttpServletResponse response) {
            if (response.getStatus() < 400 || response.containsHeader("Content-Security-Policy")) {
                return;
            }

            response.setHeader("Content-Security-Policy", "script-src 'none';");
        }
    }
}

package org.example.base.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
public class GrayHeaderInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader(Constant.X_GRAY_VERSION);
        if (Objects.isNull(header)) {
            header = Constant.X_DEFAULT_VERSION;
        }
        // 将请求头信息存储到ThreadLocal中
        GrayHeaderContextHolder.setGrayHeader(header);
        response.setHeader(Constant.X_GRAY_VERSION, header);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        GrayHeaderContextHolder.clearGrayHeader();
    }
}

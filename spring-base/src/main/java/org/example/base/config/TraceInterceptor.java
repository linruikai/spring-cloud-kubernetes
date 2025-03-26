package org.example.base.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TraceInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//        Span span = Span.current();
//        try (Scope scope = span.makeCurrent()) {
//            InputStream inputStream = request.getInputStream();
//            String body = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
//                    .lines()
//                    .collect(Collectors.joining("\n"));
//            // 将请求体放入 span 的属性中
//            span.setAttribute("http.request.body", body);
//            response.setHeader("trace_id", span.getSpanContext().getTraceId());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            span.end();
//        }
    }
}

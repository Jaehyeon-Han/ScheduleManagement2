package hello.schedulemanagement2.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.schedulemanagement2.global.error.ErrorResponse;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

import static hello.schedulemanagement2.global.constant.SessionConst.LOGIN_USER;
import static hello.schedulemanagement2.global.constant.UnauthorizedHttpConst.*;

public class LoginFilter implements Filter {

    private static final String[] WHITE_LIST = {"/users/signup", "/login", "/logout"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String requestURI = httpRequest.getRequestURI();

        // 로그인 필요
        if (notInWhiteList(requestURI)) {
            HttpSession session = ((HttpServletRequest) servletRequest).getSession(false);

            // ErrorResponse 객체를 Jackson으로 직렬화하여 반환
            if (session == null || session.getAttribute(LOGIN_USER) == null) {
                httpResponse.setStatus(UNAUTHORIZED_STATUS);
                httpResponse.setContentType("application/json");
                httpResponse.setCharacterEncoding("utf-8");

                ObjectMapper objectMapper = new ObjectMapper();

                // UnauthorizedConstant 클래스 static import
                ErrorResponse errorResponse = new ErrorResponse(UNAUTHORIZED_TITLE, UNAUTHORIZED_DETAIL, UNAUTHORIZED_STATUS, requestURI);

                String errorResponseString = objectMapper.writeValueAsString(errorResponse);
                httpResponse.getWriter().write(errorResponseString);
                return;
            }
        }

        // 로그인 불필요
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean notInWhiteList(String requestURI) {
        return !PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}

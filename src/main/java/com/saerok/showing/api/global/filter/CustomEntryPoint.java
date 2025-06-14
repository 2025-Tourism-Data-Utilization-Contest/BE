package com.saerok.showing.api.global.filter;

import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException e
    ) throws IOException {
        ResponseUtil.writeError(response, ErrorCode.MEMBER_NOT_AUTHENTICATED);
    }
}

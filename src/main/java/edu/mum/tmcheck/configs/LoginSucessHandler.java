package edu.mum.tmcheck.configs;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSucessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String userrole;// = String.valueOf(authentication.getAuthorities());
        for (GrantedAuthority a : authentication.getAuthorities()) {
            userrole = a.getAuthority();
            if (userrole.equalsIgnoreCase("ROLE_faculty")) {
                redirectStrategy.sendRedirect(request, response, "/reports/ec-attendance-report");
            } else if (userrole.equalsIgnoreCase("ROLE_admin")) {
                redirectStrategy.sendRedirect(request, response, "/reports/entry-attendance-report");
            } else if (userrole.equalsIgnoreCase("ROLE_student")) {
                redirectStrategy.sendRedirect(request, response, "/student/report");
            } else
                redirectStrategy.sendRedirect(request, response, "/student/report");
        }
    }
}

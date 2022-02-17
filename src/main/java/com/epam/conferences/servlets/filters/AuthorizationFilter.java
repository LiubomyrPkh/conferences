package com.epam.conferences.servlets.filters;

import com.epam.conferences.DAO.entityData.Person;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@WebFilter("/*")
public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Map<String,List<String>> allowedPages = new HashMap<String,List<String>>() {{
            put(Person.Role.USER.getRole(), Arrays.asList("events", "eventItem", "profile", "login", "register", "profileEdit"));
            put(Person.Role.SPEAKER.getRole(), Arrays.asList("events", "eventItem", "profile", "profileEdit", "proposeReport", "login", "register"));
            put(Person.Role.MODERATOR.getRole(), Arrays.asList("events", "eventItem", "profile", "profileEdit", "createEvent", "editEvent", "login", "register"));
        }};

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);

        String loginURI = request.getContextPath() + "/login";
        String registerURI = request.getContextPath() + "/registration";
        String mainPage = request.getContextPath() + "/";
        String staticDirectory = "/static/";
        String requestURI = request.getRequestURI();

        boolean loggedIn = session != null && session.getAttribute("loggedUser") != null;
        boolean loginRequest = requestURI.equals(loginURI);
        boolean registerRequest = requestURI.equals(registerURI);
        boolean mainPageRequest = requestURI.equals(mainPage);
        boolean isStaticResource = requestURI.contains(staticDirectory);

        if(loggedIn) {
            String role = ((Person) session.getAttribute("loggedUser")).getRole();
            List<String> pages = allowedPages.get(role);
            if(pages.stream().anyMatch(requestURI::endsWith)) {
                filterChain.doFilter(request, response);
            } else {
                if (registerRequest || loginRequest || mainPageRequest || isStaticResource) {
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                }
            }
        } else {
            if (registerRequest || loginRequest || mainPageRequest || isStaticResource) {
                filterChain.doFilter(request, response);
            } else {
                response.sendRedirect(loginURI);
            }
        }


    }
}

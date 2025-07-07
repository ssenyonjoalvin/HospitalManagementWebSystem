package org.pahappa.systems.filters;

import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.pahappa.systems.services.session.SessionManager;

import java.io.IOException;

//@WebFilter("*.xhtml")
public class SessionFilter implements Filter {

    @Inject
    private SessionManager sessionManager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        // Allow access to login page and resources
        if (isPublicResource(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        // Check if user is logged in
        if (!sessionManager.isUserLoggedIn()) {
            // Redirect to login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");
            return;
        }

        // Update session activity
        sessionManager.updateSessionActivity();

        // Continue with the request
        chain.doFilter(request, response);
    }

    private boolean isPublicResource(String requestURI) {
        return requestURI.contains("/login.xhtml") ||
                requestURI.endsWith("/login.xhtml") ||
                requestURI.contains("/resources/") ||
                requestURI.contains("/css/") ||
                requestURI.contains("/js/") ||
                requestURI.contains("/images/") ||
                requestURI.contains("/fonts/") ||
                requestURI.endsWith(".css") ||
                requestURI.endsWith(".js") ||
                requestURI.endsWith(".png") ||
                requestURI.endsWith(".jpg") ||
                requestURI.endsWith(".jpeg") ||
                requestURI.endsWith(".gif") ||
                requestURI.endsWith(".ico");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
    }
}
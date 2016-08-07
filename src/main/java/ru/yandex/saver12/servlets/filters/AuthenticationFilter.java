package ru.yandex.saver12.servlets.filters;

import org.apache.log4j.Logger;
import ru.yandex.saver12.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static ru.yandex.saver12.util.Constants.*;

/**
 * Filters not authorized access requests.
 *
 * @author Sergey Ermushin
 */

public class AuthenticationFilter implements Filter {

    /**
     * log4j Logger object.
     */
    private Logger logger = Logger.getLogger(AuthenticationFilter.class);

    /**
     * Writes into log file about Filter initializing .
     *
     * @param fConfig FilterConfig object used by
     *                a servlet container to pass information
     *                to a filter during initialization.
     * @throws ServletException if an exception occurs
     *                          that interrupts the servlet's
     *                          normal operation
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        logger.info("AuthenticationFilter initialized");
    }

    /**
     * Checks if session contains User attribute.
     * If true, allows the Filter to pass on the
     * request and response to the next entity in the chain.
     * Otherwise, redirects to login page.
     *
     * @param request  ServletRequest object
     * @param response ServletResponse object
     * @param chain    FilterChain object
     * @throws ServletException if an exception occurs
     *                          that interrupts the filter's
     *                          normal operation
     * @throws IOException      if an input or output error is
     *                          detected when the filter handles
     *                          requests
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String url = req.getServletPath();

        logger.info("Requested Resource:" + url);

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute(USER.toString());

        if (user == null && !(url.endsWith("login") || url.endsWith("register"))) {
            logger.error("Unauthorized access request");
            res.sendRedirect("login");
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        //close any resources here
    }
}
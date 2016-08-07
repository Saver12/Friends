package ru.yandex.saver12.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import static ru.yandex.saver12.util.Constants.*;

/**
 * Servlet that processes logout.
 *
 * @author Sergey Ermushin
 */

@WebServlet(name = "Logout", urlPatterns = {"/Logout"}, loadOnStartup = 0)
public class LogoutServlet extends HttpServlet {

    /**
     * log4j Logger object.
     */
    private static Logger logger = Logger.getLogger(LogoutServlet.class);

    /**
     * {@inheritDoc}
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID")) {
                    logger.info("JSESSIONID=" + cookie.getValue());
                    break;
                }
            }
        }
        HttpSession session = request.getSession(false);
        logger.info("User=" + session.getAttribute(USER.toString()));
        session.invalidate();
        response.sendRedirect("login");
    }
}
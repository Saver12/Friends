package ru.yandex.saver12.servlets;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import ru.yandex.saver12.dao.UserDAO;
import ru.yandex.saver12.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import static ru.yandex.saver12.util.Constants.*;
import static ru.yandex.saver12.util.UtilClass.validate;

/**
 * Servlet that processes authentication.
 *
 * @author Sergey Ermushin
 */

@WebServlet(name = "login", urlPatterns = {"/login"}, loadOnStartup = 0)
public class LoginServlet extends HttpServlet {

    /**
     * log4j Logger object.
     */
    private static Logger logger = Logger.getLogger(LoginServlet.class);

    /**
     * UserDAO object.
     */
    private UserDAO userDAO;

    /**
     * Invokes super.init(config) of GenericServlet and
     * initializes UserDAO object.
     *
     * @param config ServletConfig object that
     *               contains configuration
     *               information for this servlet
     * @throws ServletException if an exception occurs
     *                          that interrupts the servlet's
     *                          normal operation
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userDAO = (UserDAO) config.getServletContext().getAttribute(USER_DAO.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=utf-8");
        String lang = (String) request.getSession().getAttribute(LANGUAGE.toString());
        String email = request.getParameter(EMAIL.toString());
        String password = request.getParameter(PASSWORD.toString());
        String errorMsg = null;
        if (email == null || email.equals("")) {
            errorMsg = lang.equals("EN") ? "User Email can't be null or empty" : "Почта пользователя не может быть пустой";
        } else if (!validate(email)) {
            errorMsg = lang.equals("EN") ? "Enter valid email ID." : "Введите верный адрес почты.";
        }
        if (password == null || password.equals("")) {
            errorMsg = lang.equals("EN") ? "Password can't be null or empty" : "Пароль не может быть пустым";
        }

        if (errorMsg != null) {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/login.jsp");
            PrintWriter out = response.getWriter();
            out.println("<font color=red>" + errorMsg + "</font><br>");
            rd.include(request, response);
            out.close();
        } else {

            try {
                User user = userDAO.getUser(email, DigestUtils.md5Hex(password));
                if (user != null) {
                    logger.info("User found with details=" + user);
                    HttpSession session = request.getSession();
                    session.setAttribute(USER.toString(), user);
                    response.sendRedirect(response.encodeRedirectURL("profile"));
                } else {
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/login.jsp");
                    PrintWriter out = response.getWriter();
                    logger.error("User not found with email=" + email);
                    String userNull = lang.equals("EN") ? "No user found with given email id or password, please try again." : "Пользователей с данной почтой или паролем нет, пожалуйста, попробуйте еще раз.";
                    out.println("<font color=red>" + userNull + "</font>");
                    rd.include(request, response);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Error getting User by email and password.");
                throw new ServletException("Error getting User by email and password.");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }
}

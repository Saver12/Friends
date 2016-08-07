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
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import static ru.yandex.saver12.util.Constants.*;
import static ru.yandex.saver12.util.UtilClass.validate;

/**
 * Servlet that processes registration.
 *
 * @author Sergey Ermushin
 */

@WebServlet(name = "register", urlPatterns = {"/register"}, loadOnStartup = 0)
public class RegisterServlet extends HttpServlet {

    /**
     * log4j Logger object.
     */
    private static Logger logger = Logger.getLogger(RegisterServlet.class);

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String lang = (String) request.getSession().getAttribute("language");

        String email = request.getParameter(EMAIL.toString());
        String password = request.getParameter(PASSWORD.toString());
        String name = request.getParameter(NAME.toString());
        String surname = request.getParameter(SURNAME.toString());
        String country = request.getParameter(COUNTRY.toString());
        String errorMsg = null;
        if (email == null || email.equals("")) {
            errorMsg = lang.equals("EN") ? "User Email can't be null or empty." : "Почта пользователя не может быть пустой";
        } else if (!validate(email)) {
            errorMsg = lang.equals("EN") ? "Enter valid email ID." : "Введите верный адрес почты.";
        }
        if (password == null || password.equals("")) {
            errorMsg = lang.equals("EN") ? "Password can't be null or empty" : "Пароль не может быть пустым";
        }
        if (name == null || name.equals("")) {
            errorMsg = lang.equals("EN") ? "Name can't be null or empty" : "Имя не может быть пустым";
        }
        if (surname == null || surname.equals("")) {
            errorMsg = lang.equals("EN") ? "Surname can't be null or empty" : "Фамилия не может быть пустой";
        }
        if (country == null || country.equals("")) {
            errorMsg = lang.equals("EN") ? "Country can't be null or empty" : "Страна не может быть пустой";
        }

        if (errorMsg != null) {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/register.jsp");
            PrintWriter out = response.getWriter();
            out.println("<font color=red>" + errorMsg + "</font>");
            rd.include(request, response);
            out.close();
        } else {

            try {
                User user = userDAO.createUser(name, surname, email, country, DigestUtils.md5Hex(password));
                logger.info("User registered=" + user);
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/login.jsp");
                PrintWriter out = response.getWriter();
                String success = lang.equals("EN") ? "Registration successful, please login below." : "Регистрация завершена, пожалуйста, авторизуйтесь ниже.";
                out.println("<font color=green>" + success + "</font>");
                rd.include(request, response);
            } catch (SQLException e) {
                logger.error("Duplicate email forbidden");
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/register.jsp");
                PrintWriter out = response.getWriter();
                String duplicate = lang.equals("EN") ? "User with this Email ID already exists." : "Пользователь с таким почтовым адресом уже существует.";
                out.println("<font color=red>" + duplicate + "</font>");
                rd.include(request, response);
                out.close();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
    }
}
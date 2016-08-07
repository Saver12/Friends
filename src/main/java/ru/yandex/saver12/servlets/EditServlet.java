package ru.yandex.saver12.servlets;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ru.yandex.saver12.util.Constants.*;

/**
 * Servlet that processes profile editing.
 *
 * @author Sergey Ermushin
 */

@WebServlet(name = "edit", urlPatterns = {"/edit"}, loadOnStartup = 0)
public class EditServlet extends HttpServlet {

    /**
     * log4j Logger object.
     */
    private static Logger logger = Logger.getLogger(EditServlet.class);

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.getRequestDispatcher("/WEB-INF/jsp/edit.jsp").forward(request, response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute(USER.toString());
        String lang = (String) request.getSession().getAttribute(LANGUAGE.toString());
        String name = request.getParameter(NAME.toString());
        String surname = request.getParameter(SURNAME.toString());
        String country = request.getParameter(COUNTRY.toString());
        String errorMsg = null;
        if (name == null || name.equals("")) {
            errorMsg = lang.equals("EN") ? "User Name can't be null or empty" : "Имя пользователя не может быть пустым";
        }
        if (surname == null || surname.equals("")) {
            errorMsg = lang.equals("EN") ? "User Surname can't be null or empty" : "Фамилия пользователя не может быть пустой";
        }
        if (country == null || country.equals("")) {
            errorMsg = lang.equals("EN") ? "Country can't be null or empty" : "Страна не может быть пустой";
        }

        if (errorMsg != null) {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/edit.jsp");
            PrintWriter out = response.getWriter();
            rd.include(request, response);
            out.println("<div class=\"error\">" + errorMsg + "</div>");
            out.close();
        } else {
            try {
                String birthdate = request.getParameter(BIRTHDATE.toString());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = birthdate.equals("") ? null : sdf.parse(birthdate);
                String job = request.getParameter(JOB.toString()).replace("\"", "");
                String education = request.getParameter(EDUCATION.toString());
                String relationship = request.getParameter(RELATIONSHIP.toString());
                int userId = loggedUser.getId();
                User user = new User(name, surname, loggedUser.getEmail(), country, date, job,
                        education, relationship, userId, loggedUser.getPhotoId());
                userDAO.updateUser(user);
                session.setAttribute(USER.toString(), user);
                logger.info("Updated User with id=" + userId);
            } catch (SQLException | ParseException e) {
                e.printStackTrace();
                logger.error("Error updating User.");
                throw new ServletException("Error updating User.");
            }
            request.getRequestDispatcher("/WEB-INF/jsp/edit.jsp").forward(request, response);
        }
    }
}

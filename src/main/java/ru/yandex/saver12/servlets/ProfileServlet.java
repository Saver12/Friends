package ru.yandex.saver12.servlets;

import org.apache.log4j.Logger;
import ru.yandex.saver12.dao.UserDAO;
import ru.yandex.saver12.model.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static ru.yandex.saver12.util.Constants.*;
import static ru.yandex.saver12.util.UtilClass.checkParam;

/**
 * Servlet that processes Profile Page.
 *
 * @author Sergey Ermushin
 */

@WebServlet(name = "profile", urlPatterns = {"/profile"}, loadOnStartup = 0)
public class ProfileServlet extends HttpServlet {

    /**
     * log4j Logger object.
     */
    private static Logger logger = Logger.getLogger(ProfileServlet.class);

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

        int id = checkParam(request);
        User loggedUser = (User) request.getSession().getAttribute(USER.toString());
        int myId = loggedUser.getId();
        if (id != 0 && id != myId) {
            try {
                User user = userDAO.getUserById(request, id, myId);
                if (user != null) {
                    logger.info("User found with details=" + user);
                    request.setAttribute(ID.toString(), user);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Error getting User by ID.");
                throw new ServletException("Error getting User by ID.");
            }
        }
        request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
    }
}

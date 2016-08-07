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
import java.util.List;

import static ru.yandex.saver12.util.Constants.*;

/**
 * Servlet that processes User Search.
 *
 * @author Sergey Ermushin
 */

@WebServlet(name = "search", urlPatterns = {"/search"}, loadOnStartup = 0)
public class SearchServlet extends HttpServlet {

    /**
     * log4j Logger object.
     */
    private static Logger logger = Logger.getLogger(SearchServlet.class);

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
        request.getRequestDispatcher("/WEB-INF/jsp/search.jsp").forward(request, response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter(NAME.toString());

        if (name != null && !name.equals("")) {
            try {
                List<User> list = userDAO.getUsersByName(name);
                logger.info("Users found with details=" + list);
                request.setAttribute(LIST.toString(), list);
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Error getting Users by name.");
                throw new ServletException("Error getting Users by name.");
            }
        }
        request.getRequestDispatcher("/WEB-INF/jsp/search.jsp").forward(request, response);
    }
}
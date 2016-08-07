package ru.yandex.saver12.servlets;

import org.apache.log4j.Logger;
import ru.yandex.saver12.dao.FriendsDAO;
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
 * Servlet that processes Friend Requests.
 *
 * @author Sergey Ermushin
 */

@WebServlet(name = "requests", urlPatterns = {"/requests"}, loadOnStartup = 0)
public class RequestServlet extends HttpServlet {

    /**
     * log4j Logger object.
     */
    private static Logger logger = Logger.getLogger(RequestServlet.class);

    /**
     * FriendsDAO object.
     */
    private FriendsDAO friendsDAO;

    /**
     * Invokes super.init(config) of GenericServlet and
     * initializes FriendsDAO object.
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
        friendsDAO = (FriendsDAO) config.getServletContext().getAttribute(FRIENDS_DAO.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute(USER.toString());
        int id = user.getId();

        try {
            List<User> list = friendsDAO.getRequestsList(id);
            logger.info("Got list of requests.");
            request.setAttribute(REQUESTS_LIST.toString(), list);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error getting list of requests.");
            throw new ServletException("Error getting list of requests.");
        }
        request.getRequestDispatcher("/WEB-INF/jsp/requests.jsp").forward(request, response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        User loggedUser = (User) request.getSession().getAttribute(USER.toString());
        int myId = loggedUser.getId();
        String param = request.getParameter(REQUEST.toString());
        int id = Integer.parseInt(request.getParameter(ID.toString()));

        try {
            if (param.equals("accept")){
                friendsDAO.acceptRequest(id, myId);
                logger.info("Accepted friend request from id=" + id);
            } else if (param.equals("decline")){
                friendsDAO.declineRequest(id, myId);
                logger.info("Declined friend request from id=" + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Friend request process problem.");
            throw new ServletException("Friend request process problem.");
        }
        response.sendRedirect(response.encodeRedirectURL("requests"));
    }
}

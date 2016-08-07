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
import static ru.yandex.saver12.util.UtilClass.checkParam;

/**
 * Servlet that processes friends relationships.
 *
 * @author Sergey Ermushin
 */

@WebServlet(name = "friends", urlPatterns = {"/friends"}, loadOnStartup = 0)
public class FriendServlet extends HttpServlet {

    /**
     * log4j Logger object.
     */
    private static Logger logger = Logger.getLogger(FriendServlet.class);

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

        int id = checkParam(request);
        if (id == 0){
            User user = (User) request.getSession().getAttribute(USER.toString());
            id = user.getId();
            request.setAttribute(REQUEST.toString(), 1);
        }
        try {
            List<User> list = friendsDAO.getFriendsList(id);
            logger.info("Got list of user's friends.");
            request.setAttribute(FRIENDS_LIST.toString(), list);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error getting list of user's friends.");
            throw new ServletException("Error getting list of user's friends.");
        }
        request.getRequestDispatcher("/WEB-INF/jsp/friends.jsp").forward(request, response);
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
            switch (param) {
                case "add":
                    friendsDAO.addFriend(id, myId);
                    logger.info("Sent friend request to user with id=" + id);
                    break;
                case "delete":
                    friendsDAO.deleteFriend(id, myId);
                    logger.info("Deleted friend with id=" + id);
                    break;
                case "decline":
                    friendsDAO.declineRequest(id, myId);
                    logger.info("Declined friend request from id=" + id);
                    break;
                case "accept":
                    friendsDAO.acceptRequest(id, myId);
                    logger.info("Accepted friend request from id=" + id);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Friend request process problem.");
            throw new ServletException("Friend request process problem.");
        }
        response.sendRedirect(response.encodeRedirectURL("profile?id=" + id));
    }
}

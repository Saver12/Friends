package ru.yandex.saver12.servlets;

import org.apache.log4j.Logger;
import ru.yandex.saver12.dao.MessageDAO;
import ru.yandex.saver12.model.Message;
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
 * Servlet that processes message exchange.
 *
 * @author Sergey Ermushin
 */

@WebServlet(name = "messages", urlPatterns = {"/messages"}, loadOnStartup = 0)
public class MessageServlet extends HttpServlet {

    /**
     * log4j Logger object.
     */
    private static Logger logger = Logger.getLogger(MessageServlet.class);

    /**
     * MessageDAO object.
     */
    private MessageDAO messageDAO;

    /**
     * Invokes super.init(config) of GenericServlet and
     * initializes MessageDAO object.
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
        messageDAO = (MessageDAO) config.getServletContext().getAttribute(MESSAGE_DAO.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute(USER.toString());
        int myId = user.getId();
        int id = checkParam(request);

        try {
            if (id != 0) {
                List<Message> list = messageDAO.getMessageList(request, myId, id);
                logger.info("Got list of user's messages with " + request.getAttribute(NAME.toString()));
                request.setAttribute(USER_ID.toString(), id);
                request.setAttribute(MESSAGE_LIST.toString(), list);
            } else {
                List<User> list = messageDAO.getDialogueList(myId);
                logger.info("Got list of user's dialogues.");
                request.setAttribute(DIALOGUE_LIST.toString(), list);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error getting list of messages or dialogues.");
            throw new ServletException("Error getting list of messages or dialogues.");
        }
        request.getRequestDispatcher("/WEB-INF/jsp/messages.jsp").forward(request, response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String param = request.getParameter(REQUEST.toString());
        User user = (User) request.getSession().getAttribute(USER.toString());
        int myId = user.getId();
        int id = Integer.parseInt(request.getParameter(USER_ID.toString()));
        if (param.equals("send")) {
            String message = request.getParameter(MESSAGE.toString());
            if (message != null && !message.equals("")) {
                try {
                    Message newMessage = messageDAO.createMessage(message, myId, id);
                    logger.info("New message created = " + newMessage);
                } catch (SQLException ex) {
                    logger.error("Error creating new message.");
                    response.sendRedirect(response.encodeRedirectURL("messages"));
                    return;
                }
            }
            response.sendRedirect(response.encodeRedirectURL("messages?id=" + id));
        } else {
            try {
                messageDAO.deleteDialogue(myId, id);
                logger.info("Deleted dialogue with user by id= " + id);
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Error deleting dialogue.");
                throw new ServletException("Error deleting dialogue.");
            }
            response.sendRedirect(response.encodeRedirectURL("messages"));
        }
    }
}
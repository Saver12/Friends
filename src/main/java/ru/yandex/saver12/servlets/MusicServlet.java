package ru.yandex.saver12.servlets;

import org.apache.log4j.Logger;
import ru.yandex.saver12.dao.AudioDAO;
import ru.yandex.saver12.model.Audio;
import ru.yandex.saver12.model.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static ru.yandex.saver12.util.Constants.*;
import static ru.yandex.saver12.util.UtilClass.checkParam;

/**
 * Servlet that processes User's audios.
 *
 * @author Sergey Ermushin
 */

@WebServlet(name = "audios", urlPatterns = {"/audios"}, loadOnStartup = 0)
@MultipartConfig(maxFileSize = 1024 * 1024 * 50)
public class MusicServlet extends HttpServlet {

    /**
     * log4j Logger object.
     */
    private static Logger logger = Logger.getLogger(MusicServlet.class);

    /**
     * AudioDAO object.
     */
    private AudioDAO audiosDAO;

    /**
     * Invokes super.init(config) of GenericServlet and
     * initializes AudioDAO object.
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
        audiosDAO = (AudioDAO) config.getServletContext().getAttribute(AUDIOS_DAO.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int id = checkParam(request);
        if (id == 0) {
            User user = (User) request.getSession().getAttribute(USER.toString());
            id = user.getId();
        }
        try {
            List<Audio> list = audiosDAO.getAudiosList(id);
            logger.info("Got list of user's audios.");
            request.setAttribute(AUDIOS_LIST.toString(), list);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error getting list of user's audios.");
            throw new ServletException("Error getting list of user's audios.");
        }
        request.getRequestDispatcher("/WEB-INF/jsp/audios.jsp").forward(request, response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute(USER.toString());
        String param = request.getParameter(REQUEST.toString());
        int id = Integer.parseInt(request.getParameter(ID.toString()));
        String uploadFilePath = request.getServletContext().getRealPath(MEDIA_DIR.toString());
        try {
            switch (param) {
                case "upload":
                    uploadAudio(request, uploadFilePath, loggedUser.getId());
                    break;
                case "delete":
                    deleteAudio(uploadFilePath, id);
                    break;
                case "change":
                    updateDescription(request, id);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error processing audio.");
            throw new ServletException("Error processing audio.");
        }
        response.sendRedirect(response.encodeRedirectURL("audios"));
    }

    /**
     * Creates a new Audio object, saves it to database and uploads it to disk.
     *
     * @param request        HttpServletRequest object
     * @param uploadFilePath FilePath to put audio into
     * @param id             Authorized User's id
     */
    private void uploadAudio(HttpServletRequest request, String uploadFilePath, int id) throws IOException, ServletException, SQLException {
        Part filePart = request.getPart(AUDIO.toString());
        String fileName = filePart.getSubmittedFileName();
        if (fileName.endsWith(".mp3")) {
            Audio audio = audiosDAO.createAudio("", id);
            int audioId = audio.getId();
            filePart.write(uploadFilePath + File.separator + audioId + ".mp3");
            logger.info("Audio uploaded with id=" + audioId);
        }
    }

    /**
     * Deletes audio from database and from disk.
     *
     * @param uploadFilePath FilePath where audio lies
     * @param id             Authorized User's id
     */
    private void deleteAudio(String uploadFilePath, int id) throws SQLException {
        audiosDAO.deleteAudio(id);
        File file = new File(uploadFilePath + File.separator + id + ".mp3");
        file.delete();
        logger.info("Audio deleted with id=" + id);
    }

    /**
     * Updates audio's description.
     *
     * @param request HttpServletRequest object
     * @param id      Authorized User's id
     */
    private void updateDescription(HttpServletRequest request, int id) throws SQLException {
        String message = request.getParameter(MESSAGE.toString());
        if (message != null && !message.equals("")) {
            audiosDAO.updateDescription(id, message);
            logger.info("Audio's description changed by id=" + id);
        }
    }
}

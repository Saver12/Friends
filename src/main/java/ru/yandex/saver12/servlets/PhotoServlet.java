package ru.yandex.saver12.servlets;

import org.apache.log4j.Logger;
import ru.yandex.saver12.dao.PhotoDAO;
import ru.yandex.saver12.model.Photo;
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
 * Servlet that processes User's photos.
 *
 * @author Sergey Ermushin
 */

@WebServlet(name = "photos", urlPatterns = {"/photos"}, loadOnStartup = 0)
@MultipartConfig(maxFileSize = 1024 * 1024 * 50)
public class PhotoServlet extends HttpServlet {

    /**
     * log4j Logger object.
     */
    private static Logger logger = Logger.getLogger(PhotoServlet.class);

    /**
     * PhotoDAO object.
     */
    private PhotoDAO photosDAO;

    /**
     * Invokes super.init(config) of GenericServlet and
     * initializes PhotoDAO object.
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
        photosDAO = (PhotoDAO) config.getServletContext().getAttribute(PHOTOS_DAO.toString());
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
            List<Photo> list = photosDAO.getPhotosList(id);
            logger.info("Got list of user's photos.");
            request.setAttribute(PHOTOS_LIST.toString(), list);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error getting list of user's photos.");
            throw new ServletException("Error getting list of user's photos.");
        }
        request.getRequestDispatcher("/WEB-INF/jsp/photos.jsp").forward(request, response);
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
        String uploadFilePath = request.getServletContext().getRealPath(UPLOAD_DIR.toString());
        try {
            switch (param) {
                case "upload":
                    uploadPhoto(request, uploadFilePath, loggedUser.getId());
                    break;
                case "delete":
                    deletePhoto(uploadFilePath, id);
                    break;
                case "change":
                    updateDescription(request, id);
                    break;
                case "changeAvatar":
                    changeProfilePhoto(session, loggedUser, id);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error processing photo.");
            throw new ServletException("Error processing photo.");
        }
        response.sendRedirect(response.encodeRedirectURL("photos"));
    }

    /**
     * Creates a new Photo object, saves it to database and uploads it to disk.
     *
     * @param request        HttpServletRequest object
     * @param uploadFilePath FilePath to put photo into
     * @param id             Authorized User's id
     */
    private void uploadPhoto(HttpServletRequest request, String uploadFilePath, int id) throws IOException, ServletException, SQLException {
        Part filePart = request.getPart(PHOTO.toString());
        String fileName = filePart.getSubmittedFileName();
        if (fileName.endsWith(".jpg")) {
            Photo photo = photosDAO.createPhoto("", id);
            int photoId = photo.getId();
            filePart.write(uploadFilePath + File.separator + photoId + ".jpg");
            logger.info("Photo uploaded with id=" + photoId);
        }
    }

    /**
     * Deletes photo from database and from disk.
     *
     * @param uploadFilePath FilePath where photo lies
     * @param id             Authorized User's id
     */
    private void deletePhoto(String uploadFilePath, int id) throws SQLException {
        photosDAO.deletePhoto(id);
        File file = new File(uploadFilePath + File.separator + id + ".jpg");
        file.delete();
        logger.info("Photo deleted with id=" + id);
    }

    /**
     * Updates photo's description.
     *
     * @param request HttpServletRequest object
     * @param id      Authorized User's id
     */
    private void updateDescription(HttpServletRequest request, int id) throws SQLException {
        String message = request.getParameter(MESSAGE.toString());
        if (message != null) {
            if (!message.equals("")) {
                photosDAO.updateDescription(id, message);
                logger.info("Photo's description changed by id=" + id);
            }
        }
    }

    /**
     * Changes User's Profile Photo.
     *
     * @param session HttpSession object
     * @param user    Authorized User's id
     * @param photoId Photo's id
     */
    private void changeProfilePhoto(HttpSession session, User user, int photoId) throws SQLException {
        int userId = user.getId();
        photosDAO.changeProfilePhoto(userId, photoId);
        session.setAttribute(USER.toString(), new User(user.getName(), user.getSurname(), user.getEmail(), user.getCountry(),
                user.getBirthdate(), user.getJob(), user.getEducation(), user.getRelationship(), userId, photoId));
        logger.info("Changed profile photo by id=" + photoId);
    }
}

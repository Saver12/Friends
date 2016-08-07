package ru.yandex.saver12.util;

import org.apache.commons.validator.routines.EmailValidator;
import ru.yandex.saver12.model.User;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static ru.yandex.saver12.util.Constants.ID;

/**
 * Utility class.
 *
 * @author Sergey Ermushin
 */

public class UtilClass {

    /**
     * Gets Connection from {@code DataSource} object.
     *
     * @param request HttpServletRequest object
     * @return integer if parameter exists, 0 if it doesn't
     */
    public static int checkParam(HttpServletRequest request) {
        String param = request.getParameter(ID.toString());
        if (param != null && !param.equals("")) {
            int id;
            try {
                id = Integer.parseInt(param);
            } catch (NumberFormatException e) {
                id = 0;
            }
            return id;
        }
        return 0;
    }

    /**
     * Creates new User object from ResultSet data.
     *
     * @param rs ResultSet object
     * @return User object
     */
    public static User newUser(ResultSet rs) throws SQLException {
        java.sql.Date birthdate = rs.getDate("birthdate");
        Date date = birthdate != null ? new Date(birthdate.getTime()) : null;
        return new User(rs.getString("name"), rs.getString("surname"), rs.getString("email"), rs.getString("country"),
                date, rs.getString("job"), rs.getString("education"), rs.getString("relationship"),
                rs.getInt("userId"), rs.getInt("photoId"));
    }

    /**
     * Checks if a string has a valid e-mail address.
     *
     * @param email The value validation is being performed on.
     *              A <code>null</code> value is considered invalid
     * @return true if the email address is valid
     */
    public static boolean validate(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }
}

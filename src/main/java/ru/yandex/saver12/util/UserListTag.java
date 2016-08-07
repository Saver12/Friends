package ru.yandex.saver12.util;

import ru.yandex.saver12.model.User;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

/**
 * Custom Tag class.
 *
 * @author Sergey Ermushin
 */

public class UserListTag extends TagSupport {

    /**
     * List of users representing custom tag attribute.
     */
    private List<User> list;

    /**
     * @param list List of users
     */
    public void setList(List<User> list) {
        this.list = list;
    }

    /**
     * Processing of the start tag that writes list of users to the
     * JspWriter's buffer or, if no buffer is used, directly to the
     * underlying writer.
     *
     * @return SKIP_BODY
     */
    @Override
    public int doStartTag() throws JspException {

        JspWriter out = pageContext.getOut();
        try {
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size() - 1; i++) {
                    out.print(getString(list.get(i)) + "<hr>");
                }
                out.print(getString(list.get(list.size() - 1)));
            }
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }

    /**
     * Creates String object representing hyperlink to User's Profile Page.
     *
     * @param user User object
     * @return Connection object
     */
    private String getString(User user) {

        HttpServletResponse response = (HttpServletResponse)pageContext.getResponse();
        String encodeURL = response.encodeURL("profile");

        return "<a href=\"" + encodeURL + "?id=" + user.getId() +
                "\" class=\"names\">" + user.getName() + " " + user.getSurname() +
                "</a>";
    }
}
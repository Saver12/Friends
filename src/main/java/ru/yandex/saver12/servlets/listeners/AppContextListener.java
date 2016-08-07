package ru.yandex.saver12.servlets.listeners;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import ru.yandex.saver12.dao.impl.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;

import static ru.yandex.saver12.util.Constants.*;

/**
 * ContextListener that processes logging and context attributes.
 *
 * @author Sergey Ermushin
 */

@WebListener
public class AppContextListener implements ServletContextListener {

    /**
     * Sets DAO objects as context attributes, configures log4j
     * at the web application initialization start.
     *
     * @param servletContextEvent ServletContextEvent object
     */
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext ctx = servletContextEvent.getServletContext();

        ctx.setAttribute(USER_DAO.toString(), new UserDAOImpl());
        ctx.setAttribute(MESSAGE_DAO.toString(), new MessageDAOImpl());
        ctx.setAttribute(FRIENDS_DAO.toString(), new FriendsDAOImpl());
        ctx.setAttribute(PHOTOS_DAO.toString(), new PhotoDAOImpl());
        ctx.setAttribute(AUDIOS_DAO.toString(), new AudioDAOImpl());
        String[] rustats = {"", "В активном поиске", "Есть друг/подруга", "Влюблен/влюблена", "Замужем/женат"};
        String[] enstats = {"", "Actively searching", "In a relationship", "In love", "Married"};
        ctx.setAttribute(STATS_RU.toString(), rustats);
        ctx.setAttribute(STATS_EN.toString(), enstats);

        //initialize log4j
        String log4jConfig = ctx.getInitParameter("log4j-config");
        if (log4jConfig == null) {
            System.err.println("No log4j-config init param, initializing log4j with BasicConfigurator");
            BasicConfigurator.configure();
        } else {
            String webAppPath = ctx.getRealPath("/");
            String log4jProp = webAppPath + log4jConfig;
            File log4jConfigFile = new File(log4jProp);
            if (log4jConfigFile.exists()) {
                System.out.println("Initializing log4j with: " + log4jProp);
                DOMConfigurator.configure(log4jProp);
            } else {
                System.err.println(log4jProp + " file not found, initializing log4j with BasicConfigurator");
                BasicConfigurator.configure();
            }
        }
        System.out.println("log4j configured properly");
    }

    /**
     * {@inheritDoc}
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
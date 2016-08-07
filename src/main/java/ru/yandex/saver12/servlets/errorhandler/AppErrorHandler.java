package ru.yandex.saver12.servlets.errorhandler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that handles errors and exceptions.
 *
 * @author Sergey Ermushin
 */

@WebServlet("/AppErrorHandler")
public class AppErrorHandler extends HttpServlet {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processError(request, response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processError(request, response);
    }

    /**
     * Processes errors and exceptions and writes
     * collected data to Error Page.
     *
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws IOException if an input or output error is
     *                     detected when the servlet handles
     *                     requests
     */
    private void processError(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Throwable throwable = (Throwable) request
                .getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        String servletName = (String) request
                .getAttribute("javax.servlet.error.servlet_name");
        if (servletName == null) {
            servletName = "Unknown";
        }
        String requestUri = (String) request
                .getAttribute("javax.servlet.error.request_uri");
        if (requestUri == null) {
            requestUri = "Unknown";
        }

        response.setContentType("text/html; charset=utf-8");
        String lang = (String) request.getSession().getAttribute("language");

        String title = lang.equals("EN") ? "Exception/Error Details" : "Детали ошибки/исключения";
        String errorDetails = lang.equals("EN") ? "Error Details" : "Детали ошибки";
        String exceptionDetails = lang.equals("EN") ? "Exception Details" : "Детали исключения";
        String code = lang.equals("EN") ? "Status Code" : "Код ошибки";
        String uri = lang.equals("EN") ? "Requested URI" : "Запрашиваемый URI";
        String servletString = lang.equals("EN") ? "Servlet Name" : "Имя сервлета";
        String exceptionString = lang.equals("EN") ? "Exception Name" : "Имя исключения";
        String message = lang.equals("EN") ? "Exception Message" : "Сообщение исключения";
        String login = lang.equals("EN") ? "Login Page" : "Вход";

        PrintWriter out = response.getWriter();
        out.write("<html><head><title>" + title + "</title></head><body>");
        if (statusCode != 500) {
            out.write("<h3>" + errorDetails + "</h3>");
            out.write("<strong>" + code + "</strong>:" + statusCode + "<br>");
            out.write("<strong>" + uri + "</strong>:" + requestUri);
        } else {
            out.write("<h3>" + exceptionDetails + "</h3>");
            out.write("<ul><li>" + servletString + ":" + servletName + "</li>");
            out.write("<li>" + exceptionString + ":" + throwable.getClass().getName() + "</li>");
            out.write("<li>" + uri + ":" + requestUri + "</li>");
            out.write("<li>" + message + ":" + throwable.getMessage() + "</li>");
            out.write("</ul>");
        }

        out.write("<br><br>");
        out.write("<a href=\"login\">" + login + "</a>");
        out.write("</body></html>");
        out.close();
    }
}
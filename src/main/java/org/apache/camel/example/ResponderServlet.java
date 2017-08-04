package org.apache.camel.example;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResponderServlet extends HttpServlet {
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final int statusCode = Integer.parseInt(req.getPathInfo().split("/")[1]);
        resp.setStatus(statusCode);

        PrintWriter out = resp.getWriter();
        out.println("<h1>All went well!</h1>");
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(201);
    }
}

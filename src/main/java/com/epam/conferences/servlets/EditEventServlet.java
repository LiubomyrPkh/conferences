package com.epam.conferences.servlets;

import com.epam.conferences.DAO.entityData.Event;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/editEvent")
public class EditEventServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Event event = (Event) session.getAttribute("event");

        if(event != null) {
            session.removeAttribute("event");
            request.setAttribute("event", event);
            request.getRequestDispatcher("/WEB-INF/views/editEvent.jsp").forward(request, response);
        } else {
            session.setAttribute("error", "event was not found");
            request.getRequestDispatcher("/WEB-INF/views/events.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

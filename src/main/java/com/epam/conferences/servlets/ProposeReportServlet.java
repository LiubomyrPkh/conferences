package com.epam.conferences.servlets;

import com.epam.conferences.DAO.DAOFactory;
import com.epam.conferences.DAO.entityData.Address;
import com.epam.conferences.DAO.entityData.Event;
import com.epam.conferences.DAO.entityData.Person;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/proposeReport")
public class ProposeReportServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String error = (String) session.getAttribute("error");

        if(error != null) {
            request.setAttribute("error", error);
            session.removeAttribute("error");
        }

        request.getRequestDispatcher("/WEB-INF/views/proposeReport.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int flatNum = 0;
        int houseNum = 0;
        int insertedId = 0;
        String title = request.getParameter("title");
        HttpSession session = request.getSession(false);
        String datetime = request.getParameter("datetime");

        //Converting datetime from input to datetime string format
        LocalDateTime dateTime = LocalDateTime.parse(datetime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        String formattedDateTime = dateTime.format(formatter);

        //parsing house number and flat number to int
        try {
            houseNum = Integer.parseInt(request.getParameter("houseNum"));
            flatNum = request.getParameter("flatNum").equals("") ? 0 : Integer.parseInt(request.getParameter("flatNum"));
        } catch(NumberFormatException ex) {
            session.setAttribute("error", "invalid House Number or Flat Number value");
            response.sendRedirect("proposeReport");
            return;
        }

        //filling address fields
        Address address = new Address();
        address.setCountry(request.getParameter("country"));
        address.setCity(request.getParameter("city"));
        address.setStreet(request.getParameter("street"));
        address.setHouseNum(houseNum);
        address.setHouseNum(flatNum);

        //filling event fields
        Event event = new Event();
        event.setAddress(address);
        event.setEventDate(formattedDateTime);
        event.setSpeaker((Person)session.getAttribute("loggedUser"));
        event.setTitle(title);

        try {
            insertedId = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getEventDAO().insertProposedReport(event);
        } catch (SQLException e) {
            session.setAttribute("error", "Add new report was failed");
            response.sendRedirect("proposeReport");
            return;
        }

        if(insertedId == 0) {
            session.setAttribute("error", "Report Propose was not added");
        }

        response.sendRedirect("proposeReport");
    }
}

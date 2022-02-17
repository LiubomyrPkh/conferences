package com.epam.conferences.servlets;

import com.epam.conferences.DAO.DAOFactory;
import com.epam.conferences.DAO.entityData.Person;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        Person loggedUser = (Person) session.getAttribute("loggedUser");
        String error = (String) session.getAttribute("error");

        if(error != null) {
            request.setAttribute("error", error);
            session.removeAttribute("error");
        }

        if(loggedUser != null) {
            response.sendRedirect("profile");
        } else {
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Person user = new Person();
        user.setEmail(request.getParameter("email"));
        HttpSession session =  request.getSession();

        List<Person> usersList = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getPersonDAO().selectPersonList(user);

        if(usersList.isEmpty() || !BCrypt.checkpw(request.getParameter("password"), usersList.get(0).getPassword())) {
            session.setAttribute("error", "Wrong email or password");
        } else {
            session.setAttribute("loggedUser", usersList.get(0));
        }

        response.sendRedirect("login");
    }
}

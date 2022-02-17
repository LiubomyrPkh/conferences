package com.epam.conferences.servlets;

import com.epam.conferences.DAO.DAOFactory;
import com.epam.conferences.DAO.entityData.Person;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/registration")
public class RegisterServlet extends HttpServlet {
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
            request.getRequestDispatcher("/WEB-INF/views/registration.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Person user = new Person();
        user.setEmail(request.getParameter("email"));
        HttpSession session = request.getSession();

        //email already exist in database
        if(!DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getPersonDAO().selectPersonList(user).isEmpty()) {
            session.setAttribute("error", "This email already exist");
            return;
        }

        //passwords are not equals
        if(!request.getParameter("password").equals(request.getParameter("passwordRepeat"))) {
            session.setAttribute("error", "Passwords are not equal");
            return;
        }

        user.setPassword(BCrypt.hashpw(request.getParameter("password"), BCrypt.gensalt(10)));
        user.setName(request.getParameter("name"));
        user.setSurname(request.getParameter("surname"));
        user.setRole(Person.Role.USER.getRole());

        int insertedId = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getPersonDAO().insertPerson(user);

        if(insertedId == 0) {
            //insert new user error
            request.getSession().setAttribute("error", "New user cannot be created");
        } else {
            request.getSession().setAttribute("loggedUser", user);
        }

        response.sendRedirect("registration");
    }
}

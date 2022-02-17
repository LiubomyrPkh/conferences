package com.epam.conferences.servlets;

import com.epam.conferences.DAO.DAOFactory;
import com.epam.conferences.DAO.entityData.Event;
import com.epam.conferences.DAO.entityData.Person;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/events")
public class EventsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String eventDateParam = request.getParameter("date");
        String sort = request.getParameter("sort");
        HttpSession session = request.getSession(false);
        Person user = (Person) session.getAttribute("loggedUser");
        String error = (String) session.getAttribute("error");

        //eventItem page
        if(eventDateParam != null) {
            try{

                if(error != null) {
                    request.setAttribute("error", error);
                    session.removeAttribute("error");
                    request.getRequestDispatcher("/WEB-INF/views/eventItem.jsp").forward(request, response);
                    return;
                }

                Event criteria = new Event();
                criteria.setEventDate(eventDateParam);
                List<Event> events;

                if(user.getRole().equals(Person.Role.USER.getRole())) {
                    events = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getEventDAO().selectEventList(criteria);
                } else {
                    events = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getEventDAO().selectAllEventsByDate(eventDateParam);
                }

                if(events.isEmpty()) {
                    throw new IllegalArgumentException("events with date " + eventDateParam + " does not exist");
                }

                //events and event description
                String eventDescription = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getEventDAO().getEventDescription(events.get(0).getEventId());
                request.setAttribute("events", events);
                request.setAttribute("eventDescription", eventDescription);

                //event moderators names in string
                String moderators = events.stream().map(Event::getModerator).filter(Objects::nonNull).distinct().collect(Collectors.toList()).stream()
                        .map(Person::toString).collect(Collectors.joining(", "));
                request.setAttribute("moderators", moderators);

                //additional attributes for every role
                if(user.getRole().equals(Person.Role.USER.getRole())) {
                    //is this user already joined to event
                    Map<Event, Integer> eventsMap = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getUserEventDAO().getEventsByUserId(user.getPersonId());
                    boolean isUserJoined = eventsMap.entrySet().stream().anyMatch(x -> x.getKey().getEventDate().equals(eventDateParam));
                    request.setAttribute("isJoined", isUserJoined);

                } else if(user.getRole().equals(Person.Role.SPEAKER.getRole())) {
                    //is these events have free position for speaker
                    request.setAttribute("hasNullSpeakers", events.stream().anyMatch(x -> x.getSpeaker() == null));

                } else if(user.getRole().equals(Person.Role.MODERATOR.getRole())) {
                    //is any of these events was created by this moderator
                    request.setAttribute("hasAnyEventCreatedByModerator", events.stream().filter(x -> x.getModerator() != null).anyMatch(x -> x.getModerator().equals(user)));

                }

                request.getRequestDispatcher("/WEB-INF/views/eventItem.jsp").forward(request, response);

            } catch(IllegalArgumentException | SQLException ex) {
                //if param date was invalid
                request.getRequestDispatcher("/WEB-INF/views/events.jsp").forward(request, response);
            }
        } else {
            //sorting events by sort param
            List<Event> events = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getEventDAO().selectEventList(null);

            if(sort != null) {

                //events?sort=joined   (get user's joined events)
                if (sort.equals("joined") && user.getRole().equals(Person.Role.USER.getRole())) {
                    events = new ArrayList<>(DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getUserEventDAO()
                            .getEventsByUserId(user.getPersonId()).keySet());
                }

                //events?sort=myReports   (get speaker's reports)
                if(sort.equals("myReports") && user.getRole().equals(Person.Role.SPEAKER.getRole())) {
                    Event criteria = new Event();
                    criteria.setSpeaker(user);
                    events = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getEventDAO().selectEventList(criteria);
                }

                //events?sort=freeReports    (get all free reports for speakers)
                if(sort.equals("freeReports") && user.getRole().equals(Person.Role.SPEAKER.getRole())) {
                    try {
                        events = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getEventDAO().selectFreeReportList();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                //events?sort=moderatorsEvents    (get moderator's events)
                if(sort.equals("moderatorsEvents") && user.getRole().equals(Person.Role.MODERATOR.getRole())) {
                    Event criteria = new Event();
                    criteria.setModerator(user);
                    events = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getEventDAO().selectEventList(criteria);
                }

                //event?sort=proposedEvents
                if(sort.equals("proposedEvents") && user.getRole().equals(Person.Role.MODERATOR.getRole())) {
                    try {
                        events = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getEventDAO().selectAllEventsWithNullModerator();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }

            Map<String, Integer> usersCountMap = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getUserEventDAO().getUsersCountInEvent();
            Map<List<Event>, Integer> eventsMap = events.stream().collect(Collectors.groupingBy(Event::getEventDate)).entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getValue, s -> usersCountMap.get(s.getKey()) == null ? 0 : usersCountMap.get(s.getKey())));
            request.setAttribute("eventsMap", eventsMap);
            request.getRequestDispatcher("/WEB-INF/views/events.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String eventDateParam = request.getParameter("date");
        String selectedReportId = request.getParameter("selectedReport");
        HttpSession session = request.getSession(false);
        Person user = (Person) session.getAttribute("loggedUser");
        int userId = user.getPersonId();

        //post request from user
        if(user.getRole().equals(Person.Role.USER.getRole())) {

            int eventDateId = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getEventDAO().getEventDateId(eventDateParam);

            Map<Event, Integer> eventsMap = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getUserEventDAO().getEventsByUserId(userId);
            boolean isUserJoined = eventsMap.entrySet().stream().anyMatch(x -> x.getKey().getEventDate().equals(eventDateParam));

            if(isUserJoined) {
                DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getUserEventDAO().deleteUserEvent(userId, eventDateId);
            } else {
                DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getUserEventDAO().insertUserEvent(userId, eventDateId);
            }

            response.sendRedirect("events?date=" + eventDateParam);

            //post request from speaker
        } else if(user.getRole().equals(Person.Role.SPEAKER.getRole())) {
            List<Event> selectedFreeReportList = null;

            try {
                selectedFreeReportList = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getEventDAO()
                        .selectFreeReportList().stream().filter(x -> x.getEventDate()
                                .equals(eventDateParam)).collect(Collectors.toList());

            } catch (SQLException e) {
                e.printStackTrace();
            }

            if(selectedFreeReportList != null && !selectedFreeReportList.isEmpty()) {

                Event event = selectedFreeReportList.get(0);
                event.setSpeaker(user);
                boolean success = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getEventDAO().updateEvent(event);

                if(!success) {
                    session.setAttribute("error", "free report was not added");
                }

            } else {
                session.setAttribute("error", "free report was not found");
            }

            response.sendRedirect("events?sort=freeReports");

            //post request from moderator
        } else if(user.getRole().equals(Person.Role.MODERATOR.getRole())) {
            Event criteria = new Event();
            List<Event> selectedModeratorEvents = null;

            try {
                criteria.setEventId(Integer.parseInt(selectedReportId));
                selectedModeratorEvents = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getEventDAO().selectEventList(criteria);

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            //if event id correct
            if(selectedModeratorEvents != null && !selectedModeratorEvents.isEmpty()) {

                Event event = selectedModeratorEvents.get(0);

                //converting event string date to correct format for input tag (type = datetime-local)
                try {
                    Date date = new SimpleDateFormat("dd.MM.yy HH:mm").parse(event.getEventDate());
                    LocalDateTime ldt = new java.sql.Timestamp(date.getTime()).toLocalDateTime();
                    event.setEventDate(ldt.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                session.setAttribute("event", event);

            } else {
                session.setAttribute("error", "event was not found");
            }

            response.sendRedirect("editEvent");
        }

    }
}

package com.epam.conferences.DAO.entityData;

import java.io.Serializable;
import java.util.Objects;

public class Event implements Serializable {
    private int eventId;
    private Person moderator;
    private String eventDate;
    private Address address;
    private Person speaker;
    private String title;

    public Event(int eventId, Person moderator, String eventDate,
                 Address address, Person speaker, String title) {
        this.eventId = eventId;
        this.moderator = moderator;
        this.eventDate = eventDate;
        this.address = address;
        this.speaker = speaker;
        this.title = title;
    }

    public Event(Person moderator, String eventDate,
                 Address address, Person speaker, String title) {
        this.moderator = moderator;
        this.eventDate = eventDate;
        this.address = address;
        this.speaker = speaker;
        this.title = title;
    }

    public Event() {
        this.eventId = 0;
        this.moderator = null;
        this.eventDate = null;
        this.address = null;
        this.speaker = null;
        this.title = null;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public Person getModerator() {
        return moderator;
    }

    public void setModerator(Person moderator) {
        this.moderator = moderator;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Person getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Person speaker) {
        this.speaker = speaker;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(moderator, event.moderator) &&
                Objects.equals(eventDate, event.eventDate) &&
                Objects.equals(address, event.address) &&
                Objects.equals(speaker, event.speaker) &&
                Objects.equals(title, event.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moderator, eventDate, address, speaker, title);
    }
}

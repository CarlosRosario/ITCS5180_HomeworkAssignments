package com.group26.ticketreservation;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by meredithbrowne on 2/6/16.
 */
public class ticket implements Serializable {
    String name;
    String source;
    String destination;
    String departure_date;
    String departure_time;
    String return_date;

    public boolean isTripIsOneWay() {
        return tripIsOneWay;
    }

    public void setTripIsOneWay(boolean tripIsOneWay) {
        this.tripIsOneWay = tripIsOneWay;
    }

    public String getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(String departure_date) {
        this.departure_date = departure_date;
    }

    String return_time = null;
    boolean tripIsOneWay = true;

    public ticket(String name, String source, String destination, String departure_date, String departure_time,  boolean tripIsOneWay,  String return_date, String return_time) {
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.departure_date = departure_date;
        this.departure_time = departure_time;
        this.tripIsOneWay = tripIsOneWay;
        this.return_date = return_date;
        this.return_time = return_time;
    }

    public ticket(String name, String source, String destination, String departure_date, String departure_time, boolean tripIsOneWay) {
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.departure_date = departure_date;
        this.departure_time = departure_time;
        this.tripIsOneWay = tripIsOneWay;
        this.return_date = "";
        this.return_time = "";
    }

    @Override
    public String toString() {
        return "ticket{" +
                "name='" + name + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", departure_date='" + departure_date + '\'' +
                ", departure_time='" + departure_time + '\'' +
                ", return_date='" + return_date + '\'' +
                ", return_time='" + return_time + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getReturn_date() {
        return return_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }

    public String getReturn_time() {
        return return_time;
    }

    public void setReturn_time(String return_time) {
        this.return_time = return_time;
    }
}
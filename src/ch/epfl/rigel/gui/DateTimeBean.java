package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

//part of the MODEL of the MVC pattern
// this class is a JavaFX bean, it represents a mutable version of the ZonedDateTime class
public final class DateTimeBean {

    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private ObjectProperty<LocalTime> time = new SimpleObjectProperty<>();
    private ObjectProperty<ZoneId> zone = new SimpleObjectProperty<>();

    public DateTimeBean(){

    }

    /**
     *
     * @param date
     * @param time
     * @param zone
     */
    public DateTimeBean(ObjectProperty<LocalDate> date, ObjectProperty<LocalTime> time, ObjectProperty<ZoneId> zone) {
        this.date = new SimpleObjectProperty(date);
        this.time = new SimpleObjectProperty(time);
        this.zone = new SimpleObjectProperty(zone);
    }

    /**
     *
     * @return dateProperty object
     */
    public ObjectProperty<LocalDate> dateProperty(){
        return this.date;
    }

    /**
     *
     * @return contents of dateProperty
     */
    public LocalDate getDate(){
        return date.get();
    }

    /**
     *
     * @return contents of timeProperty
     */
    public LocalTime getTime() {
        return time.get();
    }

    /**
     *
     * @return timeProperty object
     */
    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    /**
     *
     * @return contents of zoneProperty
     */
    public ZoneId getZone() {
        return zone.get();
    }

    /**
     *
     * @return zoneProperty object
     */
    public ObjectProperty<ZoneId> zoneProperty() {
        return zone;
    }

    /**
     *
     * @param date
     */
    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    /**
     *
     * @param time
     */
    public void setTime(LocalTime time) {
        this.time.set(time);
    }

    /**
     *
     * @param zone
     */
    public void setZone(ZoneId zone) {
        this.zone.set(zone);
    }

    /**
     *
     * @return zonedatetime instance
     */
    public ZonedDateTime getZonedDateTime(){
        return  ZonedDateTime.of(this.getDate(),this.getTime(),this.getZone());
    }

    /**
     * sets current instance to have same properties as the setterTime argument
     * @param setterTime
     */
    public void setZonedDateTime(ZonedDateTime setterTime){
        this.setDate(setterTime.toLocalDate());
        this.setTime(setterTime.toLocalTime());
        this.setZone(setterTime.getZone());
    }
}

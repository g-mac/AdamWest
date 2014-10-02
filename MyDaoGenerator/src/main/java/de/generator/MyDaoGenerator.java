package de.generator;

import de.greenrobot.daogenerator.*;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "de.adamwest.database");
        addEnititys(schema);

        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }

    public static void addEnititys(Schema schema) {
        Entity route = schema.addEntity("Route");
        route.addIdProperty();
        route.addStringProperty("name").notNull();
        route.addDateProperty("createdAt").notNull();
        route.addStringProperty("description");

        Entity location = schema.addEntity("RouteLocation");
        location.addIdProperty();
        location.addDoubleProperty("latitude");
        location.addDoubleProperty("longitude");
        location.addDateProperty("createdAt");


        Entity event = schema.addEntity("Event");
        event.addIdProperty();
        event.addStringProperty("name");
        event.addStringProperty("description");

        Entity multimediaElement = schema.addEntity("MultimediaElement");
        multimediaElement.addIdProperty();
        multimediaElement.addStringProperty("type");
        multimediaElement.addStringProperty("path");
        multimediaElement.addStringProperty("description");
        multimediaElement.addDateProperty("createdAt");

        Entity holiday = schema.addEntity("Holiday");
        holiday.addIdProperty();
        holiday.addStringProperty("name");
        holiday.addStringProperty("description");
        holiday.addDateProperty("createdAt");

        Entity activeHoliday = schema.addEntity("ActiveHoliday");
        activeHoliday.addStringProperty("active").primaryKey();

        //create Relations
        Property locationId = event.addLongProperty("locationId").getProperty();
        event.addToOne(location, locationId);

        Property routeId = location.addLongProperty("routeId").getProperty();
        route.addToMany(location, routeId);

        Property routeIdForEvent = event.addLongProperty("routeId").getProperty();
        route.addToMany(event, routeIdForEvent);

        Property eventId = multimediaElement.addLongProperty("eventId").getProperty();
        event.addToMany(multimediaElement, eventId);

        Property holidayId = route.addLongProperty("holidayId").getProperty();
        holiday.addToMany(route, holidayId);

        Property currentRoute = holiday.addLongProperty("currentRouteId").getProperty();
        holiday.addToOne(route, currentRoute);

        Property activeHolidayId = activeHoliday.addLongProperty("holidayId").getProperty();
        activeHoliday.addToOne(holiday, activeHolidayId);
    }
}

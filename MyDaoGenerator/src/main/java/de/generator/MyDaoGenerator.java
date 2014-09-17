package de.generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "de.adamwest.database");
        addEnititys(schema);

        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }

    public static void addEnititys(Schema schema) {
        Entity route = schema.addEntity("Route");

        route.addIdProperty();

        route.addStringProperty("Name").notNull();
        route.addDateProperty("Created_at").notNull();
        route.addStringProperty("Description");

        Entity location = schema.addEntity("Location");
        Property locationId = location.addIdProperty().getProperty();
        location.addLongProperty("Latitude");
        location.addLongProperty("Longitude");
        location.addLongProperty("Timestamp");

        route.addToMany(location, locationId, "Locations");

        Entity event = schema.addEntity("Event");
        Property eventId = event.addIdProperty().getProperty();

        event.addStringProperty("Name");
        //event.addToOne(location, locationId, "Location");
        event.addStringProperty("Description");

        Entity multimediaElement = schema.addEntity("MultimediaElement");
        Property multimediaId = multimediaElement.addIdProperty().getProperty();
        multimediaElement.addStringProperty("Type");
        multimediaElement.addStringProperty("Path");

        event.addToMany(multimediaElement, multimediaId, "MultimediaElements");

        route.addToMany(event, eventId, "Events");

    }
}

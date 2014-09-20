package de.adamwest.database;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ROUTE_LOCATION.
 */
public class RouteLocation {

    private Long id;
    private Double latitude;
    private Double longitude;
    private java.util.Date createdAt;
    private Long routeId;

    public RouteLocation() {
    }

    public RouteLocation(Long id) {
        this.id = id;
    }

    public RouteLocation(Long id, Double latitude, Double longitude, java.util.Date createdAt, Long routeId) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = createdAt;
        this.routeId = routeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

}

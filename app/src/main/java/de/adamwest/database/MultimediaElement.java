package de.adamwest.database;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table MULTIMEDIA_ELEMENT.
 */
public class MultimediaElement {

    private Long id;
    private String Type;
    private String Path;

    public MultimediaElement() {
    }

    public MultimediaElement(Long id) {
        this.id = id;
    }

    public MultimediaElement(Long id, String Type, String Path) {
        this.id = id;
        this.Type = Type;
        this.Path = Path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String Path) {
        this.Path = Path;
    }

}

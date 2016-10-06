package com.fosterstory.bean;


public class Search {

    private String name = null;
    private Integer typeId = null;
    private Integer breedId = null;
//    private Locale locale = new Locale();
    private Integer id = null;

    // TODO: 10/4/16 Add search byDate, other advance features. 

    public Search() {
    }

//    public Search(String name, Integer typeId, Integer breedId, Locale locale, Integer id) {
    public Search(String name, Integer typeId, Integer breedId, Integer id) {
        this.name = name;
        this.typeId = typeId;
        this.breedId = breedId;
//        this.locale = locale;
        this.id = id;
    }

    public void setName(String name) {
        // if the provided name is "" then set to null
        this.name = (name == null || name.equals("") ? null : name);
    }

    public String getNameForSearch() {
        return name == null || name.equals("") ? "" : "%" + name + "%";
    }

    public String getName() {
        return name;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getBreedId() {
        return breedId;
    }

    public void setBreedId(Integer breedId) {
        this.breedId = breedId;
    }

/*
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

package com.template.project.listobjects;

public class AdapterRowHeader extends AdapterBaseRow {

    private long id;
    private String title;

    public AdapterRowHeader(long id, String title, int firstSectionPosition) {
        super(firstSectionPosition);
        this.id = id;
        this.title = title;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
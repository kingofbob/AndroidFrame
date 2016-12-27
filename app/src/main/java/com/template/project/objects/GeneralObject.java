package com.template.project.objects;

/**
 * Created by 00020443 on 17/10/2016.
 */

public class GeneralObject {
    int id;
    String title;

    public GeneralObject(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

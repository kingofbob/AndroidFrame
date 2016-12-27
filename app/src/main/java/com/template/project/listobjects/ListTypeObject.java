package com.template.project.listobjects;

/**
 * Created by 00020443 on 24/11/2016.
 */

public class ListTypeObject {
    public static final int TYPE_ARROW = 0;
    public static final int TYPE_SWITCH = 1;
    long id;
    String title;
    boolean toggle;
    int TYPE;
    String values;


    public ListTypeObject(long id, String title, boolean toggle, int TYPE, String values) {
        this.id = id;
        this.title = title;
        this.toggle = toggle;
        this.TYPE = TYPE;
        this.values = values;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
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

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }


    public boolean isToggle() {
        return toggle;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }
}

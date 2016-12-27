package com.template.project.listobjects;

/**
 * Created by 00020443 on 5/12/2016.
 */

public class NavigationRowObject {

    public static final int TYPE_WITH_ICON = 0;
    public static final int TYPE_TITLE = 1;
    public static final int TYPE_DIVIDER = 2;
    public static final int TYPE_HEADER = 3;

    int id;
    String title;
    int drawble;
    boolean visible;

    int TYPE;
    String imgURL;

    public NavigationRowObject(int id, String title, int drawble, boolean visible, int TYPE) {
        this.id = id;
        this.title = title;
        this.drawble = drawble;
        this.visible = visible;
        this.TYPE = TYPE;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
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

    public int getDrawble() {
        return drawble;
    }

    public void setDrawble(int drawble) {
        this.drawble = drawble;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }
}

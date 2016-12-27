package com.template.project.listobjects;

import android.widget.ImageView;

/**
 * Created by 00020443 on 10/11/2016.
 */

public class CommonStickyListObject {
    long id;
    String title;
    String subTitle;
    String hint;
    ImageView image;
    boolean isHeader;
    boolean isVisible;

    int sectionFirstPosition;

    public CommonStickyListObject(long id, String title, String subTitle, String hint, ImageView image, boolean isHeader, boolean isVisible, int sectionFirstPosition) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.hint = hint;
        this.image = image;
        this.isHeader = isHeader;
        this.isVisible = isVisible;
        this.sectionFirstPosition = sectionFirstPosition;
    }

    public int getSectionFirstPosition() {
        return sectionFirstPosition;
    }

    public void setSectionFirstPosition(int sectionFirstPosition) {
        this.sectionFirstPosition = sectionFirstPosition;
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

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}

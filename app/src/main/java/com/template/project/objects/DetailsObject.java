package com.template.project.objects;

/**
 * Created by 00020443 on 15/9/2016.
 */
public class DetailsObject {
    String left;
    String right;

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public DetailsObject(String left, String right) {
        this.left = left;
        this.right = right;
    }
}

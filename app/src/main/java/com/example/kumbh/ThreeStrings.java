package com.example.kumbh;

public class ThreeStrings {
    private String left;
    private String right;
    private String centre;
    private String x;

    public ThreeStrings(String left, String right, String centre, String x) {
        this.left = left;
        this.right = right;
        this.centre = centre;
        this.x =x;
    }

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }

    public String getCentre() {
        return centre;
    }

    public String getX() {
        return x;
    }
}

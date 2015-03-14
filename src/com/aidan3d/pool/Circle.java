package com.aidan3d.pool;

import math.geom2d.Vector2D;

/**
 * Simply a circle defined by a center and a radius.
 * 
 */
public class Circle
{
    private Vector2D center;
    private double radius;
    
    public Circle(Vector2D obj, double r) {
        center = obj;
        radius = r;
    }
    
    public void setCenter(Vector2D c) {
        center = c;
    }
    
    public void setRadius(double r) {
        radius = r;
    }
    
    public Vector2D getCenter() {
        return center;
    }
    
    public double getRadius() {
        return radius;
    }
}
package com.aidan3d.pool;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;

/**
 * A pair of 2d Euclidian vectors representing a line
 * (think of a line as a point "running" between two
 * terminal "posts" from the "start" (or beginning point)
 * to the "end" (or end-point)).
 */
public class Line
{
    private Vector2D start;  // the line's point of origin
    private Vector2D end;    // the line's end-point.

    Line(Point2D s, Point2D e) {
        start = new Vector2D(s);
        end = new Vector2D(e);
    }

    public void setStart(Vector2D s) {
        start = s;
    }

    public void setEnd(Vector2D e) {
        end = e;
    }
    
    public Vector2D getStart() {
        return start;
    }
    
    public Vector2D getEnd() {
        return end;
    }
}
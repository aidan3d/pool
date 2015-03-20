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
    private Vector2D start;                         // The line's point of origin
    private Vector2D end;                           // The line's end-point.

    private Vector2D vector;                        // A Vector2D representation
                                                    // of this Line object
    
    private Vector2D normalDirection;               // The normal of lineAsVector


    Line( Point2D s, Point2D e )
    {
        start = new Vector2D( s );
        end = new Vector2D( e );
        vector = new Vector2D();
        normalDirection = new Vector2D();

        // Let's work out the 2d Eulidian vector
        // representation of this Line object.
        vector = calcVector();
        
        // Useful to have the vector's normal
        // unit vector for collision detection.
        normalDirection = calcNormalDirection();
    }


    public void setStart( Vector2D s )
    {
        start = s;
    }


    public void setEnd( Vector2D e )
    {
        end = e;
    }


    public Vector2D getStart()
    {
        return start;
    }


    public Vector2D getEnd()
    {
        return end;
    }

    public Vector2D getNormalDirection()
    {
        return normalDirection;
    }

    /**
     * Send out a 2d=Euclidan=vector representation
     * of this Line object.
     * @return the Vector2D object representing this
     * Line object
     */
    public Vector2D getVector()
    {
        return vector;
        
    } // end method getVector
 

    /**
     * Send out a Vector2D object (a
     * 2d Euclidian vector) for use in
     * collision detection.
     */
    private Vector2D calcVector()
    {   
        Vector2D holdVector;
        
        // Assign the 2d Euclidian vector
        // joining this Lines object's start
        // point to end point.
        holdVector = end.minus( start );
        
        return holdVector;
        
    } // end method calcLineAsVector
    
    /**
     * This method yields the 2d Euclidian vector
     * representing the Line object's "normal"
     * i.e., the vector orthogonal to the line's
     * vector.
     */
    private Vector2D calcNormalDirection()
    {  
        Vector2D holdNormal;                        // Will hold the
                                                    // direction of
                                                    // the normal to
                                                    // this Line object
                        
        
        // Effectively rotates the Line as vector
        // by 90 degrees.
        holdNormal = new Vector2D( vector.y()*-1, vector.x() );
        
        // Send home the unit vector.
        return holdNormal.normalize();
        
    }

} //end class Line
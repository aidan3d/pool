/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aidan3d.pool;

import java.awt.Color;
import java.awt.Graphics;
import math.geom2d.Vector2D;


/**
 * This is the Ball class's default constructor.
 * This class is effectively the Ball "sprite."
 * 
 */
class Ball
{
    private int radius;                             // The ball's visual radius
    private String id;
    private double mass;
    private Vector2D displacement;
    private Vector2D velocity;
    private boolean pocketed;                       // A signal flag: raised
                                                    // if the ball is "sunk"
    private Color color;


    /**
     * No-argument constructor.
     */
    public Ball()
    {
        radius = 0;
        id = "";
        mass = 0.0F;
        displacement = new Vector2D();
        velocity = new Vector2D();
        pocketed = false;
        color = Color.white;
    }


    /**
     *  Five-argument constructor.
     */
    public Ball(int r, String name, double m, Vector2D d, Color c)
    {
        radius = r;
        id = name;
        mass = m;
        displacement = d;
        color = c;

    } // end five-argument constuctor


    /**
     * This method draws the ball to screen.
     * @param dbg the baize to draw on 
     */
    public void draw( Graphics dbg )
    {
        dbg.setColor( color );            
        dbg.fillOval( ( int )displacement.getX(), ( int )displacement.getY(), radius, radius );

    } // end method draw

    /**
     * This method retrieves the value referred to
     * by the Vector2D object "displacement."
     */
    public Vector2D getDisplacement()
    {
        return displacement;
        
    } // end method getDisplacement
    

    /**
     * This is the traction method for the Ball object.
     */
    public void move()
    {
        // Velocity is effectively the number of
        // pixels by which the Ball object will
        // move when this function is called for
        // in each gameUpdate cycle (i.e., px is
        // a great start).
        displacement = displacement.plus(velocity);

    } // end method move

} // end class Ball

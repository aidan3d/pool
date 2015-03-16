/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aidan3d.pool;

import java.awt.Color;
import math.geom2d.Vector2D;


/**
 * This is the Ball class's default constructor.
 * This class is effectively the Ball "sprite."
 * 
 */
class Ball
{
    private boolean moving;                         // A signal flag: raised
                                                    // if the ball is in motion

    private boolean pocketed;                       // A signal flag: raised
                                                    // if the ball is "sunk"
    
    private int radius;                             // The ball's visual radius

    private double mass;                            // Needed for collision
                                                    // detection (k.e.)

    private String id;                              // A human-readable label

    private Vector2D displacement;

    private Vector2D velocity;

    private Color color;


    /**
     * No-argument constructor.
     */
    public Ball()
    {
        moving = false;
        pocketed = false;
        radius = 0;
        mass = 0.0F;
        id = "";
        displacement = new Vector2D();
        velocity = new Vector2D();
        color = Color.white;
    }


    /**
     *  Five-argument constructor.
     */
    public Ball(int r, double m, String name, Vector2D d, Color c)
    {
        radius = r;
        mass = m;
        id = name;
        displacement = d;
        pocketed = false;
        color = c;

    } // end five-argument constuctor


    /**
     * This "getter" retrieves the ball's color,
     * stored in the instance variable "color."
     * @return the value referred to by the instance variable "color"
     */
    public Color getColor()
    {
        return color;

    } // end method getColor


    /**
     * This method retrieves the value referred to
     * by the Vector2D object "displacement."
     * @return the <b>Vector2D</b> object referred to by the instance variable "displacement
     * @see math.geom2d.Vector2D
     */
    public Vector2D getDisplacement()
    {
        return displacement;
        
    } // end method getDisplacement
    

    /**
     * 
     * @return the value referred to by the instance variable
     * "displacement" (a <b>Vector2D</b> object)
     * @see math.geom2d.Vector2D
     */
    public double getRadius()
    {
        return radius;
        
    } // end method getRadius


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

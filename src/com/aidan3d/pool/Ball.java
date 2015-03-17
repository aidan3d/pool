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
class Ball extends Circle
{
    private boolean moving;                         // A signal flag: raised
                                                    // if the ball is in motion

    private boolean pocketed;                       // A signal flag: raised
                                                    // if the ball is "sunk"
    
    private boolean hit;                            // A signal falg: raised
                                                    // when the ball detects
                                                    // a foreign object in
                                                    // its "airspace" or
                                                    // collision envelope
    

    private double mass;                            // Needed for collision
                                                    // detection (k.e.)

    private String id;                              // A human-readable label

    private Color color;


    /**
     * No-argument constructor.
     */
    public Ball()
    {
        super(new Vector2D(0.0, 0.0 ), 0.0, 0.0 );
        moving = false;
        pocketed = false;
        mass = 0.0F;
        id = "";
        color = Color.white;
    }


    /**
     *  Five-argument constructor.
     */
    public Ball(int r, double m, String name, Vector2D d, Color c)
    {
        super( d, r, m );
        moving = false;
        pocketed = false;
        mass = m;
        id = name;
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
     * Let's find out if any of the balls
     * are in motion (the calling object
     * uses this as a flag to stop
     * updating and run the next "shot."
     * @return 
     */
    public boolean isMoving()
    {
        return moving;
    }
    
    /**
     * Once  a balls "state" is pocketed,
     * it stays that way for the rest of
     * the game.
     */
    public boolean isPocketed()
    {
        boolean status = false;  // A signal flag, raised when either
                                 // a ball <-> pocket or a ball <->
                                 // cushion-rail collision have been
                                 // flagged
        
        // Check whether the ball is in play
        // on very call to the ball's upate()
        // method on each loop of the physics
        // engine.
        if ( pocketed )
        {
            status = true;
        }
        
        return status;
        
    } // end isPocketed


    /**
     * This is the traction method for the Ball object.
     * Rolls in after the ball's update() method to
     * place the ball in new place in the game window.
     */
    @Override
    public void move()
    {
        // Velocity is effectively the number of
        // pixels by which the Ball object will
        // move when this function is called for
        // in each gameUpdate cycle (i.e., px is
        // a great start).
        center = center.plus(velocity);

    } // end method move


    @Override
    public String toString()
    {
                return id;
    
    } //end method toString

} // end class Ball

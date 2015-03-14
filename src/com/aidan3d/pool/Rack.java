/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aidan3d.pool;

import java.util.ArrayList;

/**
 * A Rack class sets up and manages a rack
 * of fifteen pool balls and the cue ball (we need
 * all Ball objects to interact).
 */
public class Rack
{
    ArrayList<Ball> ballsList;
    
    public Rack()
    {
        // Initialize the list of Ball objects.
        ballsList = new ArrayList();

    } // end no-argument constructor
    
} // end class Rack

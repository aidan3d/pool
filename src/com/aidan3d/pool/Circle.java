package com.aidan3d.pool;

import math.geom2d.Vector2D;

/**
 * A Circle object is defined by its center (a 2d
 * Euclidian vector) and a radius (a double).
 * Mass and velocity are properties we use for
 * collision detection with other <b>Circle</b>
 * objects and w <b>Line</b> objects.
 */
public class Circle
{
    protected Vector2D center;
    protected Vector2D velocity;            // We do some motion-related
                                            // collision detection, and
                                            // this may be handy...
    protected double radius;
    protected double mass;                  // ...as may this


    /**
     * The two-argument constructor.
     * @param obj - the circle's center in 2d
     * Euclidian vector form (i.e. a <b>Vector2D</b> object)
     * @param r  - the circle's radius
     * @see math.geom2d.Vector2D
     */
    public Circle( Vector2D obj, double r, double m )
    {
        center = obj;
        radius = r;
        velocity = new Vector2D( 0.0, 0.0 );
        mass = 0.0F;

    } //end two-argument constructor


    /**
     * 
     * @param c the circle's center
     */
    public void setCenter( Vector2D c )
    {
        center = c;

    } // end method setCenter


    public void setRadius( double r )
    {
        radius = r;

    } // end method setRadius


    public void setVelocity( Vector2D v )
    {
        velocity = v;
    
    } // end method setVelocity


    public Vector2D getCenter()
    {
        return center;

    } // end method getCenter


    public double getRadius()
    {
        return radius;

    } // end method getRadius


    public Vector2D getVelocity()
    {
        return velocity;

    } // end method getVelocity


    /**
     * 
     * @param enemy a Circle object, potentially impinging
     * on this Circle object's "airspace"
     * 
     * @return true if this circle has been collided
     * with by another
     */
    public boolean circleCircleCollision( Circle enemy )
    {  
        boolean underAttack = false;            // This signal flag is
                                                // raised if the current
                                                // Circle is under attack
        
        double threshold = 0.0F;                // the sum of the radii of
                                                // the current Circle and
                                                // the encroaching circle.

        double timeToReachCenter = 0.0F;        // Total journey time from
                                                // the neignbor's current
                                                // location


        // Run a thread from the current Circle
        // object to it's "detected" potential
        // incurrer (the Circle object referred
        // to by "enemy")
        Vector2D pathToEnemy = new Vector2D();


        // The "red carpet" or "golden thread" joining the
        // current Circle object with its  neigbor.
        pathToEnemy = center.minus( enemy.center );


        // Draw a line in the virtual sand ( mark the baize).
        threshold = (radius + enemy.radius );
        
        if ( pathToEnemy.norm() < threshold )
            underAttack = true;

        // Let the call know that we've been struck
        // by a Circle object!
        return underAttack;

    } // end method circleCircleCollision


    /**
     * Under our "reverse movement" model, the Line
     * object is closing in on the current Circle object
     * @param enemy - a Line object rapidly traveling
     * through space towards us
     * @return  true if this Circle object has
     * been collided with by a Line object
     */
    public boolean circleLineCollision( Line enemy )
    {
        boolean underAttack = false;  // A signal flag, raised if
                                      // theis Circle object has
                                      // a visitor within its
                                      // airpsace.

 
        // Get the length in the horizontal
        // direction.
        Vector2D pathToEnemy = enemy.getVector().minus( center );
        System.out.printf("Distance to cushion (%s): %f.%n", enemy.toString(), pathToEnemy.norm() );
        
        if ( ( enemy.getVector().angle() - pathToEnemy.angle() ) <  Math.asin( radius / pathToEnemy.norm() ))
        {
            underAttack = true;
        
        } // end if-then
        
        return underAttack;
        
    } // end method circleLineCollision
    
    
    /**
     * A Circle object has hit the current one.
     * @param obj the incurring Circle object
     * @return 
     */
    public boolean isHitByCircle( Circle obj )
    {
        boolean status = false;
        
        // Now, let's see if we've hit! 
        if ( circleCircleCollision( obj ) ) 
        {
            status = true;
            System.out.printf("%s has been HIT by %s%n", this.toString(), obj.toString() );

        } // end if-then
        
        return status;

    } // end method isHitByCircle
    
    
    /**
     * A Line object has hit the current
     * Circle object.
     * @param obj the marauding Line object
     * @return 
     */
    public boolean isHitByLine( Line obj )
    {
        boolean status = false;
        
        // Now, lets look for a strike by
        // a Line object!
        if ( circleLineCollision( obj ) )
        {
            status = true;
            System.out.printf( "%s has been HIT by a Line. %n", this.toString() );
        
        } // end if-then
        
        return status;
        
    } // end method isHitByLine
    
    
    /**
     *  Override this method in the subclass
     *  to move your Circle-derived object.
     */
    public void move()
    {
    }


    /**
     * Override this method in the subclass to
     * update the position of your Circle-derived
     * object.
     */
    public void update()
    {
    }

} //end class Circle

package com.aidan3d.pool;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;


/**
 * The Table class simulates a pool table. It has:<br>
 * &nbsp&nbsp a) &nbsp two horizontal "cushions," as
 * well as four vertical "cushions";<br>
 * &nbsp&nbsp b) &nbsp A pair of "jaws" (circular
 * "bumpers" representing the corners of the pockets/
 * at the ends of each rail/line segment.
 */
public class Table
{
    private final int xOrigin;                // The top-left origin point
                                              // of the pool table
    
    private final int yOrigin;                // In the calling object:
                                              // a poolGame panel

    private final int ballRadius;             // The radius of a pool ball
                                              // (i.e., 5.0 px)

    private final int tableSize;              // The width, and half of the
                                              // de[tj of the pool table's
                                              // playing surface; and half
                                              // of the height (two squares
                                              // maketh the baize-on-slate
                                              // surface complete)
                                              // i.e., 300.0 px wide would
                                              //       be 600.0 px deep

    private final double pocketMultiplier;    // The "multiplier" we use
                                              // to boost the ball's radius
                                              // into a quarter of the "gap"
                                              // between rails (the gap is
                                              // nominally the product of
                                              // two balls' diameters and
                                              // the "pocketMultiplier"
                                              // multiplier before the two
                                              // jaws' radii are subtracted
                                              // out)

    private final double jawMultiplier;       // The multiplier we use to
                                              // augment the "jaws," or
                                              // cushion "circles'" radii,
                                              // e.g., if we set the
                                              // "jawMultiplier" multiplier
                                              // to 0.5, then a cushion
                                              // circle's radius will be
                                              // 0.5 * ballRadius for the
                                              // radius of each "jaw"
                                              // (i.e., 0.5 * 5.0 = 2.5px
                                              // in our case)

    private final Color baize;                // The color of the bed's
                                              // playing-surface

    private final ArrayList<Line> walls;      // The horizontal and
                                              // vertical cushioned
                                              // "rails"

    private final ArrayList<Circle> jaws;     // The table's circular
                                              // "bumpers" placed at the
                                              // mouths of each pocket

    private final ArrayList<Circle> pockets;  // Pockets circles --
                                              // once a ball has
                                              // entered one of these
                                              // circles, its "pocketed"
                                              // flag is raised
    
    private final Rack balls;                 // All sixteen pool balls,
                                              // including the cue ball,
                                              // are in a list wrapped
                                              // in a Rack object

    /**
     * The Four-argument constructor.
     * @param x is the x-ordinate of the pool table's top-left origin
     * @param y is the y-ordinate of the pool table's top-left origin
     * @param t is the width of the table, as well as half of the length
     * (the pool table is comprised of two stacked squares of width "t")
     * @param r is the size of a pool ball
     * @param p is the pocketMultiplier multiplier
     * @param j is the jawMultiplier multiplier
     */
    public Table( int x, int y, int t, int r, double p, double j )
    {
        xOrigin = x;
        yOrigin = y;
        tableSize = t;
        ballRadius = r;
        pocketMultiplier = p;
        jawMultiplier = j;

        baize = new Color( 0, 0.2f, 0 );    // (0.0F, 0.392F, 0.078F):
                                            // baizeHSB = 120, 100, 20
                                            // (0/255, 51/255, 0/255):
                                            // baizeRGB = 0, 51, 0
        
        walls = new ArrayList<>();          // Loaded inside defineWalls(),
                                            // called by defineTable()
        jaws = new ArrayList<>();

        pockets = new ArrayList<>();        // An array of circles, with
                                            // centers and radii

        balls = new Rack();                 // All sixteen balls
        
        // Set up all six "cushion rails" and put circular "bumper"
        // cushions at the mouths of each "opening" or gap between
        // the rails.
        defineTable();

    } // end six-argument constructor


    private void defineJaws()
    {
        double jawRadius = ballRadius * jawMultiplier; 
        
        // Load up the jaws list ("bumpers" slimming down
        // the pockets), using the start and end points of
        // the line segments as tangent points for the
        // bumper circles
        for ( Line wall : walls )
        {
            // If the x-ordinate of the line segment's
            // END point is at the table's origin...
            if ( wall.getEnd().x()-xOrigin < tableSize/2 )  // x is very small
            {
                // ...we have one of the left-hand vertical
                // rails in our grasp! if the y-ordinate of
                // the found line's START point is greater
                // than the table's width divided by two,
                // then we have the bottom vertical rail...
                if ( wall.getStart().y()-yOrigin > tableSize/2 )
                {
                    // ...Bottom-left vertical rail it is!
                    // Subtract jawRadius (the jaw width)
                    // from the x-ordinate of the line's start
                    // start and end points; the y-ordinate
                    // remains untouched.
                    jaws.add(
                        new Circle( new Vector2D(
                        wall.getStart().x()-jawRadius, wall.getStart().y() ), jawRadius ) );
                    
                    jaws.add(
                        new Circle( new Vector2D(
                        wall.getEnd().x()-jawRadius, wall.getEnd().y() ), jawRadius ) );
                }
                else  // The top left-hand vertical rail
                {
                    jaws.add(
                        new Circle( new Vector2D(
                        wall.getStart().x()-jawRadius, wall.getStart().y()), jawRadius ) );
                    
                    jaws.add( new Circle( new Vector2D(
                        wall.getEnd().x()-jawRadius, wall.getEnd().y()), jawRadius ) );
                
                } // end nested if-then-else
            }

            // We have identified one of the right-
            // hand vertical rails, because the x-
            // component of the horizontal rail's
            // START oint is large.
            else if ( wall.getStart().x()-xOrigin > tableSize/2 )
            {
                // Let's get the top right-hand vertical rail first.
                if ( wall.getEnd().y()-yOrigin < tableSize/2 )
                {
                    // Create jaws at the current  wall's start and end points.
                    jaws.add( new Circle(
                        new Vector2D(
                        wall.getStart().x()+jawRadius, wall.getStart().y() ), jawRadius ) );
                    jaws.add(new Circle(
                        new Vector2D(
                        wall.getEnd().x()+jawRadius, wall.getEnd().y()), jawRadius));
                }

                // We are on the bottom right-hand vertical rail.
                else 
                { 
                    jaws.add( new Circle(
                        new Vector2D(
                        wall.getStart().x()+jawRadius, wall.getStart().y()), jawRadius ));
                    jaws.add( new Circle(new Vector2D(wall.getEnd().x()+jawRadius,
                        wall.getEnd().y()), jawRadius ) );
                
                } // end nested if-then-else
                
            // y is very small... so, we have the top
            // top horizontal rail (would haved used 
            // .y==0 if ints)
            }
            else if ( wall.getEnd().y()-yOrigin < ballRadius )
            {
                jaws.add( new Circle(
                    new Vector2D(
                    wall.getStart().x(), wall.getStart().y()-jawRadius ), jawRadius ) );
                jaws.add(new Circle(
                    new Vector2D(
                    wall.getEnd().x(), wall.getEnd().y()-jawRadius ), jawRadius ) );
            }

            // The default case: must have the bottom horizontal rail!
            else 
            {
                jaws.add( new Circle(
                    new Vector2D(
                    wall.getStart().x(), wall.getStart().y()+jawRadius), jawRadius ) );
                
                jaws.add(new Circle(
                    new Vector2D(
                    wall.getEnd().x(), wall.getEnd().y()+jawRadius), jawRadius ) );
            
            } // end if-then-else-if
            
        } // end for
        
    } // end method defineJaws


    /**
     * Create <b>Circle</b> objects for each pocket.
     */
    private void definePockets()
    {
        double pocketClearRadius = ballRadius * pocketMultiplier;  // The pocket's
                                                                   // radius (will
                                                                   // be fed into
                                                                   // the Ball
                                                                   // object)
        
        double jawRadius = ballRadius * jawMultiplier;
        
        double cornerJawRotationExtension = Math.pow( 2.0, -0.5 ) * // Allow for
                ( ballRadius * jawMultiplier );                     // poor
                                                                    // placement
                                                                    // of the
                                                                    // corner
                                                                    // jaws
        

        // Get the left-hand side-pocket.
        for ( Line wall : walls )
        {
            // Get the left-hand vertical rails. We have
            // either...
            if ( ( wall.getEnd().x() - xOrigin ) < ballRadius ) // x is very small (double)
            {
                if ( ( wall.getStart().y() - yOrigin ) > ( tableSize / 2 ) ) // ...the lowermost...
                {
                    // Place a Circle object at the top of the rail.
                    pockets.add( new Circle(
                        new Vector2D(
                        new Point2D( wall.getStart().x() - jawRadius,
                        wall.getStart().y() - pocketClearRadius ) ),
                        pocketClearRadius - jawRadius ) );

                } // end nested if-then

            } // end first if-then
            
            // Now let's take a look at the right-handpair of vertical rails.
            else if ( ( wall.getStart().x() - xOrigin ) > tableSize - ballRadius )
            {
                // We're looking for the bottommost right-hand vertical rail.
                if ( ( wall.getStart().y() - yOrigin) > ( tableSize / 2 ) )
                {
                    // Place a Circle object at the top of the
                    // right-hand bottommost vertical rail.
                    pockets.add( new Circle(
                        new Vector2D( wall.getStart().x() + jawRadius,
                        wall.getStart().y() - pocketClearRadius ),
                        pocketClearRadius - jawRadius ) );
                
                } //end nested if-then

            } // end second if-then

            // Get the bottommost horizontal cushionedrail.
            else if ( ( wall.getStart().y() - yOrigin ) > ( ( tableSize * 2 ) - ballRadius ) )
            {
                // 1. BOTTOM-LEFT DIAGONAL POCKET
                //    Hold this pocket in a variable before 
                //    adding it to the list: we need to transform
                //    it before we put it in the container.
                //
                //    Place the left-hand bottom-diagonal pocket.
                //    Ignore the the jawRadius here, because we're
                //    just "shrinking" the pocket-circles, rather
                //    than translating. Think "points" and "circles"
                //    rather than lines.
                pockets.add( new Circle(
                    new Vector2D( wall.getStart().x() - ( Math.pow( 2.0, -0.5 ) * ( pocketClearRadius + jawRadius ) ) ,  // center x
                    wall.getStart().y() - ( Math.pow(2.0, -0.5) * ( pocketClearRadius - jawRadius ) ) ),                 // center y
                    pocketClearRadius - jawRadius + cornerJawRotationExtension ) );  // The corner pocket's radius

                
                // 2. BOTTOM-RIGHT POCKET
                pockets.add( new Circle(
                    new Vector2D( wall.getEnd().x() + ( Math.pow( 2.0, -0.5 ) * ( pocketClearRadius + jawRadius ) ),
                    wall.getEnd().y() - ( Math.pow( 2.0, -0.5 ) * ( pocketClearRadius - jawRadius ) ) ),
                    pocketClearRadius - jawRadius + cornerJawRotationExtension ) );
                
            } // end third if-then
            
            // We only have the top horizontal cushion rail left!
            else
            {
                // 3. TOP-LEFT POCKET.
                pockets.add( new Circle(
                    new Vector2D( wall.getStart().x() - ( Math.pow( 2.0, -0.5 ) * ( pocketClearRadius + jawRadius ) ),
                    wall.getStart().y() + ( Math.pow( 2.0, -0.5 ) * ( pocketClearRadius - jawRadius ) ) ),
                    pocketClearRadius - jawRadius + cornerJawRotationExtension ) );
                
                // 4. TOP-RIGHT POCKET.
                pockets.add( new Circle(
                    new Vector2D( wall.getEnd().x() + ( Math.pow( 2.0, -0.5 ) * ( pocketClearRadius + jawRadius ) ),
                    wall.getEnd().y() + ( Math.pow( 2.0, -0.5 ) * ( pocketClearRadius - jawRadius ) ) ),
                    pocketClearRadius - jawRadius + cornerJawRotationExtension ) );
            
            } //end fourth if-then

        } // end for

    } //end method definePockets()


    /**
     * This method loads the walls list with
     * line segments.
     */
    private void defineWalls()
    {   
        // The "gap" between side rails at the top
        // (i.e. north) and bottom (i.e., south) of
        // the pool table -- the cushions are not yet
        // under consideration. This gap consists of
        // a ball's radius multiplied by a scaling
        // factor referenced by "pocketMultiplier"
        // (1.0 means that the gap is the
        // same sized circle as a ball). So far,
        // we have a top and bottom gap.
        double pocketGapVertical = ballRadius * pocketMultiplier;
        

        
        // For convenience. The horizontal gap at the
        // top/beginning and ending points of the north
        // and south horizontal rails and the vertical
        // gap at the start and ending points of the two
        // pairs of vertical rails
        double pocketGapDiagonal = ballRadius * Math.pow( 2.0, 0.5 )
            * pocketMultiplier;             // The square root of two
                                            // multiplied by the
                                            // pocketMultiplier multiplier
                                            // yields the correct diagonal
                                            // gap
                                            // (2 * ballRadius * pocketMultiplier)
        
        

        // Append the top horizontal line segment
        // (the "northern" cushion rail).
        walls.add( new Line(
            new Point2D(xOrigin+pocketGapDiagonal, yOrigin ),
            new Point2D(xOrigin+tableSize-pocketGapDiagonal, yOrigin ) ) );

        // Append the top-left line segemen
        // (a vertical cushion rail).
        walls.add( new Line(
            new Point2D( xOrigin, yOrigin+pocketGapDiagonal ),
            new Point2D( xOrigin, yOrigin+tableSize-pocketGapVertical ) ) );

        // Append the bottom-left line segment (also
        // a vertical cushion rail).
        walls.add( new Line(
            new Point2D( xOrigin, yOrigin+tableSize+pocketGapVertical ),
            new Point2D( xOrigin, yOrigin+tableSize*2-pocketGapDiagonal ) ) );
    
        // Append the bottom horizontal line segment.
        walls.add( new Line(
            new Point2D( xOrigin+pocketGapDiagonal, yOrigin+tableSize*2 ),
            new Point2D( xOrigin+tableSize-pocketGapDiagonal, yOrigin+tableSize*2 ) ) );
        
        // Append the top-right vertical line segment.
        walls.add( new Line(
            new Point2D( xOrigin+tableSize, yOrigin+pocketGapDiagonal),
            new Point2D( xOrigin+tableSize, yOrigin+tableSize-pocketGapVertical ) ) );
        
        // Finally, append the bottom-right vertical
        // line segment.
        walls.add(new Line(
            new Point2D( xOrigin+tableSize, yOrigin+tableSize+pocketGapVertical ),
            new Point2D( xOrigin+tableSize, yOrigin+tableSize*2-pocketGapDiagonal ) ) );


    } // end method defineWalls


    /**
     * This method helps the calling method to draw a pool table.
     * It defines the boundaries for the game of pool, and carves
     * out gaps for balls to escape through (i.e., pockets).
     * <p>
     * It initializes three lists:<br>
     * &nbsp&nbsp&nbsp(i)&nbsp&nbsp&nbsp walls - cushioned rails<br>
     * &nbsp&nbsp&nbsp(ii) &nbsp&nbsp jaws - circular "bumpers"
     * centered on the terminii of the pockets' mouths<br>
     * &nbsp&nbsp&nbsp(iii) &nbsp pockets - "goal" lines to set the
     * "pocketed" status for a ball
     */
    private void defineTable()
    {
        // create the "linear" cushions
        defineWalls();

        // create the "bumpers" around each pocket
        defineJaws();
        
        // create the "goal lines" with which the balls will collide
        definePockets();

    } // end method defineTable


    /**
     * Draw the table surface, the six padded
     * rails and the six pairs of pocket jaws.
     * @param dbg the buffer on to which we
     * splash pixels
     */
    public void draw( Graphics dbg )
    {
        // Draw the baize.
        dbg.setColor( baize );
        dbg.fillRect( xOrigin, yOrigin, tableSize, tableSize*2 );

        // Draw the cushioned rails.
        dbg.setColor( Color.white );

        // Run through the cushioned rails, drawing things.
        for ( Line wall : walls )
            dbg.drawLine( ( int )wall.getStart().x(), ( int )wall.getStart().y(),
                ( int )wall.getEnd().x(), ( int )wall.getEnd().y() );

        // Draw the bumpers, rounding up doubles to ints,
        // where we can, for screen coordinates. We could
        // do that in the Circle class and just return
        // ints for code readability.
        for ( Circle jaw : jaws )
        {
            dbg.drawOval( ( int )( jaw.getCenter().x()-jaw.getRadius() ),
                ( int )(jaw.getCenter().y()-jaw.getRadius() ),
                ( int )jaw.getRadius()*2, ( int )jaw.getRadius()*2 );
            
        }  // end for
        
        // Draw the pockets.
        for ( Circle pocket : pockets )
        {
            dbg.drawOval(
                ( int )( pocket.getCenter().x()-pocket.getRadius() ),
                ( int )( pocket.getCenter().y()-pocket.getRadius() ),
                ( int ) pocket.getRadius()*2, ( int )pocket.getRadius()*2 ); // shift ovals

        }  // end for
        
        // Draw the balls
        // for ( Circle ball : balls.getBallsList() )
        // {
        // }

    }  // end method draw
    
}  // end class Table

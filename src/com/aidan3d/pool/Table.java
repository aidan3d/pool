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
    //<editor-fold defaultstate="collapsed" desc="Fields">
    private final double BALL_MASS = 1.0;     // ALL balls' mass, in kg
    
    private boolean moving;
    
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
    
    private final double friction;            // Effectively the coefficient
                                              // of restitution for the
                                              // pool table's bed. It reduces
                                              // the speed of the ball

    private final Color baize;                // The color of the bed's
                                              // playing-surface

    private final ArrayList<Ball> balls;      // All sixteen balls in play
                                              // (including the cue ball)

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
    //</editor-fold>


    //<editor-fold defaultstate="collapsed"  desc="Constructor">
    /**
     * The seven-argument constructor.
     * @param x is the x-ordinate of the pool table's top-left origin
     * @param y is the y-ordinate of the pool table's top-left origin
     * @param t is the width of the table, as well as half of the length
     * (the pool table is comprised of two stacked squares of width "t")
     * @param r is the size of a pool ball
     * @param p is the pocketMultiplier multiplier
     * @param j is the jawMultiplier multiplier
     * @param f is the "de-accelerating" friction between ball and bed
     */
    public Table( int x, int y, int t, int r, double p, double j, double f )
    {
        //< editor-fold defaultstate="folded" desc = "Fields" >
        xOrigin = x;
        yOrigin = y;
        tableSize = t;
        ballRadius = r;
        pocketMultiplier = p;
        jawMultiplier = j;
        friction = f;
        moving = false;

        baize = new Color( 0, 0.2f, 0 );    // (0.0F, 0.392F, 0.078F):
                                            // baizeHSB = 120, 100, 20
                                            // (0/255, 51/255, 0/255):
                                            // baizeRGB = 0, 51, 0

        balls = new ArrayList<>();          // Loaded inside createBalls()

        walls = new ArrayList<>();          // Loaded inside defineWalls(),
                                            // called by defineTable()
        jaws = new ArrayList<>();

        pockets = new ArrayList<>();        // An array of circles, with
                                            // centers and radii


        // Set up all six "cushion rails" and put circular "bumper"
        // cushions at the mouths of each "opening" or gap between
        // the rails.
        defineTable();
        createBalls();
        
        // Let's set the cue ball in motion!
        balls.get(0).setVelocity( new Vector2D( -1.0, 1.0) ); // 1 pixel per frame
        
        // Fake out the "moving" tool!
        moving = true;

    } // end seven-argument constructor
    //</editor-fold>


    //<editor-fold defaultstate="collapsed" desc="Operations">
    
    /**
     * Lets the calling (PoolPanel object) object know
     * whether or not it should update the "field of battle."
     * @return a terminal condition - whether or not any
     * of the balls are moving
     */
    public boolean ballsAreMoving()
    {
        return moving;
    }


    /**
     * This method runs through all sixteen
     * in-play (i.e., on the table) balls
     * looking for a hit with another ball.
     */
    private void collisionsWithBalls()
    {
        // Run through all sixteen balls, looking
        // for hits with other balls.
        for ( Ball outerBall : balls ) // outer loop begin
        {           
            // Check the outerBall with all innerBalls
            // (except itself, of course!)
            for ( Ball innerBall : balls ) // inner loop begin
            {
                // Check whether we are referencing
                // the same ball.
                if (!outerBall.equals(innerBall)) 
                {
                    if ( outerBall.isHitByCircle( innerBall ) )
                    {
                        outerBall.setVelocity( new Vector2D( 0.0, 0.0 ) ); // Hacky, but does the job

                    } // end inner-nested if-then

                } // end outer nested if-tehn

            } // end inner for loop

        } // end outer for loop

    } //end method collisionsWithBalls

    
    /**
     * Run through all balls in play,
     * checking for hits with cushion
     * rails.
     */
    private void collisionsWithWalls()
    {
        for ( Ball b : balls )
        {
            for (Line w : walls)
            {
                if ( b.isHitByLine( w ) )
                {
                    
                    double preHitX = b.getVelocity().x();
                    double preHitY = b.getVelocity().y();
                    
                    b.setVelocity( new Vector2D( new Point2D( b.getVelocity().x()*-1, b.getVelocity().y() ) ) );
                    
                    break; // stop looping through the
                           // list of walls (we can only
                           // hit one at a time!)
                } // end if-then
            
            } // end inner for loop

        } // end outer for loop
    
    } // end method collisionsWithWalls
    
    
    /**
     * This method loads (the cue ball is placed first, then
     * we load the triangle or "rack" from the foot or apex,
     * with black in the center of the "third-from-top" row)
     * an array of sixteen balls: <br>
     * &nbsp &nbsp a) one cue ball <br>
     * &nbsp &nbsp b) seven "spots" or red balls <br>
     * &nbsp &nbsp c) seven "stripes" or yellow balls <br>
     * &nbsp &nbsp d) one black ball
     */
    private void createBalls()
    {
        double FLOAT_MULTIPLIER = 1.3F;       // Create a global constant
                                              // referring to a small 2%-of-
                                              // ball-radius "air apron"
                                              // around each ball

        double GAP = Math.pow( 3.0, 0.5 ) * ballRadius *       // The vertical
            FLOAT_MULTIPLIER;                                  // distance
                                                               // between our
                                                               // rows of balls

        // Create a "first position" for the player's
        // cue ball.
        Vector2D cueStart = new Vector2D( ( tableSize / 2.0 ) + xOrigin,
            ( ( tableSize * 2.0 ) / 5 ) + yOrigin );


        // Create a location on the game table for the apex
        // or "foot" of the rack, or "triangle." Let's do
        // math on doubles, then convert to int when we draw
        // in the calling Jpanel-derived PoolPanel object.
        Vector2D triStart = new Vector2D( ( tableSize / 2.0 ) + xOrigin,
        ( tableSize * 1.5 ) + yOrigin );


        // Add in the cue ball.
        balls.add( new Ball(
            ballRadius, 10.0F, "cue", cueStart, Color.white ) );

        // Add the first "spotted" (i.e., red) ball to the foot or
        // apex of the traingle; row 1, col. 1.
        balls.add( new Ball( ballRadius, BALL_MASS, "1spot", triStart, Color.red) );

        // Add the first "striped" (i.e., yellow) ball to the rack;
        // row 2, col 1.
        balls.add( new Ball( ballRadius, BALL_MASS, "1stripe", new Vector2D( ( triStart.x() - 
            ( ballRadius * FLOAT_MULTIPLIER ) ), triStart.y() + GAP ), Color.yellow ) );

        // Rack the second "striped" ball;
        // row 3, col 2.
        balls.add( new Ball( ballRadius, BALL_MASS, "2stripe", new Vector2D( ( triStart.x() +
            ( ballRadius * FLOAT_MULTIPLIER ) ), triStart.y() + GAP ), Color.yellow ) );

        // Rack the second "spotted" ball;
        // row 3, col 1
        balls.add( new Ball( ballRadius, BALL_MASS, "2spot", new Vector2D( ( triStart.x() -
            ( 2 * ( ballRadius * FLOAT_MULTIPLIER ) ) ), triStart.y() + ( 2 * GAP ) ), Color.red ) );

        // Rack the black ball;
        // row 3, col 2.
        balls.add( new Ball( ballRadius, BALL_MASS, "black", new Vector2D( triStart.x(),
            ( triStart.y() + ( 2 * GAP ) ) ),  Color.black  ) );

        // Rack the third "spotted" ball;
        // row 3, col 3.
        balls.add( new Ball( ballRadius, BALL_MASS, "3spot", new Vector2D( ( triStart.x() +
            ( 2 * ( ballRadius * FLOAT_MULTIPLIER ) ) ), ( triStart.y() + ( 2 * GAP ) ) ), Color.red ) );

        // Rack the third "striped" ball;
        // row 4, col 1.
        balls.add( new Ball( ballRadius, BALL_MASS, "3stripe", new Vector2D ( ( triStart.x() -
            ( 3 * ( ballRadius * FLOAT_MULTIPLIER ) ) ), ( triStart.y() + ( 3 * GAP ) ) ), Color.yellow ) );

        // Add the fourth "striped" ball;
        // row 4, col 2.
        balls.add( new Ball( ballRadius, BALL_MASS, "4stripe", new Vector2D( ( triStart.x() -
            ( ( ballRadius * FLOAT_MULTIPLIER  ) ) ), ( triStart.y() + ( 3 * GAP ) ) ), Color.yellow ) );

        // Add the fourth "spotted" ball;
        // row 4, col 3.
        balls.add( new Ball( ballRadius, BALL_MASS, "4spot", new Vector2D( ( triStart.x() +
            ( ballRadius * FLOAT_MULTIPLIER ) ), ( triStart.y() + ( 3 * GAP ) )), Color.red ) );

        // Add the fifth "striped" ball;
        // row 4, col 4.
        balls.add( new Ball( ballRadius, BALL_MASS, "5stripe", new Vector2D( ( triStart.x() +
            (3 * ( ballRadius * FLOAT_MULTIPLIER)  ) ), ( triStart.y() + ( 3 * GAP ) ) ), Color.yellow ) );
        
        // Add the fifth "spotted" ball;
        // row 5, col 1.
        balls.add( new Ball( ballRadius, BALL_MASS, "5spot", new Vector2D( ( triStart.x() -
            ( 4 * ( ballRadius * FLOAT_MULTIPLIER ) ) ), ( triStart.y() + ( 4 * GAP ) ) ), Color.red ) );
        
        // Add the sixth "striped" ball;
        // row 5, col 2.
        balls.add( new Ball( ballRadius, BALL_MASS, "4stripe", new Vector2D( ( triStart.x() -
            ( 2 * ( ballRadius * FLOAT_MULTIPLIER ) ) ), ( triStart.y() + ( 4 * GAP ) ) ), Color.yellow ) );
        
        // Add the sixth "spotted" ball;
        // row 5, col 3.
        balls.add( new Ball( ballRadius, BALL_MASS, "6spot", new Vector2D( ( triStart.x() ),
            ( triStart.y() + ( 4 * GAP ) ) ), Color.red )  );
        
        // Add the seventh "striped" ball;
        // row 5, col 4.
        balls.add( new Ball( ballRadius, BALL_MASS, "7stripe", new Vector2D( ( triStart.x() +
            ( 2 * (ballRadius * FLOAT_MULTIPLIER ) ) ), ( triStart.y() + ( 4 * GAP ) ) ), Color.yellow ) );
        
        // Add the seventh "spotted" ball;
        // row 5, col 5.
        balls.add( new Ball(ballRadius, BALL_MASS, "7spot", new Vector2D( ( triStart.x() +
            ( 4 * ( ballRadius * FLOAT_MULTIPLIER ) ) ),( triStart.y() + ( 4 * GAP ) ) ), Color.red ) );
        
        

    } //end method createBalls


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
                        wall.getStart().x()-jawRadius, wall.getStart().y() ), jawRadius, 0.0 ) );
                    
                    jaws.add(
                        new Circle( new Vector2D(
                        wall.getEnd().x()-jawRadius, wall.getEnd().y() ), jawRadius, 0.0 ) );
                }
                else  // The top left-hand vertical rail
                {
                    jaws.add(
                        new Circle( new Vector2D(
                        wall.getStart().x()-jawRadius, wall.getStart().y()), jawRadius, 0.0 ) );
                    
                    jaws.add( new Circle( new Vector2D(
                        wall.getEnd().x()-jawRadius, wall.getEnd().y()), jawRadius, 0.0 ) );
                
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
                        wall.getStart().x()+jawRadius, wall.getStart().y() ), jawRadius, 0.0 ) );
                    jaws.add(new Circle(
                        new Vector2D(
                        wall.getEnd().x()+jawRadius, wall.getEnd().y()), jawRadius, 0.0 ));
                }

                // We are on the bottom right-hand vertical rail.
                else 
                { 
                    jaws.add( new Circle(
                        new Vector2D(
                        wall.getStart().x()+jawRadius, wall.getStart().y()), jawRadius, 0.0 ));
                    jaws.add( new Circle(new Vector2D(wall.getEnd().x()+jawRadius,
                        wall.getEnd().y()), jawRadius, 0.0 ) );
                
                } // end nested if-then-else
                
            // y is very small... so, we have the top
            // top horizontal rail (would haved used 
            // .y==0 if ints)
            }
            else if ( wall.getEnd().y()-yOrigin < ballRadius )
            {
                jaws.add( new Circle(
                    new Vector2D(
                    wall.getStart().x(), wall.getStart().y()-jawRadius ), jawRadius, 0.0 ) );
                jaws.add(new Circle(
                    new Vector2D(
                    wall.getEnd().x(), wall.getEnd().y()-jawRadius ), jawRadius, 0.0 ) );
            }

            // The default case: must have the bottom horizontal rail!
            else 
            {
                jaws.add( new Circle(
                    new Vector2D(
                    wall.getStart().x(), wall.getStart().y()+jawRadius), jawRadius, 0.0 ) );
                
                jaws.add(new Circle(
                    new Vector2D(
                    wall.getEnd().x(), wall.getEnd().y()+jawRadius), jawRadius, 0.0 ) );
            
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
                        ( pocketClearRadius - jawRadius ), 0.0 ) );

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
                        pocketClearRadius - jawRadius, 0.0 ) ); // jaws have no mass
                
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
                    pocketClearRadius - ( jawRadius / 2 ), 0.0 ) );  // The corner pocket is
                                                                     // slightly larger than
                                                                     // a typical side pocket

                
                // 2. BOTTOM-RIGHT POCKET
                pockets.add( new Circle(
                    new Vector2D( wall.getEnd().x() + ( Math.pow( 2.0, -0.5 ) * ( pocketClearRadius + jawRadius ) ),
                    wall.getEnd().y() - ( Math.pow( 2.0, -0.5 ) * ( pocketClearRadius - jawRadius ) ) ),
                    pocketClearRadius - ( jawRadius / 2 ), 0.0 ) );
                
            } // end third if-then
            
            // We only have the top horizontal cushion rail left!
            else
            {
                // 3. TOP-LEFT POCKET.
                pockets.add( new Circle(
                    new Vector2D( wall.getStart().x() - ( Math.pow( 2.0, -0.5 ) * ( pocketClearRadius + jawRadius ) ),
                    wall.getStart().y() + ( Math.pow( 2.0, -0.5 ) * ( pocketClearRadius - jawRadius ) ) ),
                    pocketClearRadius - ( jawRadius / 2 ), 0.0 ) );
                
                // 4. TOP-RIGHT POCKET.
                pockets.add( new Circle(
                    new Vector2D( wall.getEnd().x() + ( Math.pow( 2.0, -0.5 ) * ( pocketClearRadius + jawRadius ) ),
                    wall.getEnd().y() + ( Math.pow( 2.0, -0.5 ) * ( pocketClearRadius - jawRadius ) ) ),
                    pocketClearRadius - ( jawRadius / 2 ), 0.0 ) );
            
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
        double pocketGapDiagonal = ( ballRadius * ( Math.pow( 2.0, 0.5 )
            * pocketMultiplier ) );                 // The square root of two
                                                    // multiplied by the
                                                    // pocketMultiplier
                                                    // multiplier yields the
                                                    // correct diagonal
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

        } // end for
        
        // Draw the balls.
        for ( Ball ball : balls )
        {
            // The new disc will be the color of the current ball.
            dbg.setColor( ball.getColor() );
            
            // Draw the disc at the ball's location on the table.
            dbg.drawOval(
            ( int )( ball.getCenter().x() - ball.getRadius() ),
            ( int )( ball.getCenter().y() - ball.getRadius() ),
            ( int )( 2 * ball.getRadius() ),
            ( int )( 2 * ball.getRadius() ) );
            
        } // end for

    }  // end method draw


    /**
     * This method calculates motion whilst the moving flag
     * is set to true.
     */
    public void move()
    {      
        for (Ball b : balls)
        {
            b.move();

        } // end for

    } //end method move
    
    public void update()
    {
        // Check for collisions between
        // all balls in play.
        // collisionsWithBalls();
        
        
        // Check for collisions between
        // all balls in play and the
        // cushion rails
        collisionsWithWalls();
        
        // collisionsWithPockets();

    } // end method update
    
    // </editor-fold>
}  // end class Table

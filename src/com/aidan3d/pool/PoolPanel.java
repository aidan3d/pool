/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aidan3d.pool;

import GamePanel.GamePanel;                 // a Swing JPanel-derived object

import java.awt.Color;                      // used for the color enumerations 
                                            // (e.g., .white)
import java.awt.Font;
import java.awt.FontMetrics;

/**
 * A <b>PoolPanel</b> is a <b>GamePanel</b> is a Swing <b>JPanel</b>.
 * <p> 
 * The <b>PoolPanel</b> class inherits from the <b>JPanel</b>
 * class, adding in a few fields specific to <i>Pool</i>.<br>
 * A <b>Table</b> object is created within the panel, which
 * represents the playing surface with a few "obstacles" (cushions)
 * throw in for good measure! A pocket is a desirable collision
 * targets.<br>
 * A <b>PoolPanel</b> object's "management-level" job is to set up
 * the game components and initialize the timing system.
 */
public class PoolPanel extends GamePanel
{
    private final int TABLE_WIDTH = 250;              // The pool table's width
                                                      // (and half of the depth)

    private final int BALL_RADIUS = 6;                // The pool balls' radii

    private final double POCKET_MULTIPLIER = 2.2F;    // A multiplier to adjust
                                                      // the pocket size

    private final double JAW_MULTIPLIER = 0.7F;       // A multiplier to adjust
                                                      // the jaw size (the
                                                      // "bumpers" forming the
                                                      // terminii of the pockets'
                                                      // mouths
    
    private final double BED_FRICTION = 0.1F;         // Effectively, the
                                                      // coefficient of
                                                      // restitution between
                                                      // ball and baize



    private final Font poolFont;
    private final FontMetrics metrics;
    
    private final Pool poolTop;                       // Refers to the
                                                      // "calling"
                                                      // application
                                                      // object (a
                                                      // Pool object);
                                                      // a Pool object
                                                      // has-a PoolPanel
                                                      // object (composition)

    private final Table poolTable;
    
    long poolGameStartTime;
    long poolGameTimeSpentInGame;


    /**
     * The no-argument constructor. It calls the two-argument
     * constructor, pas<br>
     * passing it a null reference to a <b>Pool</b> object, and a long int,
     * initialized to 0 (we refer it to a variable in the superclass (a
     * GamePanel object).
     */
    public PoolPanel()
    {
        // Feed the two-argument constructor.
        this( null, 0L );

    } // end no-argument constructor


    /**
     * The two-argument constructor
     * @param poolGame  a <b>Pool</b> application object
     * @param period  the delay period, in nanoseconds,
     * between gameUpdate() calls
     */
    public PoolPanel( Pool poolGame, long period )
    {
        // Initialize this panel's time period.
        this.period = period;  
        
        
        setBackground(Color.white);
        setPreferredSize( new java.awt.Dimension( PWIDTH, PHEIGHT ) );
        
        // Build the pool table.
        poolTable = new Table( ( PWIDTH / 2 ) - ( TABLE_WIDTH / 2 ), ( PHEIGHT / 2 ) - TABLE_WIDTH,
            TABLE_WIDTH, BALL_RADIUS,  POCKET_MULTIPLIER, JAW_MULTIPLIER, BED_FRICTION );
        
        // Set up the message font.
        poolFont = new Font( "SansSerif", Font.BOLD, 12 );
        metrics = this.getFontMetrics(poolFont );
        
        poolTop = poolGame;                           // A copy of "what we're
                                                      // sending" to the "super"

    } // end two-argument constructor


    /**
     * This is the initialization method that should be overridden 
     * by the derived class. This method will only be called once:
     * to set up game objects.
     */
    @Override
    public void customizeInit() {}


    /**
     * This method should be overridden during inheritance
     * and customized for your game to handle frame
     * rendering.
     */
    @Override
    public void customizeGameRender()
    {
        // Run at the "top" of each update/render/sleep
        // game loop; check whether the game has ended
        // first.
        if ( !super.gameOver )
        {
            dbg.setColor( Color.black );
            dbg.fillRect( 0, 0, super.getWidth(), super.getHeight() );

            dbg.setColor( Color.green );
            dbg.setFont(poolFont );

            // Report the average FPS and UPS at top left.
            dbg.drawString( "Average FPS/UPS: " + df.format( getAverageFPS() ) + "/"
                    + df.format( getAverageUPS() ), 20, 25 );

            // Display a basic representation of the pool table.
            poolTable.draw( dbg );

        } // end if-then
        
    } // end method customizeGameRender


    /**
     * This is the GameUpdate() method that should
     * be overridden during inheritance and customized
     * for your game to handle frame updates. The
     * customizeGameUpdate() method helps helps the
     * spheres have motion on screen
     */
    @Override
    public void customizeGameUpdate()
    {
        // Set the elapsed time since gameplay
        // commenced (grab it from the "super").
        poolTop.setTimeSpent(  super.getTimeSpentInGame() );


        // Check whether anything actually needs
        // to be done, based on all balls having
        // stopped moving
        if ( poolTable.ballsAreMoving() )
        {
            
            // Let the user know that we are
            // updating the game state:
            System.out.println( "Updating..." );
            // Update velocities of all balls, and thusly
            // update the displacement points of all balls.
            poolTable.update();

            // Throw the balls around on the baize.
            poolTable.move();
            
        } // end if-then

    } // end method customizeGameUpdate


    /**
     * This method runs before the first "clink" of
     * the timing chain (e.g., the main game loop or
     * "move-update" cycle runner)
     */
    @Override
    protected void preGameLoop() {}


    /**
     * This method runs as soon as we are "within"
     * the game loop.
     */
    @Override
    protected void insideGameLoop() {}


    /**
     * This method holds operations carried out at
     * the pool game's end (somebody's won!. After
     * play is complete, a message such as
     * <i>"You've Won!"</i> might be displayed.
     */
    @Override
    protected void postGameLoop() {}
    
} //end class PoolPanel

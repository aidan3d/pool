/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aidan3d.pool;


/**
 * This class inherits functionality from Swing's
 * <b>JFrame</b> class. Basically, it draws a symbolic
 * pool table (with cushion rails and pockets) to
 * screen on which sixteen balls and a pool cue.
 * Much of this class handles the drawing of the
 * JFrame-derived object referenced by the variable
 * entitled "pool."
 * @see javax.swing.JFrame
 */
public class Pool extends javax.swing.JFrame
{
    private static final int DEFAULT_FPS = 60;  // the frame rate
    private static long period;


    /**
     * This is the <i>PlanetSim</i> no-argument constructor.
     * @returns a new PlanetSim JFrame-derived object
     */
    public Pool()
    {
        // set up the Swing gui components
        initComponents();

    } // end no-argument constructor


    /**
     * If the game has been paused, this method handles
     * the operations set to occur on a 'resume.'
     * @param e event delegate of the heavyweight AWT system.
     */
    public void windowActivated( java.awt.event.WindowEvent e )
    {
        ((PoolPanel)poolGame).resumeGame();

    } // end method windowActivated


    /**
     * This method handles detection by the AWT event-handling
     * system of a "pause" being triggered by the player.
     * @param e event delegate of the AWT system 
     */
    public void windowDeactivated( java.awt.event.WindowEvent e )
    {
        ((PoolPanel)poolGame).pauseGame();

    } // end method windowDeactivated


    /**
     * @param e Event delegate
     */
    public void windowDeiconified( java.awt.event.WindowEvent e )
    {
        ((PoolPanel)poolGame).resumeGame();

    } // end method windowDeiconified


    /**
     * @param e Event delegate
     */
    public void windowIconified( java.awt.event.WindowEvent e )
    {
        ((PoolPanel)poolGame).pauseGame();

    } // end method windowIconified


    /**
     * This method handles detection of the window being closed.
     * Either the user hit ALT-F4, or the "x" window-closing
     * button was clicked.
     * @param e event delegate (heavyweight)
     */
    public void windowClosing(java.awt.event.WindowEvent e)
    {
        ((PoolPanel)poolGame).stopGame();

    } // end method windowClosing


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        poolGame = new com.aidan3d.pool.PoolPanel(this, period*1000000L);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("A Simple Pool Game");
        setBackground(new java.awt.Color(128, 128, 128));

        org.jdesktop.layout.GroupLayout poolGameLayout = new org.jdesktop.layout.GroupLayout(poolGame);
        poolGame.setLayout(poolGameLayout);
        poolGameLayout.setHorizontalGroup(
            poolGameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 788, Short.MAX_VALUE)
        );
        poolGameLayout.setVerticalGroup(
            poolGameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 588, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(poolGame, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(poolGame, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    /**
     * This method is the <i>Pool</i> game's start point.
     * @param args <b>String</b> array - the command line arguments
     */
    public static void main(String args[])
    {
        int fps = DEFAULT_FPS;  // set as a default, or in the command-line-
                                // parameter if run from the command line

        // Check the parameter received from the command
        // line (if any); if it's there, use it!
        if (args.length != 0)
        {
            fps = Integer.parseInt(args[0]);
        
        }


        // "period" holds the time-for-a-frame in milliseconds
        period = (long)1000.0/fps;

        System.out.println("fps: " + fps + "; period: " + period + " ms");
        
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Pool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>


        /*
         * Create and display the form.
         */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pool().setVisible(true);
            } // end method run
        });

    } // end main
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel poolGame;
    // End of variables declaration//GEN-END:variables

}

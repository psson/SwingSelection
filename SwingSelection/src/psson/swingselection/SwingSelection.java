/*
 * The MIT License
 *
 * Copyright 2015 Andreas Pettersson.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package psson.swingselection;

import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

/**
 * The SwingSelection class provides a simple way of making selections in a
 * Swing application.
 * @author Andreas Pettersson
 */
public class SwingSelection {
    
//<editor-fold defaultstate="collapsed" desc="Members and constructors">
    private static final int NUM_SELECTION_HANDLES = 8;
    private static final int SELECTION_HANDLE_SIZE = 5;
    
    private final Container c;
    private final Rectangle[] handles;
    private final InternalSelection mySel;
    
    private final SelectionMouseAdapter listener;
    
    /**
     * Creates a new SwingSelection and connects it to a parent Container
     * @param c a Swing Container where the selections will be done
     */
    public SwingSelection( Container c ) {
        this.c = c;
        mySel = new InternalSelection();
        this.c.add( mySel );
        
        handles = new Rectangle[ NUM_SELECTION_HANDLES ];
        initSelectionHandles();
        
        listener = new SelectionMouseAdapter();
        
        c.addMouseListener( listener );
        c.addMouseMotionListener( listener );
        
    }
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="Access functions">
    /**
     * Sets the size and position of the selection
     * @param r a Rectangle with the size and position of the selection
     */
    public void setBounds( Rectangle r ) {
        mySel.setBounds( r );
    }
    
    /**
     * Returns the currents size and position of the selection
     * @return a Rectangle with the current size and position of the selection
     */
    public Rectangle getBounds() {
        return mySel.getBounds();
    }
    
    /**
     * Checks whether the selection contains the provided coordinates
     * @param x the x coordinate to check
     * @param y the y coordinate to check
     * @return true if the selection contains x,y
     */
    public boolean contains( int x, int y ) {
        //TODO The documentation for contains( Point p ) states that the Point
        // is taken to be relative to the coordinates of the component
        // Check if this is also true for this function
        // If true, modify input here, otherwise check and modify
        // contains( Point p ) if needed.
        return mySel.contains( x, y );
    }
    
    /**
     * Checks whether the selection contains the point
     * @param p the point to check
     * @return true if the selection contains p
     */
    public boolean contains( Point p ) {
        // Wrapper for contains( int x, int y)
        //TODO See TODO for contains( int x, int y )
        // Check other function first to avoid needlessly redoing
        // coordinate adjustments
        return this.contains( (int)p.getX(), (int)p.getY() );
    }
    
    /**
     * Sets the visibility of the selection
     * @param visible true to make selection visible, false to make it invisible
     */
    public void setVisible( boolean visible ) {
        mySel.setVisible( visible );
        c.repaint();
    }
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="Selection handles">
    /**
     * Initializes the array of selection handles to Rectangles
     */
    private void initSelectionHandles() {
        
        for ( int i = 0 ; i < NUM_SELECTION_HANDLES ; i++ ) {
            handles[ i ] = new Rectangle();
        }
    }
    
    /**
     * Calculates and sets the selection handles associated with the selection.
     */
    private void setSelectionHandles() {
        
        // Get limits of selection for easier
        int width = mySel.getWidth();       // Width
        int height = mySel.getHeight();     // Height
        int left = mySel.getX();            // Left side x-coordinate
        int right = left + width;           // Right side x-coordinate
        int top = mySel.getY();             // Top y-coordinate
        int bottom = top + height;          // Bottom y-coordinate
        
        // Upper left corner handle
        handles[0].setBounds( left, top, SELECTION_HANDLE_SIZE, SELECTION_HANDLE_SIZE );
        // Upper right corner handle
        handles[1].setBounds( right - SELECTION_HANDLE_SIZE, top, SELECTION_HANDLE_SIZE, SELECTION_HANDLE_SIZE );
        // Lower left corner handle
        handles[2].setBounds( left, bottom - SELECTION_HANDLE_SIZE, SELECTION_HANDLE_SIZE, SELECTION_HANDLE_SIZE );
        // Lower right corner handle
        handles[3].setBounds( right - SELECTION_HANDLE_SIZE, bottom - SELECTION_HANDLE_SIZE, SELECTION_HANDLE_SIZE, SELECTION_HANDLE_SIZE );
        // Upper edge handle
        handles[4].setBounds( left, top, width, SELECTION_HANDLE_SIZE );
        // Bottom edge handle
        handles[5].setBounds( left, bottom - SELECTION_HANDLE_SIZE, width, SELECTION_HANDLE_SIZE );
        // Left edge handle
        handles[6].setBounds( left, top, SELECTION_HANDLE_SIZE, height);
        // Right edge handle
        handles[7].setBounds( left - SELECTION_HANDLE_SIZE, top, SELECTION_HANDLE_SIZE, height);
    }
    
    /**
     * Checks whether Point p is inside one of the selection handles
     * @param p point to check against selection handles
     * @return number of handle or 0 if not inside a handle
     */
    private int inHandle( Point p ) {
        
        for( int i = 0 ; i < NUM_SELECTION_HANDLES ; i++ ) {
            if( handles[ i ].contains( p ) ) {
                return i + 1;
            }
        }
        
        return 0;   // No handle contained the point, return 0
        
    }
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="InternalSelection class">
    /**
     * InternalSelection class that is the actual component added to the parent Container.
     */
    private class InternalSelection extends JComponent {
        
        /**
         * Sets bounds based on two points
         * @param p1 a point in one corner of the selection
         * @param p2 the opposite corner of the selection
         */
        public void setBounds( Point p1, Point p2 ) {
            
            int x, y, width, height;
            
            if( p1.getX() < p2.getX() ) {
                x = (int)p1.getX();
                width = (int)( p2.getX() - p1.getX());
            } else {
                x = (int)p2.getX();
                width = (int)( p1.getX() - p2.getX() );
            }
            
            if( p1.getY() < p2.getY() ) {
                y = (int)p1.getY();
                height = (int)( p2.getY() - p1.getY() );
            } else {
                y = (int)p2.getY();
                height = (int)( p1.getY() - p2.getY() );
            }
            
            this.setBounds(x, y, width, height);
            
        }
        
    }
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="ContainerMouseAdapter class">
    /**
     * Internal MouseAdapter class
     */
    private class SelectionMouseAdapter extends MouseAdapter {
        
        private final int activeHandle;
        private final boolean moveSelection;
        private Point fp, mp;
        
        public SelectionMouseAdapter() {
            
            activeHandle = 0;
            moveSelection = false;
            fp = new Point();
            mp = new Point();
            
        }
        
        @Override
        public void mousePressed( MouseEvent e ) {
            
            fp = e.getPoint();
            
            mySel.setVisible( true );
            
        }
        
        @Override
        public void mouseDragged( MouseEvent e ) {
            
            mp = e.getPoint();
            
            mySel.setBounds( fp, mp );
            
            mySel.repaint();
            
        }
        
        @Override
        public void mouseReleased( MouseEvent e ) {
            
            mp = e.getPoint();
            
            mySel.setBounds( fp, mp );
            
            mySel.repaint();
            
        }
    }
//</editor-fold>    
}

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

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

/**
 * The SwingSelection class provides a simple way of making selections in a
 * Swing application.
 * @author Andreas Pettersson
 */
public class SwingSelection {
    
//<editor-fold defaultstate="collapsed" desc="Members and constructors">
    
    private final Container c;
    private final InternalSelection mySel;
    
    private final SelectionMouseAdapter listener;
    
    /**
     * Creates a new SwingSelection and connects it to a parent Container
     * @param c a Swing Container where the selections will be done
     */
    public SwingSelection( Container c ) {
        this.c = c;
        mySel = new InternalSelection();
        this.setDefaultBorder();
        this.c.add( mySel );
        
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
     * Returns the current border of the selection
     * @return a Border
     */
    public Border getBorder() {
        return mySel.getBorder();
    }
    
    /**
     * Sets the borde of the selection
     * @param b the border to be rendered for the selection
     */
    public void setBorder( Border b ) {
        mySel.setBorder( b );
    }
    
    /**
     * Sets a default border consisting of a black line
     */
    public void setDefaultBorder() {
        mySel.setBorder( BorderFactory.createLineBorder(Color.black) );
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
        
        private Point fp, mp;
        
        public SelectionMouseAdapter() {
            
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

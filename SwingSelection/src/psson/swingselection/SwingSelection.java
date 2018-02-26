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
import java.awt.Cursor;
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
    
    private static final int NUM_SELECTION_HANDLES = 9;
    private static final int SELECTION_HANDLE_SIZE = 3;
    
    private static final int NOT_IN_HANDLE = 0;
    private static final int UPPER_LEFT_HANDLE = 1;
    private static final int UPPER_RIGHT_HANDLE = 2;
    private static final int LOWER_LEFT_HANDLE = 3;
    private static final int LOWER_RIGHT_HANDLE = 4;
    private static final int UPPER_EDGE_HANDLE = 5;
    private static final int LOWER_EDGE_HANDLE = 6;
    private static final int LEFT_EDGE_HANDLE = 7;
    private static final int RIGHT_EDGE_HANDLE = 8;
    
    
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
        this.setDefaultBorder();
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
        return mySel.contains( x, y );
    }
    
    /**
     * Checks whether the selection contains the point
     * @param p the point to check
     * @return true if the selection contains p
     */
    public boolean contains( Point p ) {
        // Wrapper for contains( int x, int y)
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
     * Sets the border of the selection
     * @param b the border to be rendered for the selection
     */
    public void setBorder( Border b ) {
        mySel.setBorder( b );
    }
    
    /**
     * Sets a default border consisting of a black line
     */
    public final void setDefaultBorder() {
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
    
    /**
    * Sets whether or not the selection should stay inside the parent container
    * @param stayInside true if selection should be locked inside the parent container, otherwise false
    */
    public void stayInsideContainer( boolean stayInside ) {
        mySel.stayInsideContainer( stayInside );
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
        
        // Don't init handle[ 0 ], this is an unused dummy handle 
        // Upper left corner handle
        handles[ UPPER_LEFT_HANDLE ].setBounds( left, top, SELECTION_HANDLE_SIZE, SELECTION_HANDLE_SIZE );
        // Upper right corner handle
        handles[ UPPER_RIGHT_HANDLE ].setBounds( right - SELECTION_HANDLE_SIZE, top, SELECTION_HANDLE_SIZE, SELECTION_HANDLE_SIZE );
        // Lower left corner handle
        handles[ LOWER_LEFT_HANDLE ].setBounds( left, bottom - SELECTION_HANDLE_SIZE, SELECTION_HANDLE_SIZE, SELECTION_HANDLE_SIZE );
        // Lower right corner handle
        handles[ LOWER_RIGHT_HANDLE ].setBounds( right - SELECTION_HANDLE_SIZE, bottom - SELECTION_HANDLE_SIZE, SELECTION_HANDLE_SIZE, SELECTION_HANDLE_SIZE );
        // Upper edge handle
        handles[ UPPER_EDGE_HANDLE ].setBounds( left, top, width, SELECTION_HANDLE_SIZE );
        // Bottom edge handle
        handles[ LOWER_EDGE_HANDLE ].setBounds( left, bottom - SELECTION_HANDLE_SIZE, width, SELECTION_HANDLE_SIZE );
        // Left edge handle
        handles[ LEFT_EDGE_HANDLE ].setBounds( left, top, SELECTION_HANDLE_SIZE, height);
        // Right edge handle
        handles[ RIGHT_EDGE_HANDLE ].setBounds( right - SELECTION_HANDLE_SIZE, top, SELECTION_HANDLE_SIZE, height);
    }
    
    /**
     * Checks whether Point p is inside one of the selection handles
     * @param p point to check against selection handles
     * @return number of handle or 0 if not inside a handle
     */
    private int inHandle( Point p ) {
        
        // Check all handles except dummy handle
        for( int i = 1 ; i < NUM_SELECTION_HANDLES ; i++ ) {
            if( handles[ i ].contains( p ) ) {
                return i;
            }
        }
        
        return NOT_IN_HANDLE;   // No handle contained the point, return NOT_IN_HANDLE
        
    }
    
    /**
     * Returns a point opposite the current handle
     * @param activeHandle the current handle selected
     * @return point to the opposite handle
     */
    private Point getOppositePoint( int activeHandle ) {
        
        Point oppPoint = new Point();
        
        switch( activeHandle ) {
            case UPPER_LEFT_HANDLE:
                // Return point to lower right corner
                oppPoint.setLocation( mySel.getX() + mySel.getWidth(), mySel.getY() + mySel.getHeight() );
                break;
            case UPPER_RIGHT_HANDLE:
                // Return point to lower left corner
                oppPoint.setLocation( mySel.getX(), mySel.getY() + mySel.getHeight() );
                break;
            case LOWER_LEFT_HANDLE:
                // Return point to upper right corner
                oppPoint.setLocation( mySel.getX() + mySel.getWidth(), mySel.getY() );
                break;
            case LOWER_RIGHT_HANDLE:
                // Return point to upper left corner
                oppPoint = mySel.getLocation();
                break;
            case UPPER_EDGE_HANDLE:
                // Return point to lower right corner
                oppPoint.setLocation( mySel.getX() + mySel.getWidth(), mySel.getY() + mySel.getHeight() );
                break;
            case LOWER_EDGE_HANDLE:
                // Return point to upper left corner
                oppPoint = mySel.getLocation();
                break;
            case LEFT_EDGE_HANDLE:
                // Return point to lower right corner
                oppPoint.setLocation( mySel.getX() + mySel.getWidth(), mySel.getY() + mySel.getHeight() );
                break;
            case RIGHT_EDGE_HANDLE:
                // Return point to upper left corner
                oppPoint = mySel.getLocation();
                break;
            default:
                // Error, should not be possible
                //TODO Add exception if bad handle is entered? What to do about it?
                oppPoint.setLocation( 0,0 );
        }
        
        return oppPoint;
        
    }
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="InternalSelection class">
    /**
     * InternalSelection class that is the actual component added to the parent Container.
     */
    private class InternalSelection extends JComponent {
        
        private boolean inContainer;
        
        public InternalSelection() {
            super();
            inContainer = false;
        }
        
        /**
         * Checks if a position is inside the selection. The position is specified by two coordinates relative to the container the SwingSelection is attached to.
         * @param x x-coordinate of the position
         * @param y y-coordinate of the position
         * @return true if coordinates are inside the selection, otherwise false.
         */
        public boolean containsContainerCoords( int x, int y ) {
            
            if( x >= super.getX() && x < ( super.getX() + super.getWidth() ) ) {
                if( y >= super.getY() && y < ( super.getY() + super.getHeight() ) ) {
                    return true;
                }
            }
            
            return false;
        }
        
        /**
         * Checks if a point is inside the selection. The point is specified relative to the container the SwingSelection is attached to.
         * @param p a Point relative to the container
         * @return true if the point is inside the selection, otherwise false
         */
        public boolean containsContainerPoint( Point p ) {
            return this.containsContainerCoords( (int)p.getX(), (int)p.getY() );
        }
        
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
        
        /**
         * Adjusts the position of the selection based on the relative position of the two points. If newPoint is below oldPoint, the selection will move downward the same distance that separates oldPoint and newPoint.
         * @param oldPoint base point
         * @param newPoint new point 
         */
        public void move( Point oldPoint, Point newPoint ) {
            
            Rectangle sel;
            
            sel = this.getBounds();
            
            double x, y;   // New coordinates
            
            // Calculate new position for selection
            x = sel.getX() + ( newPoint.getX() - oldPoint.getX() );
            y = sel.getY() + ( newPoint.getY() - oldPoint.getY() );
            
            if( inContainer ) {
                if( x < 0 ) {
                    x = 0;
                }
                if( x + sel.getWidth() > c.getWidth() ) {
                    x = c.getWidth() - sel.getWidth();
                }
                if( y < 0 ) {
                    y = 0;
                }
                if( y + sel.getHeight() > c.getHeight() ) {
                    y = c.getHeight() - sel.getHeight();
                }
            }
            
            sel.setLocation( (int)x, (int)y );
            
            this.setBounds( sel );
        }
        
        /**
         * Sets whether or not the selection should stay inside the parent container
         * @param stayInside true if selection should be locked inside the parent container, otherwise false
         */
        public void stayInsideContainer( boolean stayInside ) {
            inContainer = stayInside;
        }
        
    }
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="ContainerMouseAdapter class">
    /**
     * Internal MouseAdapter class
     */
    private class SelectionMouseAdapter extends MouseAdapter {
        
        private Point fp, mp;
        
        private int activeHandle;
        
        private boolean moveSelection;
        
        public SelectionMouseAdapter() {
            
            fp = new Point();
            mp = new Point();
            
            activeHandle = 0;
            
            moveSelection = false;
            
        }
        
        @Override
        public void mouseMoved( MouseEvent e ) {
            
            if( mySel.isVisible() ) {
                
                activeHandle = inHandle( e.getPoint() );
                
                switch( activeHandle ) {
                    case UPPER_LEFT_HANDLE:
                        c.setCursor( new Cursor( Cursor.NW_RESIZE_CURSOR ) );
                        break;
                    case UPPER_RIGHT_HANDLE:
                        c.setCursor( new Cursor( Cursor.NE_RESIZE_CURSOR ) );
                        break;
                    case LOWER_LEFT_HANDLE:
                        c.setCursor( new Cursor( Cursor.SW_RESIZE_CURSOR ) );
                        break;
                    case LOWER_RIGHT_HANDLE:
                        c.setCursor( new Cursor( Cursor.SE_RESIZE_CURSOR ) );
                        break;
                    case UPPER_EDGE_HANDLE:
                        c.setCursor( new Cursor( Cursor.N_RESIZE_CURSOR ) );
                        break;
                    case LOWER_EDGE_HANDLE:
                        c.setCursor( new Cursor( Cursor.S_RESIZE_CURSOR ) );
                        break;
                    case LEFT_EDGE_HANDLE:
                        c.setCursor( new Cursor( Cursor.W_RESIZE_CURSOR ) );
                        break;
                    case RIGHT_EDGE_HANDLE:
                        c.setCursor( new Cursor( Cursor.E_RESIZE_CURSOR ) );
                        break;
                    default:
                        if( mySel.containsContainerPoint( e.getPoint() )) {
                            // Pointer inside selection, set move cursor
                            c.setCursor( new Cursor( Cursor.MOVE_CURSOR ) );
                        } else {
                            // All other cases, normal cursor
                            c.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
                        }
                        break;
                }
            } else {
                c.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ));
            }
        }
        
        @Override
        public void mousePressed( MouseEvent e ) {
            
            fp = e.getPoint();
            
            // Don't move or resize invisible selection
            if( mySel.isVisible() ) {
                activeHandle = inHandle( e.getPoint() );
                if( activeHandle > 0 ) {
                    // In selection handle, resize
                } else if( mySel.containsContainerPoint( e.getPoint() ) ) {
                    // Inside selection, prepare to move
                    moveSelection = true;
                } else {
                    // Outside selection, do nothing
                }
            }
            
            // Selection should always be visible after mouse button has been pressed
            mySel.setVisible( true );
            
        }
        
        @Override
        public void mouseDragged( MouseEvent e ) {
            
            mp = e.getPoint();
            
            if( activeHandle > 0 ) {
                // Resize selection based on current handle and opposite corner
                fp = getOppositePoint( activeHandle );
                mySel.setBounds( fp, mp );
            } else if( moveSelection) {
                // Move selection
                mySel.move(fp, mp);
                fp = mp;
            } else {
                // Drag a new selection
                mySel.setBounds( fp, mp );
            }
            
            mySel.repaint();
            
        }
        
        @Override
        public void mouseReleased( MouseEvent e ) {
            
            mp = e.getPoint();
            
            if( activeHandle > 0 ) {
                // Finish resizing selection
                fp = getOppositePoint( activeHandle );
                mySel.setBounds( fp, mp );
            } else if ( moveSelection ) {
                // Finish moving selection
                mySel.move(fp, mp);
            } else {
                // Finish 
                mySel.setBounds( fp, mp );
            }
            
            moveSelection = false;
            
            setSelectionHandles();
            
            mySel.repaint();
            
        }
    }
//</editor-fold>    
}

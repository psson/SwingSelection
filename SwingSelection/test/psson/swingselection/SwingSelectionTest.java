/*
 * The MIT License
 *
 * Copyright 2018 andreas.pettersson.
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
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.border.Border;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author andreas.pettersson
 */
public class SwingSelectionTest {
    
    private static final int C_WIDTH = 800;
    private static final int C_HEIGHT = 600;
    private static Container myContainer;
    private SwingSelection testSel;
    
    public SwingSelectionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        myContainer = new Container();
        myContainer.setSize( C_WIDTH, C_HEIGHT );
    }
    
    @AfterClass
    public static void tearDownClass() {
        myContainer = null;
    }
    
    @Before
    public void setUp() {
        testSel = new SwingSelection( myContainer );
    }
    
    @After
    public void tearDown() {
        testSel = null;
    }

    /**
     * Tests of setBounds and getBounds method, of class SwingSelection.
     * Tests setting basic bounds for the selection and retrieving them
     */
    @Test
    public void testSetAndGetBounds() {
        System.out.println("setBounds and getBounds");
        Rectangle r = new Rectangle( 0,0,100,100 );
        Rectangle expResult;
        testSel.setBounds(r);
        
        // Test that setting basic bounds work
        expResult = testSel.getBounds();
        assertEquals( r, expResult );
    }

    /**
     * Test of contains int,int method, of class SwingSelection.
     */
    @Test
    public void testContains_int_int() {
        System.out.println("contains int int");
        Rectangle r = new Rectangle( 0,0,100,100 );
        testSel.setBounds(r);
        int x;
        int y;
        boolean expResult;
        boolean result;
        
        // Test points inside the selection
        x = 0;
        y = 0;
        expResult = true;
        result = testSel.contains(x, y);
        assertEquals(expResult, result);
        
        x = 0;
        y = 99;
        expResult = true;
        result = testSel.contains(x, y);
        assertEquals(expResult, result);
        
        x = 99;
        y = 0;
        expResult = true;
        result = testSel.contains(x, y);
        assertEquals(expResult, result);
        
        // Test points outside the selection
        x = 0;
        y = 100;
        expResult = false;
        result = testSel.contains(x, y);
        assertEquals(expResult, result);
        
        x = 100;
        y = 0;
        expResult = false;
        result = testSel.contains(x, y);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of contains Point method, of class SwingSelection.
     * Very basic test since this is a wrapper for contains int int
     */
    @Test
    public void testContains_Point() {
        System.out.println("contains Point");
        Rectangle r = new Rectangle( 0,0,100,100 );
        testSel.setBounds(r);
        Point p = new Point();
        p.setLocation( 50, 50 );
        boolean expResult = true;
        boolean result = testSel.contains(p);
        assertEquals(expResult, result);
    }

    /**
     * Test of getBorder method, of class SwingSelection.
     */
    @Test
    public void testGetBorder() {
        System.out.println("getBorder");
        SwingSelection instance = null;
        Border expResult = null;
        Border result = instance.getBorder();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setBorder method, of class SwingSelection.
     */
    @Test
    public void testSetBorder() {
        System.out.println("setBorder");
        Border b = null;
        SwingSelection instance = null;
        instance.setBorder(b);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setDefaultBorder method, of class SwingSelection.
     */
    @Test
    public void testSetDefaultBorder() {
        System.out.println("setDefaultBorder");
        SwingSelection instance = null;
        instance.setDefaultBorder();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setVisible method, of class SwingSelection.
     */
    @Test
    public void testSetVisible() {
        System.out.println("setVisible");
        boolean visible = false;
        SwingSelection instance = null;
        instance.setVisible(visible);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Tests that the selection stays within the parent container when 
     * resized or moved
     */
    @Test
    public void testStayInsideContainer() {
        System.out.println("stayInsideContainer");
        
        testSel.stayInsideContainer(true);
        int selX = 0;
        int selY = 0;
        int selW = 100;
        int selH = 100;
        
        // Simple test with selection in upper left corner
        Rectangle r = new Rectangle( selX, selY, selW, selH );
        Rectangle expResult = new Rectangle( selX, selY, selW, selH );
        Rectangle result;
        
        testSel.setBounds(r);
        result = testSel.getBounds();
        assertEquals( expResult,result );
        
        // Test with simple values away from upper left corner
        r.setBounds( selX, selY, selW, selH );
        expResult.setBounds( selX, selY, selW, selH );
        testSel.setBounds(r);
        result = testSel.getBounds();
        assertEquals( expResult,result );
        
        // Test where right edge of selection would extend off the Container
        selX = 400;
        selW = 500;
        
        r.setBounds( selX, selY, selW, selH );
        expResult.setBounds( selX, selY, selW, selH );
        testSel.setBounds(r);
        result = testSel.getBounds();
        
        // Test that x is modified
        assertFalse( expResult.getX() == result.getX() );
        
        // Check that x is correctly modified to allow for the entire
        // width of the selection to remain on the Component
        assert( result.getX() == C_WIDTH - result.getWidth());
        
        selX = 100;
        selY = 400;
        selW = 100;
        selH = 400;
        
        // Test where bottom edge of selection would extend off the Container
        r.setBounds( selX, selY, selW, selH );
        expResult.setBounds( selX, selY, selW, selH );
        testSel.setBounds(r);
        result = testSel.getBounds();
        
        // Test that y is modified
        assertFalse( expResult.getY() == result.getY() );
        
        // Check that y is correctly modified to allow for the entire
        // width of the selection to remain on the Component
        assert( result.getY() == C_HEIGHT - result.getHeight());
        
        // Run tests where the selection is allowed outside container
        testSel.stayInsideContainer(false);
        
        // Test where right edge of selection would extend off the Container
        selX = 400;
        selW = 500;
        
        r.setBounds( selX, selY, selW, selH );
        expResult.setBounds( selX, selY, selW, selH );
        testSel.setBounds(r);
        result = testSel.getBounds();
        
        // Test that the selection is unmodified
        assertEquals( expResult, result );

        // Test where bottom edge of selection would extend off the Container
        selX = 100;
        selY = 400;
        selW = 100;
        selH = 400;
        
        r.setBounds( selX, selY, selW, selH );
        expResult.setBounds( selX, selY, selW, selH );
        testSel.setBounds( r );
        result = testSel.getBounds();
        
        // Test that the selection is unmodified
        assertEquals( expResult, result );
        
        // Redo test with above selection parameters
        testSel.stayInsideContainer(true);
        
        testSel.setBounds( r );
        result = testSel.getBounds();
        
        // Test that y is modified
        assertFalse( expResult.getY() == result.getY() );
        
        // Check that y is correctly modified to allow for the entire
        // width of the selection to remain on the Component
        assert( result.getY() == C_HEIGHT - result.getHeight());
        
        // Test for values of x lower than 1
        selX = -100;
        selY = 100;
        selW = 100;
        selH = 100;
        
        r.setBounds( selX, selY, selW, selH );
        expResult.setBounds( selX, selY, selW, selH );
        testSel.setBounds( r );
        result = testSel.getBounds();
        
        // Test that x is modified
        assertFalse( expResult.getX() == result.getX() );
        
        // Check that x is correctly modified to allow for the entire
        // width of the selection to remain on the Component
        assert( result.getX() == 0 );
        
        // Test for values of y lower than 1
        selX = 100;
        selY = -100;
        selW = 100;
        selH = 100;
        
        r.setBounds( selX, selY, selW, selH );
        expResult.setBounds( selX, selY, selW, selH );
        testSel.setBounds( r );
        result = testSel.getBounds();
        
        // Test that y is modified
        assertFalse( expResult.getY() == result.getY() );
        
        // Check that y is correctly modified to allow for the entire
        // width of the selection to remain on the Component
        assert( result.getY() == 0 );
    }
    
}

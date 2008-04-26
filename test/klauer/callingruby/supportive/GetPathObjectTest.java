/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package klauer.callingruby.supportive;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author klauer
 */
public class GetPathObjectTest {

    public GetPathObjectTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getPathForObject method, of class GetPathObject.
     */
    @Test
    public void testGetPathForObject() {
        System.out.println("getPathForObject");
        Object obj = null;
        String expResult = "";
        String result = GetPathObject.getPathForObject(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
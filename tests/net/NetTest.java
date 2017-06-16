/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import java.io.File;
import java.util.List;
import org.dom4j.DocumentException;
import static org.junit.Assert.fail;
import org.junit.*;

/**
 *
 * @author vodl
 */
public class NetTest {

    public NetTest() {
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
     * Test of generateDocument method, of class Net.
     */
    @Test
    public void testGenerateDocument() {
        System.out.println("generateDocument");
        Net instance = new Net();
        instance.generateDocument();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void test01() {

        Net dom4j = new Net();
        dom4j.generateDocument();
    }

    @Test
    public void test02() {
        File f = new File("sit.xml");
        Net dom4j = new Net();
        try {
            dom4j.getNet(f);
        } catch (DocumentException e) {
            System.err.println("Soubor nenalezen");
        }
        List places = dom4j.getPlaces();
        Object misto = places.get(2);
    }
    /*
     * @Test public void test02() { Num n1 = new EvenInteger(16); Num n2 = new
     * RomanEvenInteger("20");
     *
     * assertEquals("n1.intValue()", 16, n1.intValue());
     * assertEquals("n1.stringValue()", "16", n1.stringValue());
     *
     * assertEquals("n2.intValue()", 20, n2.intValue());
     * assertEquals("n2.stringValue()", "20", n2.stringValue()); }
     *
     * @Test public void test03() { Num n1 = new EvenInteger(11); Num n2 = new
     * RomanEvenInteger("23");
     *
     * Num n3 = n1.plus(n2); Num n4 = n2.plus(n1);
     *
     * assertEquals("n3.intValue()", 36, n3.intValue());
     * assertEquals("n4.intValue()", 36, n4.intValue()); }
     *
     * @Test public void test04() { Num n1 = new EvenInteger(11); Num n2 = new
     * RomanEvenInteger("23");
     *
     * assertTrue("EvenInteger je odvozen od AbstractInteger", n1 instanceof
     * AbstractInteger); assertFalse("RomanEvenInteger neni odvozen od
     * AbstractInteger", n2 instanceof AbstractInteger); }
     *
     *
     * @Test public void test05() { Stack s = new Stack(2); Num n1 = new
     * EvenInteger(11); Num n2 = new RomanEvenInteger("23"); Num n3 = new
     * RomanEvenInteger("45");
     *
     * Num t1 = new RomanEvenInteger("24"); Num t2 = new RomanEvenInteger("12");
     * Num t3 = new EvenInteger(12);
     *
     * assertTrue("Vlozeni n1", s.push(n1)); assertTrue("Vlozeni n2",
     * s.push(n2)); assertFalse("Vlozeni n3", s.push(n3));
     *
     * assertTrue("Vrchol zasobniku obsahuje rim. cislo 24", s.contains(t1));
     * assertTrue("Odstraneni vrcholu zasobniku", s.pop()); assertFalse("Vrchol
     * zasobniku neobsahuje rim. cislo 12", s.contains(t2)); assertTrue("Vrchol
     * zasobniku obsahuje cislo 12", s.contains(t3)); assertTrue("Odstraneni
     * vrcholu zasobniku", s.pop());
     *
     * assertFalse("Zasobnik je prazdny", s.pop());
     *
     * assertEquals("Hash code n2 a t1 je shodny", n2.hashCode(),
     * t1.hashCode()); assertEquals("Hash code n1 a t3 je shodny",
     * n1.hashCode(), t3.hashCode()); }
     *
     * @Test public void test06() { assertTrue("Trida AbstractInteger ma byt
     * abstraktni.", Modifier.isAbstract(AbstractInteger.class.getModifiers()));
     * try { assertTrue("Metoda AbstractInteger.validate(int) ma byt
     * abstraktni.",
     * Modifier.isAbstract(AbstractInteger.class.getDeclaredMethod("validate",
     * int.class).getModifiers())); } catch (NoSuchMethodException ex) {
     * ex.printStackTrace(); fail("Metoda AbstractInteger.validate(int) neni
     * deklarovana"); } }
     */
}

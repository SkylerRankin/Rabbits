package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import RabbitsAndRiver.Configuration;

public class ConfigurationTest {
    
    @Test
    public void testSimpleEquals() {
        Set<String> l1 = new HashSet<>();
        Set<String> r1 = new HashSet<>();
        l1.add("A1");
        l1.add("C1");
        r1.add("A2");
        r1.add("C2");
        Configuration c1 = new Configuration(4, 0, l1, r1);
        Set<String> l2 = new HashSet<>();
        Set<String> r2 = new HashSet<>();
        l2.add("A1");
        l2.add("C1");
        r2.add("A2");
        r2.add("C2");
        Configuration c2 = new Configuration(4, 0, l2, r2);
        assertEquals(c1, c2);
        assertEquals(c2, new Configuration(4, 0, l1, r1));
    }
    
    @Test
    public void testSetContains() {
        
        Set<Configuration> set = new HashSet<>();
        
        Set<String> l1 = new HashSet<>();
        Set<String> r1 = new HashSet<>();
        l1.add("A1");
        l1.add("C1");
        r1.add("A2");
        r1.add("C2");
        set.add(new Configuration(4, 0, l1, r1));
        
        Set<String> l2 = new HashSet<>();
        Set<String> r2 = new HashSet<>();
        l2.add("A1");
        l2.add("C1");
        r2.add("A2");
        r2.add("C2");
        Configuration c2 = new Configuration(4, 0, l2, r2);
        System.out.println(c2);
        for (Configuration s : set)  {
            System.out.println(c2.equals(s)+" "+s);
        }
        
        assertTrue(set.contains(c2));
        assertTrue(set.contains(new Configuration(4, 0, l1, r1)));
        assertFalse(set.contains(new Configuration(4, 0, l1, l1)));
        assertFalse(set.contains(new Configuration(4, 0, r1, r1)));
        
        
    }
    
    @Test
    public void testEquals2() {
        
    }


}

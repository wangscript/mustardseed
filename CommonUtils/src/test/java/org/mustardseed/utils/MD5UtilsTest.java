package org.mustardseed.utils;

import java.io.File;
import org.junit.*;
import static org.junit.Assert.*;

public class MD5UtilsTest {
    @Before
    public void init() {
    }
    @Test
    public void toJsonTest() {
	File file = new File("E:/test.txt");
	String a = MD5Utils.hash(file);
	String b = MD5Utils.hash(file);
	assertEquals(a,b);
    }

}
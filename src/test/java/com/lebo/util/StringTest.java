package com.lebo.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author: Wei Liu
 * Date: 13-9-22
 * Time: PM8:00
 */
public class StringTest {
    @Test
    public void format() {
        assertEquals("a   abc", String.format("%-4sabc", "a"));
        assertEquals("   aabc", String.format("%4sabc", "a"));
    }
}

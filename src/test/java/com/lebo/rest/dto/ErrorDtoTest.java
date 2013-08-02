package com.lebo.rest.dto;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author: Wei Liu
 * Date: 13-7-24
 * Time: PM7:21
 */
public class ErrorDtoTest {

    @Test
    public void toJson() {
        assertEquals(ErrorDto.BAD_REQUEST.toJson(), ErrorDto.BAD_REQUEST.toJson());
    }
}

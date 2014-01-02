package com.lebo.entity;

import java.util.LinkedHashSet;

/**
 * @author: Wei Liu
 * Date: 13-12-31
 * Time: PM5:05
 */
public class Robot {

    private LinkedHashSet<String> groups;

    public LinkedHashSet<String> getGroups() {
        return groups;
    }

    public void setGroups(LinkedHashSet<String> groups) {
        this.groups = groups;
    }
}

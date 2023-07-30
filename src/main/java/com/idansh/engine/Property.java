package com.idansh.engine;

public class Property extends HasUniqueName {
    String type = null; // todo: Change to a Class of its own, or enum
    Integer range = null;      // If exists
    Boolean isRandom = null;   // Was the property assigned randomly
}

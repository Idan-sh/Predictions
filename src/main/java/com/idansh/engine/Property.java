package com.idansh.engine;


/**
 * Property for an entity
 */
public class Property extends HasUniqueName {
    String type = null; // todo: convert to enum
    Integer range = null;      // If exists
    Boolean isRandom = null;   // Was the property assigned randomly
}

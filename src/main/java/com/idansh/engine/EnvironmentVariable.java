package com.idansh.engine;

public class EnvironmentVariable extends HasUniqueName{
    Object value;

    // todo: convert both to enums
    Class<?> type;                  // Possible types: decimal, float, boolean, string
    Object rangeBottom, rangeTop;   // Possible types: decimal, float

}

package com.idansh.dto.action;

import java.util.*;

public class ActionDTO {
    String type;
    String mainEntityContext;
    String secondaryEntityContext;
    Map<String, String> argumentsMap;
    Map<String, String> extraInfoMap;

    public ActionDTO(String type, String mainEntityContext, String secondaryEntityContext) {
        this.type = type;
        this.mainEntityContext = mainEntityContext;
        this.secondaryEntityContext = secondaryEntityContext;
        this.argumentsMap = new LinkedHashMap<>();
        this.extraInfoMap = new LinkedHashMap<>();
    }

    public void addArgument(String argName, String argValue) {
        argumentsMap.put(argName, argValue);
    }

    public void addExtraInfo(String infoName, String infoValue) {
        extraInfoMap.put(infoName, infoValue);
    }

    public String getType() {
        return type;
    }

    public String getMainEntityContext() {
        return mainEntityContext;
    }

    public String getSecondaryEntityContext() {
        return secondaryEntityContext;
    }

    public Map<String, String> getArgumentsMap() {
        return argumentsMap;
    }

    public Map<String, String> getExtraInfoMap() {
        return extraInfoMap;
    }
}

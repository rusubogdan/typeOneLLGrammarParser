package com.company;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParsingMatrix {

    private Map<String, Map<String, String>> parsingMatrix;
    private List<Map<String, String>> firstSet;
    private List<Map<String, String>> followSet;


    public ParsingMatrix() {
        parsingMatrix = new HashMap<String, Map<String, String>>();
    }

    public void createParsingMatrix(Map<String, List<Map<String, String>>> map) {
        firstSet = map.get("first");
        followSet = map.get("follow");


    }


}

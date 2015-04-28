package com.company;

import java.io.*;
import java.util.*;

public class FirstSetAndFollowSet {

    private String first[];
    private String follow[];
    private Grammar cfGrammar;

    public FirstSetAndFollowSet() {
    }

    public Map<String, Map<String, String>> resolve(Grammar buildInGrammar) throws IOException {
        cfGrammar = buildInGrammar;
        int nonTerminalsLength = cfGrammar.getNonTerminals().size();
        first = new String[nonTerminalsLength];

        for (int i = 0; i < nonTerminalsLength; i++)
            first[i] = removeDuplicates(first(i));

        follow = new String[nonTerminalsLength];

        for (int i = 0; i < nonTerminalsLength; i++)
            follow[i] = removeDuplicates(follow(i));

        Map map = new HashMap();

        Map<String, String> firstList = new HashMap<String, String>();
        Map<String, String> followList = new HashMap<String, String>();

        // entrySet : 'S' - firstS
        // entrySet : 'A' - firstA ...

        int k = 0;
        for (String nonTerminal : cfGrammar.getNonTerminals()) {
            firstList.put(nonTerminal, first[k++]);
        }

        k = 0;
        for (String nonTerminal : cfGrammar.getNonTerminals()) {
            followList.put(nonTerminal, follow[k++]);
        }

        map.put("first", firstList);
        map.put("follow", followList);

        return map;
    }

    private String first(int i) {
        boolean found;
        String temp = "";
        String str = "";

        String currentNonTerminal = cfGrammar.getNonTerminals().get(i);

        // for every productions of nonTerminal
        for (int j = 0; j < cfGrammar.getProductionsOfNonTerminal(currentNonTerminal).size(); j++) {
            String productionRule = cfGrammar
                    .getProductionsOfNonTerminal(currentNonTerminal).get(j);

            for (int k = 0; k < productionRule.length(); k++) {
                // if a nonTerminal is found in the production rule, resolve first set for it
                found = false;
                // for every terminal in the nonTerminal list
                for (int l = 0; l < cfGrammar.getNonTerminals().size(); l++) {
                    // if current production contains a nonTerminal, resolve first of that nonTerminal
                    if (productionRule.charAt(k) == cfGrammar.getNonTerminals().get(l).charAt(0)) {
                        //  X → Y1Y2..Yk then add first(Y1Y2..Yk) to first(X)
                        str = first(l);
                        // when epsilon production is the only nonTerminal production
                        // If X -> eps is a production, then add eps to FIRST(X)
                        if (!(str.length() == 1 && str.charAt(0) == '@')) {
                            temp = temp + str;
                        }

                        found = true;
                        break;
                    }
                }
                if (found) {
                    // if eps is found in the production add the next nonTerminal 's first set to current first set
                    if (str.contains("@")) {
                        continue;
                    }
                } else {
                    temp = temp + productionRule.charAt(k);
                }
                break;
            }
        }

        return temp;
    }

    private String follow(int i) {
        char production[], chr[];
        String temp = "";
        boolean found = false;
        // first production need to have a "$" symbol at th end
        if (i == 0) {
            temp = "$";
        }

        // for every nonTerminals
        for (int j = 0; j < cfGrammar.getNonTerminals().size(); j++) {
            String currentNonTerminal = cfGrammar.getNonTerminals().get(j);
            List<String> currentProductions = cfGrammar
                    .getProductionsOfNonTerminal(currentNonTerminal);
            // for every productions of the nonTerminal
            for (int k = 0; k < currentProductions.size(); k++) {
                String currentProduction = currentProductions.get(k);
                production = currentProduction.toCharArray();

                // for every character in production
                for (int l = 0; l < production.length; l++) {

                    if (production[l] == cfGrammar.getNonTerminals().get(i).charAt(0)) {

                        // add the last terminal to follow set
                        if (l == production.length - 1) {
                            if (j < i) {
                                temp = temp + follow[j];
                            }
                        } else {
                            // for every nonTerminals
                            for (int m = 0; m < cfGrammar.getNonTerminals().size(); m++) {

                                if (production[l + 1] == cfGrammar.getNonTerminals().get(m).charAt(0)) {
                                    chr = first[m].toCharArray();
                                    for (int n = 0; n < chr.length; n++) {
                                        if (chr[n] == '@') {
                                            if (l + 1 == production.length - 1) {
                                                temp = temp + follow(j);
                                            } else {
                                                temp = temp + follow(m);
                                            }
                                        } else {
                                            temp = temp + chr[n];
                                        }
                                    }
                                    found = true;
                                }
                            }
                            if (!found) {
                                temp = temp + production[l + 1];
                            }
                        }
                    }
                }
            }
        }
        return temp;
    }

    private String removeDuplicates(String setString) {
        char ch;
        boolean seen[] = new boolean[256];
        StringBuilder sb = new StringBuilder(seen.length);
        for (int i = 0; i < setString.length(); i++) {
            ch = setString.charAt(i);
            if (!seen[ch]) {
                seen[ch] = true;
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
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
            first[i] = removeDuplicates(first1(i));

        follow = new String[nonTerminalsLength];

        for (int i = 0; i < nonTerminalsLength; i++)
            follow[i] = removeDuplicates(follow1(i));

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

    private String first1(int i) {
        boolean found;
        String temp = "";
        String str = "";

        String currentNonTerminal = cfGrammar.getNonTerminals().get(i);

        // nr of productions/substitutions
        for (int j = 0; j < cfGrammar.getProductionsOfNonTerminal(currentNonTerminal).size(); j++) {
            // when non terminal has epsilon production

            String productionRule = cfGrammar
                    .getProductionsOfNonTerminal(currentNonTerminal).get(j);

            for (int k = 0; k < productionRule.length(); k++) {
                found = false;
                // finding non terminal
                for (int l = 0; l < cfGrammar.getNonTerminals().size(); l++) {
                    // for non terminal in first set
                    if (productionRule.charAt(k) == cfGrammar.getNonTerminals().get(l).charAt(0)) {
                        str = first1(l);
                        // when epsilon production is the only non terminal production
                        if (!(str.length() == 1 && str.charAt(0) == '9')) {
                            temp = temp + str;
                        }
                        found = true;
                        break;
                    }
                }
                if (found) {
                    if (str.contains("9")) {
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

    private String follow1(int i) {
        char pro[], chr[];
        String temp = "";
        boolean found = false;
        if (i == 0) {
            temp = "$";
        }

        for (int j = 0; j < cfGrammar.getNonTerminals().size(); j++) {
            String currentNonTerminal = cfGrammar.getNonTerminals().get(j);
            List<String> currentProductions = cfGrammar
                    .getProductionsOfNonTerminal(currentNonTerminal);
            for (int k = 0; k < currentProductions.size(); k++) {
                String currentProduction = currentProductions.get(k);
                pro = currentProduction.toCharArray();

                for (int l = 0; l < pro.length; l++) {

                    if (pro[l] == cfGrammar.getNonTerminals().get(i).charAt(0)) {

                        if (l == pro.length - 1) {
                            if (j < i) {
                                temp = temp + follow[j];
                            }
                        } else {
                            for (int m = 0; m < cfGrammar.getNonTerminals().size(); m++) {

                                if (pro[l + 1] == cfGrammar.getNonTerminals().get(m).charAt(0)) {
                                    chr = first[m].toCharArray();
                                    for (int n = 0; n < chr.length; n++) {
                                        if (chr[n] == '9') {
                                            if (l + 1 == pro.length - 1) {
                                                temp = temp + follow1(j);
                                            } else {
                                                temp = temp + follow1(m);
                                            }
                                        } else {
                                            temp = temp + chr[n];
                                        }
                                    }
                                    found = true;
                                }
                            }
                            if (!found) {
                                temp = temp + pro[l + 1];
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
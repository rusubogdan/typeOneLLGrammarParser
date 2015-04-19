package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Grammar {

    private List<Map<String, String>> substitutions;
    private List<String> nonTerminals;
    private List<String> terminals;

    public Grammar() {
    }

    public Integer getNumberOfProductionOfNonTerminal(String nonTerminal) {
        Integer count = 0;
        for (Map substitution : substitutions) {
            if (substitution.get("nonTerminal").equals(nonTerminal)) {
                count ++;
            }
        }
        return count;
    }

    public List<String> getProductionsOfNonTerminal(String nonTerminal) {
        List<String> productions = new ArrayList<String>();
        for (Map substitution : substitutions) {
            if (substitution.get("nonTerminal").equals(nonTerminal)) {
                productions.add((String) substitution.get("rule"));
            }
        }
        return productions;
    }

    public List<Map<String, String>> getSubstitutions() {
        return substitutions;
    }

    public void setSubstitutions(List<Map<String, String>> substitutions) {
        this.substitutions = substitutions;
    }

    public List<String> getTerminals() {
        return terminals;
    }

    public void setTerminals(List<String> terminals) {
        this.terminals = terminals;
    }

    public List<String> getNonTerminals() {
        return nonTerminals;
    }

    public void setNonTerminals(List<String> nonTerminals) {
        this.nonTerminals = nonTerminals;
    }
}

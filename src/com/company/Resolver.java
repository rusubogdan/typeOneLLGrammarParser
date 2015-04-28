package com.company;

import java.io.*;
import java.util.*;

public class Resolver {

    private FileInputStream inputStream = null;
    private FileOutputStream outputStream = null;
    private PrintWriter printWriter = null;
    private Grammar cfGrammar;
    private ParsingMatrix parsingMatrix;
    private Map<String, String> firstSet;
    private Map<String, String> followSet;
    private FirstSetAndFollowSet firstSetAndFollowSet;
    private String inputWord;
    private Map<String, List<Map<String, String>>> matrix;

    public Resolver() {
        cfGrammar = new Grammar();
        parsingMatrix = new ParsingMatrix();
        firstSetAndFollowSet = new FirstSetAndFollowSet();
        matrix = new HashMap<String, List<Map<String, String>>>();
    }

    public void readGrammar() {
        try {
            inputStream = new FileInputStream("grammarInput2.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            br.readLine(); // skip the first line of the input file

            String nonTerminalsString = br.readLine();
            List<String> nonTerminalsList = Arrays.asList(nonTerminalsString.split(" "));

            br.readLine(); // skip the third line of the input file

            String terminalsString = br.readLine();
            List<String> terminalsList = Arrays.asList(terminalsString.split(" "));

            cfGrammar.setNonTerminals(nonTerminalsList);
            cfGrammar.setTerminals(terminalsList);

            // read substitutions

            br.readLine(); // skip the fifth line on the input file

            String substitutionString;
            List<Map<String, String>> substitutions = new ArrayList<Map<String, String>>();
            substitutionString = br.readLine();
            while (substitutionString != null && substitutionString.length() > 0) {
                Map<String, String> substitution = new HashMap<String, String>();
                List<String> productionRule = Arrays.asList(substitutionString.split(" -> "));
                substitution.put("nonTerminal", productionRule.get(0));
                substitution.put("rule", productionRule.get(1));
                substitutions.add(substitution);

                substitutionString = br.readLine();
            }

            cfGrammar.setSubstitutions(substitutions);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createFirstAndFollowSets() {
        try {
            Map<String, Map<String, String>> map = firstSetAndFollowSet.resolve(cfGrammar);
            firstSet = map.get("first");
            followSet = map.get("follow");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createParsingMatrix() {
        // rule 1 For production “A->w”, for each terminal in FIRST(A),
        // put w as an entry in the A row under the column for that terminal.

        // for every non nonTerminal
        for (String nonTerminal : cfGrammar.getNonTerminals()) {
            // for every production of that nonTerminal
            List<String> productions = cfGrammar.getProductionsOfNonTerminal(nonTerminal);
            int k = 0;
            String subs = "";

            List<Map<String, String>> nonTerminalList = new ArrayList<Map<String, String>>();

            // while prod k || first(NT)(k)
            while (k < productions.size()
                    && (productions.get(k) != null || (Character) firstSet.get(nonTerminal).charAt(k) != null)) {
                // if prod k && first(NT)(k)
                if (productions.get(k) != null && (Character) firstSet.get(nonTerminal).charAt(k) != null
                        && firstSet.get(nonTerminal).charAt(k) != '@') {
                    // NT -> first(NT)(k): prod k
                    Map<String, String> myMap = new HashMap<String, String>();
                    myMap.put(String.valueOf(firstSet.get(nonTerminal).charAt(k)), productions.get(k));
                    nonTerminalList.add(myMap);

                    // subs = first(NT)(k);
                    subs = String.valueOf(firstSet.get(nonTerminal).charAt(k));
                    k++;
                } else

                    // if prod k && prod k == eps
                    if (productions.get(k) != null && productions.get(k).equals("@")) {
                        // for i follow NT
                        for (String character : followSet.get(nonTerminal).split("")) {
                            // NT -> follow(NT)(i) : eps
                            Map<String, String> myMap = new HashMap<String, String>();
                            myMap.put(character, "@");
                            nonTerminalList.add(myMap);

                            k++;
                        }
                    } else {
                        // if ! prod k && first(NT)(k)
                        while (productions.size() < k && String.valueOf(firstSet.get(nonTerminal).charAt(k)) != null) {
                            // NT -> subs : prod k
                            Map<String, String> myMap = new HashMap<String, String>();
                            myMap.put(subs, String.valueOf(firstSet.get(nonTerminal).charAt(k)));
                            nonTerminalList.add(myMap);
                            // k ++ or k = -1
                            k++;
                        }
                    }
                matrix.put(nonTerminal, nonTerminalList);
            }
        }
    }

    private void readInput() {
        try {
            inputStream = new FileInputStream("grammarInputWord.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            inputWord = br.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeOnOutput(String message) {
        PrintWriter printWriter = new PrintWriter(outputStream);

        printWriter.print(message);
        printWriter.flush();

    }

    private String extractTerminalsSequence(String sequence) {
        String seq = "";

        for (String s : sequence.split("")) {
            if ((Character) s.charAt(0) >= (Character) "a".charAt(0) && (Character) s.charAt(0) <= (Character) "z".charAt(0)) {
                seq += s;
            } else {
                return seq;
            }
        }

        return seq;
    }

    private Boolean acceptWord() {
        List<String> inputWordArray = Arrays.asList(inputWord.split(""));
        Collections.reverse(inputWordArray);
        Stack<String> inputStack = new Stack();

        Stack<String> grammarStack = new Stack();
        grammarStack.push("$");
        grammarStack.push(cfGrammar.getNonTerminals().get(0)); // S

        String outputMessage = /*cfGrammar.getNonTerminals().get(0) + "\n"*/ "";

        outputMessage += ""/*cfGrammar.getNonTerminals().get(0) +
                " -> " + cfGrammar.getProductionsOfNonTerminal(cfGrammar.getNonTerminals().get(0)).get(0) + "\n"*/
//                                        + cfGrammar.getProductionsOfNonTerminal("S") + "\n";
        ;
        String intermediaryOutput = ""; // abABca - > ab
        String remainingProductionOutput = intermediaryOutput; // abABs -> ABca
        intermediaryOutput += /*extractTerminalsSequence(
                cfGrammar.getProductionsOfNonTerminal(cfGrammar.getNonTerminals().get(0))
                        .get(0))*/"";

        try {
            outputStream = new FileOutputStream("grammarOutput.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        printWriter = new PrintWriter(outputStream);

        for (String letter : inputWordArray) {
            inputStack.push(letter);
        }

        do {

            String grammarTopElement = grammarStack.pop();
            String inputTopElement = inputStack.pop();

            if (grammarTopElement.equals("@")) {
//                intermediaryOutput += "@";

                // if is epsilon, skip
                inputStack.push(inputTopElement);
                continue;
            }

            if (grammarTopElement.equals(inputTopElement)) {
                intermediaryOutput += inputTopElement;

//                intermediaryOutput += remainingProductionOutput;

                outputMessage += intermediaryOutput;
                outputMessage += '\n';
                continue;
            }

            if (!grammarTopElement.equals(inputTopElement)) {
                // put back the element
                inputStack.push(inputTopElement);

                if (cfGrammar.getNonTerminals().contains(grammarTopElement)) {
                    String production = findProduction(grammarTopElement, inputTopElement);

                    if (production != null) {
                        List<String> productionCharacters = Arrays.asList(production.split(""));

//                        remainingProductionOutput = "";

//                        for (String character : productionCharacters) {
////                            outputMessage += character;
//                            remainingProductionOutput += character;
//                        }



                        Collections.reverse(productionCharacters);

                        outputMessage += grammarTopElement + " -> ";

                        for (String character : productionCharacters) {
//                            outputMessage += character;
                            grammarStack.push(character);
                        }

                        outputMessage += production;

                        outputMessage += '\n';

//                        outputMessage += remainingProductionOutput + '\n';

//                        intermediaryOutput += extractTerminalsSequence(production);
//                        outputMessage += intermediaryOutput;
//                        outputMessage += '\n';
                    }
                } else {
                    writeOnOutput("The word " + inputWord + " is not accepted by the grammar");

                    return false;
                }
            } else {
                writeOnOutput("The word " + inputWord + " is not accepted by the grammar");

                return false;
            }
        } while (!grammarStack.empty());

        outputMessage += '\n';
        outputMessage += "Input accepted";

        writeOnOutput(outputMessage);

        return true;
    }

    private String findProduction(String nonTerminal, String terminal) {
        List<Map<String, String>> productionsOfNonTerminal = matrix.get(nonTerminal);

        for (Map<String, String> production : productionsOfNonTerminal) {
            if (production.get(terminal) != null) {
                return production.get(terminal);
            }
        }

        return null;
    }

    public String getUnusedSymbol() {
        String unusedSymbol = "A";

        while (cfGrammar.getNonTerminals().contains(unusedSymbol)) {
            unusedSymbol += 1;
        }

        return unusedSymbol;
    }

    public void removeEpsilonDerivations() {
        cfGrammar.getSubstitutions();
    }

    public Boolean resolve() {
        readGrammar();


        createFirstAndFollowSets();
        createParsingMatrix();
        readInput();

        return acceptWord();
    }
}

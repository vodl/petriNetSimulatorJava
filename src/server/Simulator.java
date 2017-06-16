package server;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import net.*;
import org.dom4j.DocumentException;

/**
 * Trida pro simulaci vypoctu
 * @author Jan Kebisek
 */
public class Simulator {

    boolean condition = true;
    boolean last = true;
    List edges;
    List transitions;
    List closeTrans;
    List closeEdges;
    List places;
    List tokenList;
    String myNet;
    Net net;
    boolean stop = false;
    boolean step;
    boolean constant = false;
    
    /**
     * konstruktor simulacie
     * @param myNet 
     */
    public Simulator(String myNet, boolean step) {
        this.step=step;
        this.myNet = myNet;
        net = new Net();
        try {
            net.getNet2(myNet);
        } catch (DocumentException ex) {
            System.out.println(ex.getMessage());
        }
        places = net.getPlaces();
        transitions = net.getTransitions();
        edges = net.getEdges();
        closeTrans = new ArrayList();
        closeEdges = new ArrayList();
        simulate();
    }

    
    
    /**
     * spusti simulaciu
     * @return XML retazec s ulozenou sietou
     */
    public String simulate() {
        while (true) {
            //System.out.printf("------------------***********************************----------\n");
            for (Iterator x = transitions.listIterator(); x.hasNext();) {                //prejdem vsetky straze
                Transition transition = (Transition) x.next();
                if (isLastTransition(transition)) {
                    if (!closeTrans.contains(transition)) {
                        closeTrans.add(transition);
                        //System.out.printf("---------------Posledny je prechod: %d\n", transition.getId() / 2 + 1);
                        replaceTokens(transition);
                        
                    }
                    if(step && stop){
                        net.getPlaceList().setList(this.places);
                        return this.net.saveToXml().asXML();
                    }
                    
                }
            }
            
            
            Edge edgeToRemove;
            for (Iterator x = closeTrans.listIterator(); x.hasNext();) { // z povodneho zoznamu strazi vymazem aktualne posledne
                Transition transition = (Transition) x.next();
                for (Iterator y = edges.listIterator(); y.hasNext();) {    // prejdem vsetky hrany 
                    Edge edge = (Edge) y.next();
                    if (edge.getSource() == transition.getId()) {
                        closeEdges.add(edge);
                        break;
                    }
                }
            }
            if (transitions.size() == closeTrans.size()) {
                break;
            }
        }
        
        
        net.getPlaceList().setList(this.places);
        return this.net.saveToXml().asXML();
    }
    
    
    
    /**
     * najde aktualne posledny prechod
     * @param transition  prechod
     * @return booleanovsku hodnotu urcujucu ci ide o posledny prechod
     */
    public boolean isLastTransition(Transition transition) {
        for (Iterator x = edges.listIterator(); x.hasNext();) {    // prejdem vsetky hrany 
            Edge edge = (Edge) x.next();
            if (edge.getTarget() == transition.getId()) {
                Place place = (Place) places.get(edge.getSource() / 2);
                for (Iterator y = edges.listIterator(); y.hasNext();) {    // prejdem vsetky hrany 
                    Edge edge2 = (Edge) y.next();
                    if (edge2.getTarget() == place.getId() && (!closeEdges.contains(edge2))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    
    
    /**
     * premiestni tokeny
     * @param transition aktualne posledny prechod
     */
    public void replaceTokens(Transition transition) {
        int constVal=0;
        Place place1 = null;
        Place place2 = null;
        String operator = "xx";
        if (transition.getCondition().compareTo("empty") == 0) {
            return;
        }
        String str = transition.getCondition();
        String strArray[] = str.split("&");
        for (int i = 0; i < strArray.length; i++) {    // prejdem vsetky casti(podmienky)
            String exprTokens[] = strArray[i].split(" ");
            if (exprTokens.length != 3) {
                return;
            }
            for (int x = 0; x < 3; x++) {
                if (x == 0) {         // prvy token
                    for (Iterator z = edges.listIterator(); z.hasNext();) {  // prejdem vsetky hrany 
                        Edge edge = (Edge) z.next();
                        if (edge.getVariable() == null) {
                            return;
                        }
                        if (edge.getTarget() == transition.getId() && edge.getVariable().compareTo(exprTokens[x]) == 0) {
                            Place place = (Place) places.get(edge.getSource() / 2);
                            place1 = place;
                            if (place1.getTokens().isEmpty()) {
                                return;
                            }
                        }
                    }

                } else if (x == 1) {   // operator
                    operator = exprTokens[x];

                } else if (x == 2) {                 // druhy token
                    if(isInt(exprTokens[x])){
                        constant = true;
                        constVal= Integer.parseInt(exprTokens[x]);
                    }
                    else{
                        for (Iterator z = edges.listIterator(); z.hasNext();) {    // prejdem vsetky hrany 
                            Edge edge = (Edge) z.next();
                            if (edge.getVariable() == null) {
                                return;
                            }
                            if (edge.getTarget() == transition.getId() && edge.getVariable().compareTo(exprTokens[x]) == 0) {
                                Place place = (Place) places.get(edge.getSource() / 2);
                                place2 = place;
                                if (place2.getTokens().isEmpty()) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }

            /********** skontrolujem podmienku ***********/
            if (!checkCondition(place1, place2, operator,constVal)) {
                constant = false;
                break;
            }
            constant = false;

            /*System.out.printf("Dobre tokeny1:");
            for (Iterator x = place1.getRightTokens().listIterator(); x.hasNext();) { // vypisem si tokeny 
                Token token = (Token) x.next();
                System.out.printf("%d,", token.getValue());
            }
            System.out.printf("\nDobre tokeny2:");
            for (Iterator x = place2.getRightTokens().listIterator(); x.hasNext();) { // vypisem si tokeny 
                Token token = (Token) x.next();
                System.out.printf("%d,", token.getValue());
            }
            System.out.printf("\n\n");*/
        }
        
        /********   predam vysledne tokeny simulacie "o poschodie" nizsie   ***********/
        for (Iterator x = edges.listIterator(); x.hasNext();) {    // prejdem vsetky hrany 
            Edge edge = (Edge) x.next();
            if (edge.getSource() == transition.getId()) {
                Place place = (Place) places.get(edge.getTarget() / 2);
                makeChange(transition, place);
                //break;
            }
        }

    }
    
    
    
    /**
     * zisti ci sa jedna o konstantu
     * @param str string s premenou alebo konstantou
     * @return booleanovska hodnota urcujuca ci string predstavuje konstantu
     */
    public boolean isInt(String str)
    {
        try
        {
            Integer.parseInt(str);
            return true;
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
    }
    
    /**
     * vyhodnoti podmienku
     * @param place1 miesto predstavujuce prvu premennu
     * @param place2 miesto predstavujuce druhu premennu
     * @param operator operator medzi operandami
     * @param constVal pripadna konstanta
     * @return booleanovska hodnota urcujuca, ci podmienka plati
     */
    public boolean checkCondition(Place place1, Place place2, String operator, int constVal) {
        /**
         * ******* prechadzam zoznam zlych tokenov v 1 mieste  *********
         */
        for (Iterator x = place1.getTokens().listIterator(); x.hasNext();) {
            Token token1 = (Token) x.next();
            //System.out.printf("%d",token.getValue());
            if(operator.compareTo("<") == 0){
                    /**
                     * ** prechadzam zoznam zlych tokenov v 2. mieste ***
                     */
                    if(constant){
                        if (token1.getValue() < constVal) {
                            if (!place1.getRightTokens().contains(token1)) {
                               place1.addRightToken(token1);
                            }
                        }
                    }else{
                       for (Iterator y = place2.getTokens().listIterator(); y.hasNext();) {
                            Token token2 = (Token) y.next();
                            if (token1.getValue() < token2.getValue()) {
                                if (!place1.getRightTokens().contains(token1)) {
                                    place1.addRightToken(token1);
                                }
                                if (!place2.getRightTokens().contains(token2)) {
                                    place2.addRightToken(token2);
                                }
                            }
                        } 
                    }
            }else if(operator.compareTo(">") == 0){
                    /**
                     * ** prechadzam zoznam zlych tokenov v 2. mieste ***
                     */
                    if(constant){
                        if (token1.getValue() > constVal) {
                            if (!place1.getRightTokens().contains(token1)) {
                               place1.addRightToken(token1);
                            }
                        }
                    }else{
                       for (Iterator y = place2.getTokens().listIterator(); y.hasNext();) {
                            Token token2 = (Token) y.next();
                            if (token1.getValue() > token2.getValue()) {
                                if (!place1.getRightTokens().contains(token1)) {
                                    place1.addRightToken(token1);
                                }
                                if (!place2.getRightTokens().contains(token2)) {
                                    place2.addRightToken(token2);
                                }
                            }
                        } 
                    }
            }else if(operator.compareTo("<=") == 0){
                    /**
                     * ** prechadzam zoznam zlych tokenov v 2. mieste ***
                     */
                    if(constant){
                        if (token1.getValue() <= constVal) {
                            if (!place1.getRightTokens().contains(token1)) {
                               place1.addRightToken(token1);
                            }
                        }
                    }else{
                       for (Iterator y = place2.getTokens().listIterator(); y.hasNext();) {
                            Token token2 = (Token) y.next();
                            if (token1.getValue() <= token2.getValue()) {
                                if (!place1.getRightTokens().contains(token1)) {
                                    place1.addRightToken(token1);
                                }
                                if (!place2.getRightTokens().contains(token2)) {
                                    place2.addRightToken(token2);
                                }
                            }
                        } 
                    }
            }else if(operator.compareTo(">=") == 0){
                    /**
                     * ** prechadzam zoznam zlych tokenov v 2. mieste ***
                     */
                    if(constant){
                        if (token1.getValue() >= constVal) {
                            if (!place1.getRightTokens().contains(token1)) {
                               place1.addRightToken(token1);
                            }
                        }
                    }else{
                       for (Iterator y = place2.getTokens().listIterator(); y.hasNext();) {
                            Token token2 = (Token) y.next();
                            if (token1.getValue() >= token2.getValue()) {
                                if (!place1.getRightTokens().contains(token1)) {
                                    place1.addRightToken(token1);
                                }
                                if (!place2.getRightTokens().contains(token2)) {
                                    place2.addRightToken(token2);
                                }
                            }
                        } 
                    }
            }else if(operator.compareTo("==") == 0){
                    /**
                     * ** prechadzam zoznam zlych tokenov v 2. mieste ***
                     */
                    if(constant){
                        if (token1.getValue() == constVal) {
                            if (!place1.getRightTokens().contains(token1)) {
                               place1.addRightToken(token1);
                            }
                            
                        }
                    }else{
                       for (Iterator y = place2.getTokens().listIterator(); y.hasNext();) {
                            Token token2 = (Token) y.next();
                            if (token1.getValue() == token2.getValue()) {
                                if (!place1.getRightTokens().contains(token1)) {
                                    place1.addRightToken(token1);
                                }
                                if (!place2.getRightTokens().contains(token2)) {
                                    place2.addRightToken(token2);
                                }
                            }
                        } 
                    }
            }else if(operator.compareTo("!=") == 0){
                    /**
                     * ** prechadzam zoznam zlych tokenov v 2. mieste ***
                     */
                    if(constant){
                        if (token1.getValue() != constVal) {
                            if (!place1.getRightTokens().contains(token1)) {
                               place1.addRightToken(token1);
                            }
                        }
                    }else{
                       for (Iterator y = place2.getTokens().listIterator(); y.hasNext();) {
                            Token token2 = (Token) y.next();
                            if (token1.getValue() != token2.getValue()) {
                                if (!place1.getRightTokens().contains(token1)) {
                                    place1.addRightToken(token1);
                                }
                                if (!place2.getRightTokens().contains(token2)) {
                                    place2.addRightToken(token2);
                                }
                            }
                        } 
                    }
            }else{
                    System.err.printf("BAD OPERATOR!\n");
                    return false;
            }
        }
        if (place1.getRightTokens().isEmpty()) {
            return false;
        }
        return true;

    }
    
    /**
     * spravi samotny prechod
     * @param transition aktualny spracovavany prechod
     * @param place miesto do ktoreho pojdu tokeny vysledku prechodu
     */
    public void makeChange(Transition transition, Place place) {
        List usedTokens = new ArrayList();
        String change = transition.getChange();
        int result;
        int sign;
        while (true) {
            sign = 0;   //znamienko +
            result = 0;
            usedTokens.clear();

            String strArray[] = change.split(" ");
            for (int i = 2; i < strArray.length; i++) {
                //System.out.printf("***%s***",strArray[i]);
                if (i % 2 == 1) {
                    if (strArray[i].compareTo("+") == 0) {
                        sign = 0;    // znamienko +
                    } else if (strArray[i].compareTo("-") == 0) {
                        sign = 1;    // znamienko -
                    } else {
                        System.err.printf("Chybne znamienko v prechode!");
                    }
                } else {
                    for (Iterator x = edges.listIterator(); x.hasNext();) {    // prejdem vsetky hrany 
                        Edge edge = (Edge) x.next();
                        if (edge.getTarget() == transition.getId() && strArray[i].compareTo(edge.getVariable()) == 0) {
                            if (sign == 0) {
                                Place place2 = (Place) places.get(edge.getSource() / 2);
                                if (place2.getRightTokens().isEmpty()) {
                                    return;
                                }
                                Token token2 = (Token) place2.getRightTokens().get(0);
                                result += token2.getValue();
                                usedTokens.add(token2);
                                //place2.getTokens().remove(token2);
                                place2.getRightTokens().remove(0);
                                break;
                            }
                            if (sign == 1) {
                                Place place2 = (Place) places.get(edge.getSource() / 2);
                                if (place2.getRightTokens().isEmpty()) {
                                    return;
                                }
                                Token token2 = (Token) place2.getRightTokens().get(0);
                                result -= token2.getValue();
                                usedTokens.add(token2);
                                //place2.getTokens().remove(token2);
                                place2.getRightTokens().remove(0);
                                break;
                            }
                        }
                    }
                }
            }
            
            /********** tu musim mazat **********/
            for (Iterator x = places.listIterator(); x.hasNext();) {    // prejdem vsetky miesta
                Place place5 = (Place) x.next();
                for (Iterator y = usedTokens.listIterator(); y.hasNext();) {    // prejdem vsetky hrany 
                    Token token5 = (Token) y.next();
                    if(place5.getTokens().contains(token5)){
                        place5.getTokens().remove(token5);
                    }
                }
            }
            
            Token token3 = new Token();
            token3.setPosId(place.getId());
            token3.setValue(result);
            place.addToken(token3);
            if(step){
                stop = true;
                return;
            }
        }
    }
}

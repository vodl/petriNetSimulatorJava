/**
 * Soubor s tridou definujici prechody
 * @author Vojtech Dlapal
 */
package net;

/**
 * Trida pro prechod
 * @author Vojtech Dlapal
 */
public class Transition extends NetElement{
    String condition; // urci zda je prechod proveditelny // x > y
    String change; // urci co se stane po provedeni prechodu

    public Transition() {
        super();
        this.condition = "";
        this.change = "";
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    
    
}

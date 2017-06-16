/**
 * Soubor s tridou definujici hrany
 * @author Vojtech Dlapal
 */
package net;

/**
 *
 * @author vodl
 */
public class Edge {
    int target;
    int source;
    String variable; // promena hrany


    public String getVariable() {
        return variable;
    }

    public void setVar(String variable) {
        this.variable = variable;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getSource() {
        return source;
    }

    public int getTarget() {
        return target;
    }
    
    
    
}

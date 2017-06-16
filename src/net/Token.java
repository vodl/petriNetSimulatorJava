/**
 * Soubor s tridou definujici tokeny
 * @author Vojtech Dlapal
 */
package net;

/**
 *
 * @author Vojtech Dlapal <xdlapa01 at stud.fit.vutbr.cz>
 */
public class Token {
    int value; //hodnota napr. 8
    int posId; // ID elementu ve kterem se aktualne nachazi

    public Token() {
        this.value = 0;
        this.posId = 0;
    }

    public int getPosId() {
        return posId;
    }

    public void setPosId(int posId) {
        this.posId = posId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
    public Token(int value) {
        this.value = value;
    }


    
    
}

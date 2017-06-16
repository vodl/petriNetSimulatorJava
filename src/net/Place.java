/**
 * Soubor s tridou definujici misto
 * @author Vojtech Dlapal
 */
package net;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vodl
 */
public class Place extends NetElement{
    public TokenList tokens;
    public TokenList rightTokens;

    public Place() {
        super();
        this.tokens = new TokenList();
        this.rightTokens = new TokenList();
    }
    
    public void addToken(Token token){
        this.tokens.add(token);
    }
    
    public void addRightToken(Token token){
        this.rightTokens.add(token);
    }
    
    public String getName(){
        return "p" + Integer.toString(this.getId()/2); 
    }

    public List getTokens() {
        return tokens.list;
    }
    
    public List getRightTokens() {
        return rightTokens.list;
    }
    
    

    
    
    
}

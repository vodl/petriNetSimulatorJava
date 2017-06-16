/**
 * Soubor s tridou definujici seznam tokenu
 * @author Vojtech Dlapal
 */
package net;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vojtech Dlapal <xdlapa01 at stud.fit.vutbr.cz>
 */
public class TokenList {
    public List<Token> list;

    public TokenList(){
        list = new ArrayList<Token>();
    }

    public void add(Token t){
        list.add(t);
    }
}
/**
 * Soubor s tridou definujici seznamy prechodu
 * @author Vojtech Dlapal
 */
package net;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vojtech Dlapal <xdlapa01 at stud.fit.vutbr.cz>
 */
public class TransitionList {
    public List<Transition> list;

    public TransitionList(){
        list = new ArrayList<Transition>();
    }

    public void add(Transition t){
        list.add(t);
    }
}

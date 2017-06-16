/**
 * Soubor s tridou definujici seznam hran
 * @author Vojtech Dlapal
 */
package net;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vojtech Dlapal <xdlapa01 at stud.fit.vutbr.cz>
 */
public class EdgeList {
    public List<Edge> list;

    public EdgeList(){
        list = new ArrayList<Edge>();
    }

    public void add(Edge e){
        list.add(e);
    }
}

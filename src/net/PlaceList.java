/**
 * Soubor s tridou definujici seznam mist
 *
 * @author Vojtech Dlapal
 */
package net;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vojtech Dlapal <xdlapa01 at stud.fit.vutbr.cz>
 */
public class PlaceList {

    public List<Place> list;

    public PlaceList() {
        list = new ArrayList<Place>();
    }

    public void add(Place p) {
        list.add(p);
    }

    public void setList(List<Place> l) {
        this.list = l;

    }
}

/**
 * Soubor s tridou pro serializaci nejen siti
 *
 * @author Vojtech Dlapal
 */
package net;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Vojtech Dlapal <xdlapa01 at stud.fit.vutbr.cz>
 */
public class Ser {

    XStream xstream;

    public Ser() {
        this.xstream = new XStream(new DomDriver()); // does not require XPP3 library

        xstream.alias("place", Place.class);
        xstream.alias("token", Token.class);
        xstream.alias("transition", Transition.class);
        xstream.alias("net", Net.class);
        xstream.alias("edge", Edge.class);

        /*
         * xstream.addImplicitCollection(EdgeList.class, "list");
         * xstream.addImplicitCollection(TokenList.class, "list");
         * xstream.addImplicitCollection(TransitionList.class, "list");
         * xstream.addImplicitCollection(PlaceList.class, "list");
         */
        //xstream.addImplicitArray(PlaceList.class, "places", Place.class);

    }

    public Net XgetNet(File f) {
        Net net = (Net) this.xstream.fromXML(f);
        return net;
    }

    /**
     * Obecna deserializace objektu knihovnou XStream
     * @param f soubor se serializovanym objektem
     * @return deserializovany objekt
     */
    public Object xDeserialize(File f) {
        Object obj = this.xstream.fromXML(f);
        return obj;
    }

    /**
     * Obecna serializace knihovnou XStream
     * @param obj objekt co bude serializovan
     * @param filename nazev souboru do ktereho bude serializovano
     */
    public void xSerialize(Object obj, String filename) {

        String xml = this.xstream.toXML(obj);

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            out.write(xml);
            out.close();
        } catch (IOException e) {
            System.err.println("Exception ");

        }
    }

    public void XNetToXML(Net net) {

        String xml = this.xstream.toXML(net);

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(net.getName()));
            out.write(xml);
            out.close();
        } catch (IOException e) {
            System.out.println("Exception ");

        }
    }

    public void XNetToXML(Net net, String name) {

        String xml = this.xstream.toXML(net);

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(name));
            out.write(xml);
            out.close();
        } catch (IOException e) {
            System.out.println("Exception ");

        }
    }
}

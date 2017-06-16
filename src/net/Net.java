/**
 * Soubor s tridou reprezentujici sit
 *
 * @author Vojtech Dlapal
 */
package net;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import server.Server;

/**
 * Trida reprezentujici sit
 *
 * @author Vojtech Dlapal
 */
public class Net {

    public EdgeList edges;
    public TransitionList transitions;
    public PlaceList places;
    public String name;
    public String author;
    public int version;

    public Net() {
        this.name = "newnet";
        this.author = "";
        this.version = 0;
        this.edges = new EdgeList();
        this.transitions = new TransitionList();
        this.places = new PlaceList();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public List getEdges() {
        return edges.list;
    }

    public List getPlaces() {
        return places.list;
    }

    public PlaceList getPlaceList() {
        return this.places;
    }

    public void addPlace(Place place) {
        places.add(place);
    }

    public List getTransitions() {
        return transitions.list;
    }

    /**
     * Pomocna metoda naplni Net z DOM dokumentu
     *
     * @param doc
     */
    private void iterateDoc(Document doc) {

        Element root = doc.getRootElement();
        for (Iterator ii = root.attributeIterator(); ii.hasNext();) {
            Attribute attribute = (Attribute) ii.next();

            String s = attribute.getName();

            if ("version".equals(s)) {
                this.version = Integer.parseInt(attribute.getValue());
            } else if ("author".equals(s)) {
                this.author = attribute.getValue();
            } else if ("name".equals(s)) {
                this.name = attribute.getValue();
            }         
        }// iterator atributu rootu


        for (Iterator i = root.elementIterator(); i.hasNext();) {
            Element nextElement = (Element) i.next();

            String s = nextElement.getName();

            /*
             * Mista
             */
            if ("place".equals(s)) {
                Place placeObj = new Place();

                for (Iterator ii = nextElement.elementIterator("token"); ii.hasNext();) {

                    Element tokenEl = (Element) ii.next();
                    Token newtok = new Token();

                    for (Iterator iii = tokenEl.attributeIterator(); iii.hasNext();) {
                        Attribute attribute = (Attribute) iii.next();

                        String ss = attribute.getName();
                        if ("value".equals(ss)) {
                            newtok.setValue(Integer.parseInt(attribute.getValue()));
                        } else if ("posId".equals(ss)) {
                            newtok.setPosId(Integer.parseInt(attribute.getValue()));
                        }
                    } // iterace atributu
                    placeObj.addToken(newtok);
                } //iterace tokenu v place 

                for (Iterator j = nextElement.attributeIterator(); j.hasNext();) {
                    Attribute attribute = (Attribute) j.next();

                    /**
                     * Jmeno atributu pro porovnani
                     */
                    String ss = attribute.getName();
                    {
                        if ("id".equals(ss)) {
                            placeObj.setId(Integer.parseInt(attribute.getText()));
                        } else if ("x".equals(ss)) {
                            placeObj.setPosX(Integer.parseInt(attribute.getText()));
                        } else if ("y".equals(ss)) {
                            placeObj.setPosY(Integer.parseInt(attribute.getText()));
                        }
                    }
                } //iterace elementu place
                places.add(placeObj);
            } /**
             * Prechody
             */
            else if ("transition".equals(s)) {
                Transition transObj = new Transition();

                for (Iterator j = nextElement.attributeIterator(); j.hasNext();) {
                    Attribute attribute = (Attribute) j.next();

                    /**
                     * Jmeno atributu pro porovnani
                     */
                    String ss = attribute.getName();
                    if ("id".equals(ss)) {
                        transObj.setId(Integer.parseInt(attribute.getValue()));
                    } else if ("x".equals(ss)) {
                        transObj.setPosX(Integer.parseInt(attribute.getValue()));
                    } else if ("y".equals(ss)) {
                        transObj.setPosY(Integer.parseInt(attribute.getValue()));
                    } else if ("condition".equals(ss)) {
                        transObj.setCondition(attribute.getValue());
                    } else if ("change".equals(ss)) {
                        transObj.setChange(attribute.getValue());
                    }
                }
                transitions.add(transObj);
            } /**
             * Hrany
             */
            else if ("edge".equals(s)) {
                Edge edgeObj = new Edge();

                for (Iterator j = nextElement.attributeIterator(); j.hasNext();) {
                    Attribute attribute = (Attribute) j.next();

                    /**
                     * ss je jmeno atribudu pro porovnani
                     */
                    String ss = attribute.getName();
                    if ("source".equals(ss)) {
                        edgeObj.setSource(Integer.parseInt(attribute.getValue()));
                    } else if ("target".equals(ss)) {
                        edgeObj.setTarget(Integer.parseInt(attribute.getValue()));
                    } else if ("variable".equals(ss)) {
                        edgeObj.setVar(attribute.getValue());
                    }
                } // iterace atributu

                edges.add(edgeObj);
            } else {
                System.out.println("Additional information: " + nextElement.getName() + ": " + nextElement.getText());
            }
        }// iterace elementu rootu
    }

    /**
     * Naplni objekt site elementy site z ulozeneho XML souboru
     *
     * @param f soubor s ulozenou siti v XML
     * @throws DocumentException
     */
    public void getNet2(File f) throws DocumentException {

        this.name = f.getName();
        SAXReader xmlReader = new SAXReader();
        Document doc = xmlReader.read(f);
        this.iterateDoc(doc);
    }

    /**
     * Naplni objekt site z retezce
     *
     * @param instr retezec obsahujici sit jako XML
     * @throws DocumentException
     */
    public void getNet2(String instr) throws DocumentException {

        Document doc = null;
        try {
            doc = DocumentHelper.parseText(instr);
        } catch (DocumentException ex) {
            Logger.getLogger(Server.ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.iterateDoc(doc);
    }

    /**
     * Pomocna metoda naplni elementy DOM stromu
     * @param netElement korenovy element net DOM stromu site
     * @return korenovy element i s podelementy
     */
    private Element fillDoc(Element netElement) {

        for (Iterator x = this.places.list.listIterator(); x.hasNext();) {
            Place myplace = (Place) x.next();
            Element placeElement = netElement.addElement("place");
            placeElement.addAttribute("x", Integer.toString(myplace.getPosX()));
            placeElement.addAttribute("y", Integer.toString(myplace.getPosY()));
            placeElement.addAttribute("id", Integer.toString(myplace.getId()));

            for (Iterator xx = myplace.getTokens().listIterator(); xx.hasNext();) {
                Token tok = (Token) xx.next();

                Element tokElement = placeElement.addElement("token");
                tokElement.addAttribute("value", Integer.toString(tok.getValue()));
                tokElement.addAttribute("posId", Integer.toString(tok.getPosId()));
            }
        }

        for (Iterator x = this.transitions.list.listIterator(); x.hasNext();) {
            Transition mytrans = (Transition) x.next();
            Element transElement = netElement.addElement("transition");
            transElement.addAttribute("x", Integer.toString(mytrans.getPosX()));
            transElement.addAttribute("y", Integer.toString(mytrans.getPosY()));
            transElement.addAttribute("id", Integer.toString(mytrans.getId()));
            transElement.addAttribute("condition", mytrans.getCondition());
            transElement.addAttribute("change", mytrans.getChange());
        }

        for (Iterator x = this.edges.list.listIterator(); x.hasNext();) {
            Edge myedge = (Edge) x.next();

            Element edgeElement = netElement.addElement("edge");
            edgeElement.addAttribute("source", Integer.toString(myedge.getSource()));
            edgeElement.addAttribute("target", Integer.toString(myedge.getTarget()));
            edgeElement.addAttribute("variable", myedge.getVariable());
        }
        return netElement;
    }

    /**
     * Exportuje sit do souboru
     *
     * @param f sobor pro ulozeni site
     */
    public void saveToXml(File f) {
        Document document = DocumentHelper.createDocument();
        Element netElement = document.addElement("net");
        netElement.addComment("sit pro PetriNet");
        netElement = netElement.addAttribute("name", f.getName());

        netElement = fillDoc(netElement);

        try {
            XMLWriter output = new XMLWriter(
                    new FileWriter(f));
            output.write(document);
            output.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Vraci sit jako DOM strom
     *
     * @return sit jako DOM strom
     */
    public Element saveToXml() {

        Document document = DocumentHelper.createDocument();

        Element netElement = document.addElement("net");
        netElement.addComment("sit pro PetriNet");
        netElement = netElement.addAttribute("name", this.getName());
        netElement = fillDoc(netElement);
        return netElement;

    }
}

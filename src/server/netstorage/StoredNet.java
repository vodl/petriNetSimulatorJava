/*
 * Soubor pro tridu site v ulozisti
 * @author Vojtech Dlapal
 */
package server.netstorage;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.Net;
import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.Element;

/**
 *
 * @author Vojtech Dlapal <xdlapa01 at stud.fit.vutbr.cz>
 */
public class StoredNet extends Net {

    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    /**
     * Ziska aktualni datom a cas
     * @return retezec aktualniho 
     */
    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    /**
     * Ulozi sit do uloziste
     *
     * @param root korenovy DOM element site
     * @param username jmeno uzivatele
     */
    public void store(Element root, String username) {

        // ziskat jmeno
        for (Iterator i = root.attributeIterator(); i.hasNext();) {
            Attribute attribute = (Attribute) i.next();
            if ("name".equals(attribute.getName())) {
                this.setName(attribute.getValue());
            }
        }


        // Vytvorit adresarovou strukturu a ulozit sit
        File f = new File("./storage/" + this.getName());
        if (f.exists()) {
            // neni to 1. verze
            int fnum = 1;
            do {
                f = new File("./storage/" + this.getName() + "/" + Integer.toString(fnum));
                fnum++;
            } while (f.exists());



            if (!f.mkdirs()) {
                //nepodarilo se vytvorit adresare
            } else {
                try {

                    FileOutputStream xfos = new FileOutputStream("./storage/" + this.getName() + "/" + Integer.toString(fnum - 1) + "/" + this.getName());
                    DataOutputStream dos = new DataOutputStream(xfos);
                    root = root.addAttribute("version", Integer.toString(fnum - 1));
                    root = root.addAttribute("author", username);
                    String mystr = root.asXML();
                    dos.writeBytes(mystr);
                } catch (IOException ex) {
                    Logger.getLogger(StoredNet.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } else {
            // je to prvni verze
            f = new File("./storage/" + this.getName() + "/1");

            if (!f.mkdirs()) {
                //nepodarilo se otevrit
            } else {
                try {
                    root = root.addAttribute("version", Integer.toString(1));
                    root = root.addAttribute("author", username);
                    String mystr = root.asXML();
                    FileOutputStream xfos = new FileOutputStream("./storage/" + this.getName() + "/1/" + this.getName());
                    DataOutputStream dos = new DataOutputStream(xfos);
                    dos.writeBytes(mystr);
                } catch (IOException ex) {
                    Logger.getLogger(StoredNet.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    /**
     *  Ukladani informaci o spustenych simulacich
     */
    public void storeSim(String solution, Element root, String username) {

        // ziskat jmeno
        for (Iterator i = root.attributeIterator(); i.hasNext();) {
            Attribute attribute = (Attribute) i.next();
            if ("name".equals(attribute.getName())) {
                this.setName(attribute.getValue());
            }
        }

        StoredNet solNet = new StoredNet();
        try {
            solNet.getNet2(solution);
        } catch (DocumentException ex) {
            Logger.getLogger(StoredNet.class.getName()).log(Level.SEVERE, null, ex);
        }
        Element solNetEl = solNet.saveToXml();


        // Vytvorit adresarovou strukturu a ulozit sit
        File f = new File("./storage/simulations");
        if (f.exists()) {
            // neni to 1. verze
            int fnum = 1;
            do {
                f = new File("./storage/simulations/" + Integer.toString(fnum));
                fnum++;
            } while (f.exists());



            if (!f.mkdirs()) {
                //nepodarilo se vytvorit adresare
            } else {
                try {

                    FileOutputStream xfos = new FileOutputStream("./storage/simulations/" + Integer.toString(fnum - 1) + "/simulations");
                    DataOutputStream dos = new DataOutputStream(xfos);
                    
                    solNetEl = solNetEl.addAttribute("author", username);
                    Element el = solNetEl.addElement("user");
                    el.addText(username);
                    el = solNetEl.addElement("date");
                    el.addText(now());
                    
                    String mystr = solNetEl.asXML();
                    dos.writeBytes(mystr);
                } catch (IOException ex) {
                    Logger.getLogger(StoredNet.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } else {
            // je to prvni verze
            f = new File("./storage/simulations/1");

            if (!f.mkdirs()) {
                //nepodarilo se otevrit
            } else {
                try {
                    FileOutputStream xfos = new FileOutputStream("./storage/simulations/1/simulations");
                    DataOutputStream dos = new DataOutputStream(xfos);

                    
                    solNetEl = solNetEl.addAttribute("author", username);
                    Element el = solNetEl.addElement("user");
                    el.addText(username);
                    el = solNetEl.addElement("date");
                    el.addText(now());
                    
                    String mystr = solNetEl.asXML();
                    dos.writeBytes(mystr);
                    
                } catch (IOException ex) {
                    Logger.getLogger(StoredNet.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }
}

/**
 * Trida pro komunikaci se serverem
 *
 * @author: Vojtech Dlapal
 */
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Trida pro komunikaci se serverem
 * @author Vojtech Dlapal <xdlapa01 at stud.fit.vutbr.cz>
 */
public class Connection {

    Socket s;
    PrintStream out;
    BufferedReader in;
    String username;
    String password;

    /**
     * Port, na kterém server poběží.
     */
    public static final int PORT = 9998;

    /**
     * Ustanovi spojeni se serverem
     */
    public Connection() {
        try {
            username = "default";
            password = "default";

            s = new Socket("localhost", PORT);
            //System.out.println("Client: new socket port " + s.getLocalPort());
            out = new PrintStream(s.getOutputStream()); //vytvořit PrintStream
            in = new BufferedReader(new InputStreamReader(s.getInputStream())); //vytvořit BufferedReader

        } catch (UnknownHostException ex) {
            System.err.println("client connection unknown host exception");
        } catch (java.io.IOException ex) {
            System.err.println("client connection IO exception...");
            close();
        }
    }
    
    /**
     * Ustanovi spojeni se serverem
     * @param host jmeno serveru
     */
    public Connection(String host) {
        try {
            username = "default";
            password = "default";

            s = new Socket("host", PORT);
            //System.out.println("Client: new socket port " + s.getLocalPort());
            out = new PrintStream(s.getOutputStream()); //vytvořit PrintStream
            in = new BufferedReader(new InputStreamReader(s.getInputStream())); //vytvořit BufferedReader

        } catch (UnknownHostException ex) {
            System.err.println("client connection unknown host exception");
        } catch (java.io.IOException ex) {
            System.err.println("client connection IO exception...");
            close();
        }
    }

    /**
     * Pripravi zpravu pro server ve formatu XML
     *
     * @param task identifikator pozadavku
     * @param netel korenovy element site, muze byt null
     * @param netnamestr jmeno site, muze byt null
     * @return naformatovana zprava
     */
    private String makemsg(String task, Element netel, String netnamestr) {
        Document document = DocumentHelper.createDocument();

        Element root = document.addElement("msg");
        //netElement.addComment("sit pro PetriNet");
        Element header = root.addElement("header");
        Element el = header.addElement("username");
        el = el.addText(this.username);
        el = header.addElement("password");
        el = el.addText(this.password);
        el = header.addElement("task");
        el = el.addText(task);

        //Element netel = root.addElement("net");
        if (netel != null) {
            try {
                root.add(netel);
            } catch (org.dom4j.IllegalAddException ex) {}
        }

        if (netnamestr != null) {
            Element netnamel = root.addElement("netname");
            netnamel.addText(netnamestr);
        }

        return document.asXML();
    }
    
    private String readMsg(){
                String inmsg = "";
        try {
            do {
                inmsg += in.readLine();
            } while (!inmsg.endsWith("</msg>"));
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inmsg;
    }
    

    /**
     * Pozada server o autentizaci uzivatele
     *
     * @param name Jmeno uzivatele
     * @param pass Heslo uzivatele
     * @return priznak uspesnosti autentizace
     */
    public boolean login(String name, String pass) {
        this.username = name;
        this.password = pass;

        out.println(this.makemsg("authentificate", null, null));
        out.flush();

        String inmsg = readMsg();


        if (inmsg.trim().startsWith("<msg>OK</msg>")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Pozada server o autentizaci uzivatele
     *
     * @param name Jmeno uzivatele
     * @param pass Heslo uzivatele
     * @return priznak uspesnosti autentizace
     */
    public boolean createUser(String name, String pass) {
        this.username = name;
        this.password = pass;


        out.println(this.makemsg("createUser", null, null));
        out.flush();

        String inmsg = readMsg();

        if (inmsg.trim().startsWith("<msg>OK</msg>")) {
            return true;
        } else {
            return false;
        }
    }
    
    public String simulateNet(Element netel){
        
        out.println(this.makemsg("simulateNet", netel, null));
        out.flush();
        
        String inmsg = readMsg();
        return inmsg.substring(5, inmsg.indexOf("</msg>"));
    }
    
    public String simulateNetStep(Element netel){
        
        out.println(this.makemsg("simulateNetStep", netel, null));
        out.flush();
        
        String inmsg = readMsg();
        return inmsg.substring(5, inmsg.indexOf("</msg>"));
    }

    
    /**
     * Ulozeni site do uloziste
     *
     * @param netel korenovy uzel DOM dokumentu se siti
     * @return priznak uspesnosti
     */
    public boolean storeNet(Element netel) {

        out.println(this.makemsg("storeNet", netel, null));
        out.flush();

        String inmsg = readMsg();
        if (inmsg.startsWith("<msg>OK</msg>")) {
            return true;
        } else {
            return false;
        }
    }

    public String showVersions(String netname) {
        out.println(this.makemsg("showVersions", null, netname));
        out.flush();


        String inmsg = "";
        String outlist = "";
        try {

            do {
                inmsg += in.readLine();
                inmsg += "\n";
            } while (!inmsg.endsWith("</msg>\n"));

        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return inmsg.substring(5, inmsg.indexOf("</msg>"));

    }

    /**
     * Pozadavek na server pro vraceni nazvu vsech ulozenych siti
     *
     * @return retezec nazvu ulozenych siti
     */
    public String showStoredNets() {

        out.println(this.makemsg("showStoredNets", null, null));
        out.flush();

        String inmsg = "";
        String outlist = "";
        try {
            do {
                inmsg += in.readLine();
                inmsg += "\n";
            } while (!inmsg.endsWith("</msg>\n"));

        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        return inmsg.substring(5, inmsg.indexOf("</msg>"));
    }

    
    
    /**
     * Ziska ze serveru string se siti nazvu netname
     *
     * @param netname nazev site hledane v ulozisti
     * @return string s pozadovanou siti ve formatu XML nebo chybova zprava
     */
    public String getStoredNet(String netname) {

        out.println(this.makemsg("getStoredNet", null, netname));
        out.flush();

        String inmsg = readMsg();
        return inmsg.substring(5, inmsg.indexOf("</msg>"));
    }
    
    
    
    
    /**
     * Ziska ze serveru string se siti nazvu netname
     *
     * @param netname nazev site hledane v ulozisti
     * @return string s pozadovanou siti ve formatu XML nebo chybova zprava
     */
    public String getStoredNetVersion(String netname) {

        out.println(this.makemsg("getStoredNetVersion", null, netname));
        out.flush();

        String inmsg = readMsg();
        return inmsg.substring(5, inmsg.indexOf("</msg>"));
    }

    /**
     * Odlogovani - ukonci komunikaci
     *
     * @return - priznak chyby
     */
    public boolean logout() {
        out.println(this.makemsg("logout", null, null));
        out.flush();

        String inmsg = "";
        try {

            do {
                inmsg += in.readLine();
                if (inmsg.equals("<msg>ENDSHAKE</msg>")) {
                    close();
                    return true;
                }
            } while (!(inmsg.endsWith("</msg>") || inmsg == null));
            close();


        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void close() {
        try {
            this.out.flush();
            this.out.close();
            this.s.close();
        } catch (IOException ex) {
            System.err.println("client close connection");
        }
    }
}

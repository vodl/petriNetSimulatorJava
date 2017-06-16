/**
 * Soubor s hlavni tridou serveru
 *
 * @author Vojtech Dlapal
 */
package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import server.netstorage.StoredNet;

/**
 *
 * @author Vojtech Dlapal <xdlapa01 at stud.fit.vutbr.cz>
 */
public class Server {

    /**
     * Port, na kterém server poběží.
     */
    public static final int PORT = 9998;
    /**
     * Soket serveru.
     */
    private ServerSocket ss;
    /**
     * Seznam připojených klientů.
     */
    private List<ClientThread> clients;

    /**
     * Vstupni serveru
     * @param argv 
     */
    public static void main(String[] argv) {
        new Server().start();
    }

    public synchronized List<ClientThread> getClients() {
        return clients;
    }

    public void start() {
        clients = new java.util.ArrayList<ClientThread>();
        File serf = new File("./users.dat");
        if (serf.exists()) {
            User usr = new User();
            try {
                usr.deserialize();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            ss = new ServerSocket(PORT);

            while (true) {
                Socket s = ss.accept();
                ClientThread newclient = new ClientThread(s); //vytvořit vlákno
                clients.add(newclient); //přidat klienta do seznamu
                newclient.start(); //spustit vlákno
            }
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
        } finally {
            if (ss != null) {
                //odpojit všechny klienty
                for (ClientThread clt : getClients()) {
                    clt.close();
                }
                clients.clear();

                try {
                    ss.close();
                } catch (java.io.IOException e) {
                }
            }
        }
    }

    /**
     *
     * @author Vojtech Dlapal <xdlapa01 at stud.fit.vutbr.cz>
     */
    public class ClientThread extends Thread {

        Socket s;
        String username;
        String password;
        String task;
        User user;
        String lastChosedNet;
        /**
         * Výstupní proud.
         */
        PrintStream out;
        /**
         * Vstupní proud.
         */
        BufferedReader in;

        public ClientThread(Socket s) {
            super();
            this.s = s;
            user = new User();

            try {
                out = new PrintStream(s.getOutputStream()); //vytvořit PrintStream
                in = new BufferedReader(new InputStreamReader(s.getInputStream())); //vytvořit BufferedReader
            } catch (IOException e) {
                e.printStackTrace(System.err);
                close();
            }
        }

        /**
         * Odpojí klienta.
         */
        public void close() {
            getClients().remove(this); //vymazat ze seznamu
            try {
                out.close(); //zavřít výstupní proud
                in.close(); //zavřít vstupní proud
                s.close(); //zavřít soket
            } catch (IOException e) {
            }
        }

        @Override
        public void run() {
            try {
               // System.out.println("Server (new socket) - local port " + s.getLocalPort() + "; remote port " + s.getPort());
                                
                while (true) {
                    String msg = "";
                    //String line;
                    do {
                        msg += in.readLine();
                    } while (!msg.endsWith("</msg>"));


                    String outmsg = this.parseMsg(msg);
                    out.println(outmsg);
                    out.flush();

                    if (outmsg.equals("<msg>ENDSHAKE</msg>")) {
                        break;
                        //close() ve finally bloku
                    }
                }
            } catch (java.io.IOException ex) {
                System.err.println("client probably exit without logout");
            }
            finally {
                close();
            }
        }

        /**
         * Zpracuje prichozi zpravu
         *
         * @param msg
         */
        private String parseMsg(String msg) {
 
            Document doc = null;
            try {
                doc = DocumentHelper.parseText(msg);
            } catch (DocumentException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            Element root = doc.getRootElement();

            for (Iterator i = root.elementIterator("header"); i.hasNext();) {
                Element header = (Element) i.next();

                for (Iterator hi = header.elementIterator(); hi.hasNext();) {
                    Element headChild = (Element) hi.next();

                    if ("username".equals(headChild.getName())) {
                        this.username = headChild.getText();
                    } else if ("password".equals(headChild.getName())) {
                        this.password = headChild.getText();
                    } else if ("task".equals(headChild.getName())) {
                        this.task = headChild.getText();
                    }
                } // </header>


                /**
                 * Ukol je vytvorit noveho uzivatele
                 */
                if (this.task.equals("createUser")) {
                    if (user.createUser(this.username, this.password)) {
                        // OK jo supr poslat spravu ze muze LOGNOUT
                        return "<msg>OK</msg>";
                    } else {
                        return "<msg>ERR</msg>";
                    }
                }
                /**
                 * Ukolem je overit daneho uzivatele
                 */
                if (this.task.equals("authentificate")) {
                    if (user.autentificate(this.username, this.password)) {
                        return "<msg>OK</msg>";
                    } else {
                        return "<msg>ERR</msg>";
                    }
                }
                /**
                 * Overeni zda je uzivatel prihlasen pro provadeni cinnosti ke
                 * kterym je to nutne
                 */
                if (user.isLogged()) {

                    /**
                     * Odhlaseni uzivatele
                     */
                    if ("logout".equals(this.task)) {
                        user = new User();
                        return "<msg>ENDSHAKE</msg>";
                    } /**
                     * Simulovani prichozi site
                     */
                    else if ("simulateNet".equals(this.task)) {
                        for (Iterator ni = root.elementIterator("net"); ni.hasNext();) {
                            Element incNet = (Element) ni.next();
                            Simulator sim = new Simulator(incNet.asXML(), false);
                            String solution = sim.simulate();
                            
                            StoredNet stn = new StoredNet();
                            stn.storeSim(solution, root, username);
                            
                            return "<msg>" + solution + "</msg>";
                        }
                    } /**
                     * Jeden krok simulace site
                     */
                    else if ("simulateNetStep".equals(this.task)) {
                        for (Iterator ni = root.elementIterator("net"); ni.hasNext();) {
                            Element incNet = (Element) ni.next();
                            Simulator sim = new Simulator(incNet.asXML(), true);
                            String solution = sim.simulate();
                            
                            StoredNet stn = new StoredNet();
                            stn.storeSim(solution, root, username);
                            
                            return "<msg>" + solution + "</msg>";
                        }
                    } /**
                     * Ulozeni site do uloziste
                     */
                    else if ("storeNet".equals(this.task)) {
                        for (Iterator ni = root.elementIterator("net"); ni.hasNext();) {
                            Element incNet = (Element) ni.next();
                            StoredNet stNet = new StoredNet();
                            stNet.store(incNet, user.getName());
                        }
                        return "<msg>OK</msg>";
                    } /**
                     * Zobrazeni dostupnych verzi pro vybranou sit
                     */
                    else if ("showVersions".equals(this.task)) {
                        Element netnamel = null;
                        for (Iterator iii = root.elementIterator("netname"); iii.hasNext();) {
                            netnamel = (Element) iii.next();
                        }
                        String chosedNet = netnamel.getText();
                        this.lastChosedNet = chosedNet;
                        File dir = new File("./storage/" + chosedNet + "/");
                        File listDir[] = dir.listFiles();
                        String dirlist = "";

                        for (int iii = 0; iii < listDir.length; iii++) {
                            if (listDir[iii].isDirectory()) {
                                dirlist += listDir[iii].getName() + "\n";
                            }
                        }
                        return "<msg>" + dirlist + "</msg>";
                    } /**
                     * Zobrazeni vsech siti v ulozisti
                     */
                    else if ("showStoredNets".equals(this.task)) {
                        File dir = new File("./storage/");
                        File listDir[] = dir.listFiles();
                        String dirlist = "";
                        for (int iii = 0; iii < listDir.length; iii++) {
                            if (listDir[iii].isDirectory()) {
                                dirlist += listDir[iii].getName() + "\n";
                            }
                        }
                        return "<msg>" + dirlist + "</msg>";
                    } /**
                     * Preda danou verzi dane site
                     */
                    else if ("getStoredNetVersion".equals(this.task)) {
                        Element netnamel = null;
                        for (Iterator iii = root.elementIterator("netname"); iii.hasNext();) {
                            netnamel = (Element) iii.next();
                        }
                        String chosedNetNum = netnamel.getText();
                        File ff = new File("./storage/" + lastChosedNet + "/"
                                + chosedNetNum + "/" + lastChosedNet);
                        if (!ff.exists()) {
                            return "<msg>ERR</msg>";
                        } else {
                            StringBuilder contents = new StringBuilder();

                            try {
                                BufferedReader input = new BufferedReader
                                        (new FileReader("./storage/" + lastChosedNet
                                        + "/" + chosedNetNum + "/" + lastChosedNet));
                                try {
                                    String line = null;
                                    while ((line = input.readLine()) != null) {
                                        contents.append(line);
                                        contents.append(System.getProperty("line.separator"));
                                    }
                                } finally {
                                    input.close();
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                            return "<msg>" + contents.toString() + "</msg>";
                        }


                    } /**
                     * Proiteruje slozku uloziste a vrati string s XML se siti,
                     * ktere je mozne zkonvertovat na sit pomoci getNet2() -
                     * prekryteho pro prazdny zadny argument
                     */
                    else if ("getStoredNet".equals(this.task)) {
                        Element netnamel = null;
                        for (Iterator iii = root.elementIterator("netname"); iii.hasNext();) {
                            netnamel = (Element) iii.next();
                        }
                        String chosedNet = netnamel.getText();

                        File f = new File("./storage/" + chosedNet);
                        if (!f.exists()) {
                            return "<msg>ERR</msg>";
                        } else {
                            int counter = 1;
                            do {
                                f = new File("./storage/" + chosedNet + "/"
                                        + Integer.toString(counter) + "/" + chosedNet);
                                counter++;
                            } while (f.exists());

                            StringBuilder contents = new StringBuilder();

                            try {
                                BufferedReader input = new BufferedReader
                                        (new FileReader("./storage/" + chosedNet + "/" +
                                        Integer.toString(counter - 2) + "/" + chosedNet));
                                try {
                                    String line = null;
                                    while ((line = input.readLine()) != null) {
                                        contents.append(line);
                                        contents.append(System.getProperty("line.separator"));
                                    }
                                } finally {
                                    input.close();
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            return "<msg>" + contents.toString() + "</msg>";
                        }
                    } else {
                        return "<msg>PROTOCOL_ERROR</msg>";
                    }
                } else {
                    return "<msg>NOT_LOGGED_IN</msg>";
                }
            }
            return "<msg>PROTOCOL_ERROR</msg>";
        }
    }
}
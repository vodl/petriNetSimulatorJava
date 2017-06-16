/**
 * Soubor s tridou definujici uzivatele
 *
 * @author Vojtech Dlapal
 */
package server;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vojtech Dlapal <xdlapa01 at stud.fit.vutbr.cz>
 */
public class User implements Serializable {

    private static HashMap logins = new HashMap();
    private static final String userfile = "users.dat";
    private boolean loggedIn;
    private String name;

    public User() {
        this.loggedIn = false;
        this.name = "noname";
        this.logins.put("student", "student");
    }

    private static HashMap getLogins() {
        return logins;
    }

    private static void setLogins(HashMap logins) {
        User.logins = logins;
    }

    public boolean isLogged() {
        return loggedIn;
    }
    
    class Logdat implements Serializable{
        HashMap datalogins;

        public Logdat() {
            this.datalogins = User.logins;
        }

        public HashMap getDatalogins() {
            return datalogins;
        }

        public void setDatalogins(HashMap datalogins) {
            this.datalogins = datalogins;
        }
        
    }

    // synchronized public static boolean createUser(String name, String pass) {
    synchronized public boolean createUser(String name, String pass) {
        if (logins.containsKey(name)) {
            return false;
        } else {
            logins.put(name, pass);
            try {
                this.serialize();
            } catch (IOException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean autentificate(String name, String pass) {
        if (logins.containsKey(name)) {
            String rightPass = (String) logins.get(name);
            if (rightPass.equals(pass)) {
                this.name = name;
                this.loggedIn = true;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void serialize() throws IOException {
        FileOutputStream fos = new FileOutputStream(userfile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        Logdat log = new Logdat();
        oos.writeObject(log);
        fos.close();
    }

    public void deserialize() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(userfile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Logdat log = (Logdat) ois.readObject();
        fis.close();
        this.logins = log.getDatalogins();
    }
}

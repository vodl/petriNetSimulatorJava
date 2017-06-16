/**
 * Trida pro vykresleni okna programu
 * @author Jan Kebisek
 */

package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import net.Net;
import net.Ser;
import org.dom4j.DocumentException;
import server.Simulator;

public class NetDisplay extends javax.swing.JFrame {

    /**
     * po prihlaseni se vytvori nove spojeni se serverem
     */
    Connection conn = null;
    
    /**
     * Nastaveni aplikace
     */
    Config config;
    
    /**
     * Priznak pripojeni k serveru
     */
    boolean connected = false;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    boolean flag = false;
    protected JFrame frame;
    public List tabs;
    public int x = 50;
    public int y = 50;
    File f = null;
    Drawer dr; // trida pro prekreslovani plochy
    Ser ser; //objekt XStream pro serializaci nastaveni
    
    
    /**
     * konstruktor triedy NetDisplay
     */
    public NetDisplay() {
        this.ser = new Ser();
        
        /**
         * deserializace nastaveni aplikace
         */
        File conffile = new File("./config.xml");
        if(conffile.exists()){            
            this.config = (Config) ser.xDeserialize(conffile);         
        }
        else{
            this.config = new Config();
        }
        
        initComponents();
    }

    public Connection getConn() {
        return this.conn;
    }

    /**
     * Inicializacia komponentov
     */
    private void initComponents() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();  // File
        jMenu2 = new javax.swing.JMenu();  // Connect
        jMenu7 = new javax.swing.JMenu();  // Style
        jMenu8 = new javax.swing.JMenu();  // Help
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        
        /**** FILE *****/
        jMenu1.setText("File");
        jMenu1.setToolTipText("file");
        
        
        /**** NEW FILE *****/
        jMenuItem1.setText("new file");
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem1MousePressed(evt);
            }
        });
        
        
        /**** OPEN LOCAL *****/
        jMenuItem2.setText("open local");
        jMenuItem2.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem2MousePressed(evt);
            }
        });
        
        
        /**** OPEN FROM SERVER *****/
        jMenuItem3.setText("open from server");
        jMenuItem3.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem3MousePressed(evt);
            }
        });
        
        
        /**** SAVE LOCAL *****/
        jMenuItem4.setText("save local");
        jMenuItem4.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem4MousePressed(evt);
            }
        });
        
        
        /**** SAVE TO SERVER *****/
        jMenuItem5.setText("save to server");
        jMenuItem5.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem5MousePressed(evt);
            }
        });
        
        
        jMenu1.add(jMenuItem1);
        jMenu1.add(jMenuItem2);
        jMenu1.add(jMenuItem3);
        jMenu1.add(jMenuItem4);
        jMenu1.add(jMenuItem5);
        jMenuBar1.add(jMenu1);
        
        
        /*****  SERVER  ******/
        jMenu2.setText("Server");
        jMenu2.setToolTipText("server");
        
        
        /*****  LOG ON  ******/
        jMenuItem9.setText("log on");
        jMenu2.add(jMenuItem9);
        jMenuItem9.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenu2MousePressed(evt);
            }
        });

        
        /*****  REMOTE LOG ON  ******/
        jMenuItem11.setText("remote log on");
        jMenu2.add(jMenuItem11);    
        jMenuItem11.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem11MousePressed(evt);
            }
        });
        
        
        /*****  NEW USER  ******/
        jMenuItem10.setText("new user");
        jMenu2.add(jMenuItem10);    
        jMenuItem10.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem10MousePressed(evt);
            }
        });
        
        
        /******** MENU BAR 1  ********/
        jMenuBar1.add(jMenu2);
        
        /******** SET STYLE  ********/
        jMenu7.setText("Set style");
        
        /******** SET STYLE 1 ********/
        jMenuItem6.setText("style 1");
        jMenuItem6.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem6MousePressed(evt);
            }
        });
        
        /******** SET STYLE 2 ********/
        jMenuItem7.setText("style 2");
        jMenuItem7.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem7MousePressed(evt);
            }
        });
        
        /******** SET STYLE 3 ********/
        jMenuItem8.setText("style 3");
        jMenuItem8.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem8MousePressed(evt);
            }
        });
        
        jMenu7.add(jMenuItem6);
        jMenu7.add(jMenuItem7);
        jMenu7.add(jMenuItem8);
        jMenu7.setToolTipText("change style");
        jMenuBar1.add(jMenu7);
        
        /******** HELP  ********/
        jMenu8.setText("Help");
        jMenu8.setToolTipText("show help");
        jMenu8.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenu8MousePressed(evt);
            }
        });
        jMenuBar1.add(jMenu8);
        
        /*************  ADD PLACE  *****************/
        try{
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client/circle.png")));
        }catch (NullPointerException ex){}
        jButton1.setText("Add place");
        jButton1.setSize(25, 60);
        jButton1.setToolTipText("add 1 place");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton1MousePressed(evt);
            }
        });
        jMenuBar1.add(jButton1);
        
        
        /*************  ADD TRANSITION  *****************/
        try{
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client/rectangle.png")));
        }catch (NullPointerException ex){}
        jButton2.setText("Add transition");
        jButton2.setSize(25, 60);
        jButton2.setToolTipText("add 1 transition");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton2MousePressed(evt);
            }
        });
        jMenuBar1.add(jButton2);

        
        /*************  ADD EDGE  *****************/
        try{
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client/arrow.png")));
        }catch (NullPointerException ex){}
        jButton6.setText("Add edge");
        jButton6.setSize(25, 60);
        jButton6.setToolTipText("add new edge");
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton6MousePressed(evt);
            }
        });
        jMenuBar1.add(jButton6);
        
        
        /*************  DELETE ELEMENT  *****************/
        try{
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client/delete.png")));
        }catch (NullPointerException ex){}
        jButton3.setText("Delete element");
        jButton3.setSize(25, 60);
        jButton3.setToolTipText("delete 1 element");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton3MousePressed(evt);
            }
        });
        jMenuBar1.add(jButton3);
        
        
        /*************  DELETE TOKENS  *****************/
        try{
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client/delete2.png")));
        }catch (NullPointerException ex){}
        jButton8.setText("Delete tokens");
        jButton8.setSize(25, 40);
        jButton8.setToolTipText("delete tokens");
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton8MousePressed(evt);
            }
        });
        jMenuBar1.add(jButton8);
        
        
        /*************  STEP  *****************/
        try{
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client/step.png")));
        }catch (NullPointerException ex){}
        jButton4.setText("Step");
        jButton4.setSize(25, 60);
        jButton4.setToolTipText("make 1 step");
        jMenuBar1.add(jButton4);
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton4MousePressed(evt);
            }
        });
        
        
        /*************  RUN  *****************/
        try{
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client/play.png")));
        }catch (NullPointerException ex){}
        jButton5.setText("Run");
        jButton5.setSize(25, 60);
        jButton5.setToolTipText("run");
        jMenuBar1.add(jButton5);
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton5MousePressed(evt);
            }
        });

        
        /*************  LOG OFF  *****************/
        try{
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client/off.png")));
        }catch (NullPointerException ex){}
        jButton7.setText("Log off");
        jButton7.setSize(25, 40);
        jButton7.setToolTipText("log off from server");
        jMenuBar1.add(jButton7);
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton7MousePressed(evt);
            }
        });
    }// konec initComponents
    
    
    /**
     * HELP
     * @param evt prichadzajuci signal
     */
    private void jMenu8MousePressed(java.awt.event.MouseEvent evt) {
        JOptionPane.showMessageDialog(frame, "Petri's net simulator\nThis is simulator, viewer and editor of petri's net in one.\nAt top you have menu and all butons you need.", "Help", JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    /**
     * LOG ON 
     * @param evt prichadzajuci signal
     */
    private void jMenu2MousePressed(java.awt.event.MouseEvent evt) {
        //JOptionPane.showMessageDialog(this, "Are you sure you want log on?", "Logging", JOptionPane.INFORMATION_MESSAGE);
        JTextField xField = new JTextField(10);
        xField.requestFocusInWindow();
        JTextField yField = new JTextField(10);
        yField.requestFocusInWindow();
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("login:"));
        myPanel.add(xField);
        myPanel.add(Box.createHorizontalStrut(15));
        myPanel.add(new JLabel("password:"));
        myPanel.add(yField);
        int result = JOptionPane.showConfirmDialog(null, myPanel, "Please enter condition and change", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            if (xField.getText().length() == 0 && yField.getText().length() == 0) {
                JOptionPane.showMessageDialog(this, "You must insert your login and password!", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {
                if (conn == null) {
                    conn = new Connection();
                }
                if (conn.login(xField.getText(), yField.getText())) {
                    JOptionPane.showMessageDialog(this, "Loging was succesfull!", "Login status", JOptionPane.INFORMATION_MESSAGE);
                    connected = true;
                } else {
                    JOptionPane.showMessageDialog(this, "Login error!", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    
    /**
     * NEW FILE
     * @param evt  prichadzajuci signal
     */
    private void jMenuItem1MousePressed(java.awt.event.MouseEvent evt) {
        this.f = null;
        frame.dispose();
        showNet();
    }
    
    
    /**
     * STYLE 1
     * @param evt  prichadzajuci signal
     */
    private void jMenuItem6MousePressed(java.awt.event.MouseEvent evt) {
        this.config.setStyle(1);
        this.dr.setStyle(this.config);
        dr.repaint();
        ser.xSerialize(this.config, "config.xml");       
    }
    
    
    /**
     * STYLE 2
     * @param evt  prichadzajuci signal
     */
    private void jMenuItem7MousePressed(java.awt.event.MouseEvent evt) {
        this.config.setStyle(2);
        this.dr.setStyle(this.config);
        dr.repaint();
        ser.xSerialize(this.config, "config.xml");  
    }
    
    
    /**
     * STYLE 3 
     * @param evt  prichadzajuci signal
     */
    private void jMenuItem8MousePressed(java.awt.event.MouseEvent evt) {
        this.config.setStyle(3);
        this.dr.setStyle(this.config);
        dr.repaint();
        ser.xSerialize(this.config, "config.xml");  
    }
    

    /**
     * OPEN FILE LOCAL
     * @param evt  prichadzajuci signal
     */
    private void jMenuItem2MousePressed(java.awt.event.MouseEvent evt) {
        javax.swing.JFileChooser jFileChooser1 = new javax.swing.JFileChooser(".");
        jFileChooser1.setDialogTitle("Choose net");
        //jFileChooser1.showOpenDialog(frame);
        int returnVal = jFileChooser1.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            f = jFileChooser1.getSelectedFile();
            try {
                frame.dispose();
                showNet();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "This file is not possible to open!", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "This file is not possible to open!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    /**
     * OPEN FROM SERVER
     * @param evt  prichadzajuci signal
     */
    private void jMenuItem3MousePressed(java.awt.event.MouseEvent evt) {
        OpenFromServer openWindow = new OpenFromServer(this);
        openWindow.setVisible(true);
    }
    
    
    /**
     * REMOTE LOG ON
     * @param evt prichadzajuci signal
     */
    private void jMenuItem11MousePressed(java.awt.event.MouseEvent evt) {
        RemoteLog remoteLog = new RemoteLog(this);
        // connected i conn se nastavuji pres pristup k Netdisplay nd
        remoteLog.setVisible(true);
        
    }
    
    
    /**
     * SAVE TO SERVER
     * @param evt  prichadzajuci signal
     */
    private void jMenuItem5MousePressed(java.awt.event.MouseEvent evt) {
        String str = JOptionPane.showInputDialog("Enter name of net");
        if (str == null || str.length() == 0) {
            JOptionPane.showMessageDialog(this, "You must insert name!", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            /**
             * Pokud je ustanovene spojeni tak z Draveru se vezme aktualne
             * vykreslovana sit a posle se zpravou
             */
            if (conn != null) {
                this.dr.net.setName(str);

                if (conn.storeNet(this.dr.getNet())) {
                    JOptionPane.showMessageDialog(this, "Your net was saved!", "Saved", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Net was not saved, are you logged in?", "ERROR", JOptionPane.ERROR_MESSAGE);

                }
            } else {
                JOptionPane.showMessageDialog(this, "You are not connected to server", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }


    }
    
    
    /**
     * ADD PLACE
     * @param evt  prichadzajuci signal
     */
    private void jButton1MousePressed(java.awt.event.MouseEvent evt) {
        dr.setPlaceInsertFlag();
    }
    
    
    /**
     * DELETE ELEMENT
     * @param evt  prichadzajuci signal
     */
    private void jButton3MousePressed(java.awt.event.MouseEvent evt) {
        dr.setDeleteFlag();
    }
    
    
    /**
     * ADD TTRANSITION
     * @param evt  prichadzajuci signal
     */
    private void jButton2MousePressed(java.awt.event.MouseEvent evt) {
        dr.setTransitionInsertFlag();
    }
    
    
    /**
     * MAKE STEP
     * @param evt  prichadzajuci signal
     */
    private void jButton4MousePressed(java.awt.event.MouseEvent evt) {
        if(!connected){
            JOptionPane.showMessageDialog(this, "Error! You are not connected.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String theNet = conn.simulateNetStep(this.dr.getNet());

        if (theNet.equals("ERR")) {
            JOptionPane.showMessageDialog(this, "Error!", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            Net net = new Net();
            try {
                net.getNet2(theNet);
            } catch (DocumentException ex) {
                Logger.getLogger(OpenFromServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            frame.dispose();
            this.showNet(net);
        }
    }
    
    
    /**
     * INSERT EDGE
     * @param evt  prichadzajuci signal
     */
    private void jButton6MousePressed(java.awt.event.MouseEvent evt) {
        dr.setEdgeInsertFlag();
    }
    
    
    /**
     * DELETE TOKENS
     * @param evt  prichadzajuci signal
     */
    private void jButton8MousePressed(java.awt.event.MouseEvent evt) {
        dr.setTokensDeleteFlag();
    }

    
    /**
     * Toto je tlacitko na Simulaci
     * @param evt  prichadzajuci signal
     */
    private void jButton5MousePressed(java.awt.event.MouseEvent evt) {
        //Simulator simulator = new Simulator();
        if(!connected){
            JOptionPane.showMessageDialog(this, "Error! You are not connected.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String theNet = conn.simulateNet(this.dr.getNet());

        if (theNet.equals("ERR")) {
            JOptionPane.showMessageDialog(this, "Error!", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            Net net = new Net();
            try {
                net.getNet2(theNet);
            } catch (DocumentException ex) {
                Logger.getLogger(OpenFromServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            frame.dispose();
            this.showNet(net);
        }
    }
    
    
    /**
     * CONNECT
     * @param evt  prichadzajuci signal
     */
    private void jMenuItem10MousePressed(java.awt.event.MouseEvent evt) {
        if (conn == null) {
            this.conn = new Connection();
        }
        NewUser newUser = new NewUser(this.conn, this);
        newUser.setVisible(true);
        newUser.requestFocusInWindow();
    }
    
    
    /**
     * LOG OUT
     * @param evt  prichadzajuci signal
     */
    private void jButton7MousePressed(java.awt.event.MouseEvent evt) {
        connected = false;
        if (conn != null) {
            conn.logout();
            this.conn = null;
        }
        JOptionPane.showMessageDialog(this, "Now you are not logged!", "Log off", JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    /**
     * SAVE NET LOCAL
     * @param evt  prichadzajuci signal
     */
    private void jMenuItem4MousePressed(java.awt.event.MouseEvent evt) {
        javax.swing.JFileChooser jFileChooser2 = new javax.swing.JFileChooser(".");
        jFileChooser2.setDialogTitle("Save net");
        int returnVal = jFileChooser2.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser2.getSelectedFile();
            try {
                dr.net.saveToXml(file);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "It is not possible to save net into this file!", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "It is not possible to save net into this file!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    /**
     * vykreslenie okna
     */
    public void showNet() {
        frame = new JFrame("Petri's net viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.add(tr, BorderLayout.CENTER);
        if (f == null) {
            dr = new Drawer();
        } else {
            dr = new Drawer(f);
        }
        dr.setStyle(this.config);
        frame.add(dr, BorderLayout.CENTER);
        frame.setJMenuBar(jMenuBar1);
        frame.setExtendedState(frame.MAXIMIZED_BOTH);
        frame.pack();
        frame.addMouseListener(dr);
        frame.addMouseMotionListener(dr);
        frame.setVisible(true);
    }
    
    
    /**
     * vykreslenie okna
     * @param net siet na vykreslenie
     */
    public void showNet(Net net) {
        frame = new JFrame("Petri's net viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dr = new Drawer(net);
        dr.setStyle(this.config);
        frame.add(dr, BorderLayout.CENTER);
        frame.setJMenuBar(jMenuBar1);
        frame.setExtendedState(frame.MAXIMIZED_BOTH);
        frame.pack();
        frame.addMouseListener(dr);
        frame.addMouseMotionListener(dr);
        frame.setVisible(true);
    }
    
    
    /**
     * vykreslenie gui
     */
    private void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        showNet();
    }
    
    /**
     * zaciatok gui
     */
    public void startDraw() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

/**
 * Trida pro vykreslovani komponent na platno
 * @author Jan Kebisek
 */

package client;


import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import net.Edge;
import net.Net;
import net.Place;
import net.Transition;
import org.dom4j.DocumentException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import javax.swing.*;
import net.*;
import org.dom4j.Element;

public class Drawer extends JPanel implements MouseListener,MouseMotionListener{
    private final int ARR_SIZE = 8;
    public List edges;
    public List transitions;
    public List places;
    File f;
    boolean flag=true;
    boolean click = false;
    boolean PlaceInsertFlag = false;
    boolean TokensDeletedFlag=false;
    boolean DeletedFlag = false;
    boolean TransitionInsertFlag = false;
    int lastX=-1,lastY=-1;
    long lastTime;
    public Net net;
    
    boolean EdgeInsertFlag = false;
    boolean EdgeInsertFlag2 = false;
    int posX_edgeInsert=0;
    int posY_edgeInsert=0;
    int idx_edgeInsert=0;
    int type_edgeInsert=0;
  
    public List circles;
    public List rectangles;
    public List lines;
    public List linesToDelete;
    
    /**
     * Nastaveni stylu
     * konstruktor nastavi jako vychozi styl styl 1
     * prenastaveni stylu je mozne pomoci style.setStyle(cislo stylu)
     * 
     */
    Config style = new Config();

   /**
    * vykreslenie komponentov
    * @param f 
    */ 
   public Drawer(File f){
       this.f = f;
       this.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
       this.setBackground(style.getBackgroundColor());
       circles = new ArrayList();
       rectangles = new ArrayList();
       lines = new ArrayList();
       linesToDelete = new ArrayList();
       
        net = new Net();
        try {
            net.getNet2(f);
        } catch (DocumentException ex) {
            System.out.println(ex.getMessage());
        }
        places = net.getPlaces();
        transitions = net.getTransitions();
        edges = net.getEdges();
    }
    
      
      /**
       * vykreslenie komponentov
       */
      public Drawer(){
       this.f = null;
       this.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
       this.setBackground(style.getBackgroundColor());
       circles = new ArrayList();
       rectangles = new ArrayList();
       lines = new ArrayList();
       linesToDelete = new ArrayList();
       
        net = new Net();

        places = net.getPlaces();
        transitions = net.getTransitions();
        edges = net.getEdges();

    }
      
      /**
       * vykreslenie komponentov
       * @param net 
       */
      public Drawer(Net net){
       this.f = null;
       this.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
       this.setBackground(style.getBackgroundColor());
       circles = new ArrayList();
       rectangles = new ArrayList();
       lines = new ArrayList();
       linesToDelete = new ArrayList();
       
        this.net = net;

        places = net.getPlaces();
        transitions = net.getTransitions();
        edges = net.getEdges();
    }
   
   /**
    * ulozenie siete
    * @return 
    */
   public Element getNet(){
       return this.net.saveToXml();       
   }
   
   /**
    * nadstavenie vkladania miesta
    */
   public void setPlaceInsertFlag(){
       PlaceInsertFlag = true;
   }
   
   /**
    * nadstavenie vymazavania elementu
    */
   public void setDeleteFlag(){
       DeletedFlag = true;
   }
   
   /**
    * nadstavenie vymazavania tokenov
    */
   public void setTokensDeleteFlag(){
       TokensDeletedFlag = true;
   }
   
   /**
    * nadstavenie vkladania prechodu
    */
   public void setTransitionInsertFlag(){
       TransitionInsertFlag = true;
   }
   
   /**
    * nadstavenie vkladania hrany
    */
   public void setEdgeInsertFlag(){
       EdgeInsertFlag = true;
   }
   
   /**
    * vykreslenie sipky
    * @param g1 grafika
    * @param x1 x-ova suradnica 1. bodu
    * @param y1 y-ova suradnica1. bodu
    * @param x2 x-ova suradnica 2. bodu
    * @param y2 y-ova suradnica2. bodu
    */
   void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy)*2/3;
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);
        
        g.setPaint(style.getLineColor());
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
        new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }
   
   
    /**
     * vykreslenie komponentov
     * @param g 
     */
    @Override
    public void paintComponent (Graphics g) {
        int xSource=0;
        int ySource=0;
        int xTarget=0;
        int yTarget=0;
        
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(style.getLineWidth());
        g2.setPaint(style.getLineColor());
        g2.setFont(style.getFont());
        for(Iterator i = edges.listIterator(); i.hasNext();)
        {
            Edge edge = (Edge) i.next();
                     
            for(Iterator x = places.listIterator(); x.hasNext();)
            {
                Place place = (Place) x.next();
                if(place.getId() == edge.getSource())
                {
                    xSource=place.getPosX();
                    ySource=place.getPosY();
                    break;
                }
            }
            
            for(Iterator x = transitions.listIterator(); x.hasNext();)
            {
                Transition transition = (Transition) x.next();
                if(transition.getId() == edge.getSource())
                {
                    xSource=transition.getPosX();
                    ySource=transition.getPosY();
                    break;
                }
            }
            
            for(Iterator x = transitions.listIterator(); x.hasNext();)
            {
                Transition transition = (Transition) x.next();
                if(transition.getId() == edge.getTarget())
                {
                    xTarget=transition.getPosX();
                    yTarget=transition.getPosY();
                    break;
                }
            }
            
            for(Iterator x = places.listIterator(); x.hasNext();)
            {
                Place place = (Place) x.next();
                if(place.getId() == edge.getTarget())
                {
                    xTarget=place.getPosX();
                    yTarget=place.getPosY();
                    break;
                }
            }
            
            // vykreslenie spojnice
            Line2D.Double line;
            if((edge.getSource() % 2) == 0){  // ak je source Place 
                g2.draw(line = new Line2D.Double(xSource+50,ySource+50,xTarget+85,yTarget+30));
                drawArrow(g2,xSource+50,ySource+50,xTarget+85,yTarget+30);   
            }
            
            else{   //ak je source Transition
                g2.draw(line = new Line2D.Double(xSource+85,ySource+30,xTarget+50,yTarget+50));
                drawArrow(g2,xSource+85,ySource+30,xTarget+50,yTarget+50); 
            }
            if(flag){
                lines.add(line);
            }
            //colorstyle1
            if(edge.getVariable() != null){
                g2.setPaint(Color.red);
                g2.drawString(edge.getVariable(),(xSource+xTarget+135)/2+10,(ySource+yTarget+80)/2+10);
                g2.setPaint(style.getLineColor());
            }
        }
        
        if(EdgeInsertFlag2){
            if((type_edgeInsert%2) == 0){
                Place place;
                place = (Place) places.get(idx_edgeInsert);
                g2.draw(new Line2D.Double(place.getPosX()+50,place.getPosY()+50,posX_edgeInsert,posY_edgeInsert));
            }else{
                Transition transition;
                transition = (Transition) transitions.get(idx_edgeInsert);
                g2.draw(new Line2D.Double(transition.getPosX()+85,transition.getPosY()+30,posX_edgeInsert,posY_edgeInsert));
            }
        }
        
        for(Iterator i = places.listIterator(); i.hasNext();)
        {
            Place place = (Place) i.next();
            Ellipse2D.Double circleObj;
            g2.setPaint(style.getFillColor());
            g2.fillOval(place.getPosX(),place.getPosY(),100,100);
            g2.setPaint(style.getLineColor());
            g2.draw(circleObj = new Ellipse2D.Double(place.getPosX(),place.getPosY(),100,100));
            List tokens = place.getTokens();
            String str="";
            //String 
            String tmpStr;
            for(Iterator r = tokens.listIterator(); r.hasNext();){
                Token token = (Token) r.next();
                tmpStr= Integer.toString(token.getValue());
                str += tmpStr;
                str += ", ";
            }
            if(str.length() != 0){
                g2.drawString(str,place.getPosX()+50-((str.length()/2-1)*7),place.getPosY()+55);
            }
            g2.drawString(place.getName(),place.getPosX(),place.getPosY());
            if(flag){
                circles.add(circleObj);
            }
        }
        
        for(Iterator i = transitions.listIterator(); i.hasNext();){
            Transition transition = (Transition) i.next();
            Rectangle2D.Double rectangleObj;
            
            g2.setPaint(style.getFillColor());
            g2.fillRect(transition.getPosX(),transition.getPosY(), 170, 60);
            g2.setPaint(style.getLineColor());
            g2.draw(rectangleObj = new Rectangle2D.Double(transition.getPosX(),transition.getPosY(),170,60));
            if(flag){
                rectangles.add(rectangleObj);
            }
            g2.draw(new Line2D.Double(transition.getPosX()+10,transition.getPosY()+30,transition.getPosX()+160,transition.getPosY()+30));
            g2.drawString(transition.getCondition(),transition.getPosX()+85-((transition.getCondition().length()/2)*7),transition.getPosY() +18);
            g2.drawString(transition.getChange(),transition.getPosX()+85-((transition.getChange().length()/2)*7),transition.getPosY() +48);
        }
        flag=false;
    }

    /**
     * opustenie okna
     * @param evt 
     */
    @Override
    public void mouseExited(MouseEvent evt) {
        click = false;
    }
    
    /**
     * vstup do okna
     * @param ev 
     */
    @Override
    public void mouseEntered(MouseEvent ev) {}
    
    /**
     * pohyb mysi
     * @param ev 
     */
    @Override
    public void mouseReleased(MouseEvent ev) {}
    
    /**
     * kliknutie mysi
     * @param ev 
     */
    @Override
    public void mouseClicked(MouseEvent ev) {}
    
    /**
     * presun mysi
     * @param evt 
     */
    @Override
    public void mouseMoved   (MouseEvent evt) {
        if(EdgeInsertFlag2){
            posX_edgeInsert = evt.getX()-5;
            posY_edgeInsert = evt.getY()-51;
            repaint();
        }
    }
    
    /**
     * stlacenie mysi
     * @param evt 
     */
    @Override
    public void mousePressed(MouseEvent evt) {        
        if ((lastX==evt.getX()) && (lastY==evt.getY()) && ((evt.getWhen()-lastTime) < 300.0)) {
            int idx = 0;
            Token token;
            Ellipse2D.Double circleObj;
            for(Iterator x = circles.listIterator(); x.hasNext();){
                circleObj = (Ellipse2D.Double) x.next();
                Place place =(Place) places.get(idx);
                if(circleObj.contains(evt.getX()-5, evt.getY()-51)) {
                    String str = JOptionPane.showInputDialog("Enter new token");
                    if(str == null || str.length()==0){
                        JOptionPane.showMessageDialog(this, "You must insert value!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                       token = new Token(); 
                       token.setValue(Integer.parseInt(str));
                       place.addToken(token);
                       repaint();
                    }
                    return;
                }
                else{
                    idx++;
                }
            }
            
            Line2D.Double line;
            idx=0;
            for(Iterator x = lines.listIterator(); x.hasNext();){
                line = (Line2D.Double) x.next();
                Edge edge =(Edge) edges.get(idx);
                Ellipse2D.Double circle = new Ellipse2D.Double((line.getX1()+line.getX2())/2-50,(line.getY1()+line.getY2())/2-50,100,100);
                if(circle.contains(evt.getX()-5, evt.getY()-51)) {
                    String str = JOptionPane.showInputDialog("Enter name of token");
                    if(str == null || str.length()==0){
                        JOptionPane.showMessageDialog(this, "You must insert value!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                       edge.setVar(str);
                       repaint();
                    }
                    return;
                }
                else{
                    idx++;
                }
            }

            Rectangle2D.Double rectangleObj;
            idx=0;
            for(Iterator x = rectangles.listIterator(); x.hasNext();){
                rectangleObj = (Rectangle2D.Double) x.next();
                Transition transition = (Transition) transitions.get(idx);               
                if(rectangleObj.contains(evt.getX()-5,evt.getY()-51)){                   
                    JTextField xField = new JTextField(10);
                    JTextField yField = new JTextField(10);

                    JPanel myPanel = new JPanel();
                    myPanel.add(new JLabel("Condition:"));
                    myPanel.add(xField);
                    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                    myPanel.add(new JLabel("Change:"));
                    myPanel.add(yField);
                    int result = JOptionPane.showConfirmDialog(null, myPanel,"Please enter condition and change", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        if(xField.getText().length() != 0){
                            transition.setCondition(xField.getText());
                        }
                        if(yField.getText().length() != 0){
                            transition.setChange(yField.getText());
                        }else{
                            if(xField.getText().length() == 0){
                               JOptionPane.showMessageDialog(this, "You must insert some value!", "ERROR", JOptionPane.ERROR_MESSAGE); 
                            }
                        }
                        repaint();
                    }
                    
                    repaint();
                    break;  
                }
                else {
                   idx++;
                }
            }   
        }
        else {
            lastTime = evt.getWhen();
            lastX=evt.getX();
            lastY=evt.getY();
        }
        
        if(PlaceInsertFlag){
            Place place = new Place();
            place.setPosX(evt.getX()-55);
            place.setPosY(evt.getY()-101);
            place.setId(places.size()*2);
            places.add(places.size(), place);
            
            Ellipse2D.Double circle = new Ellipse2D.Double(evt.getX()-55,evt.getY()-101,100,100);
            circles.add(circles.size(), circle);
            repaint();
            PlaceInsertFlag=false;
            return;
        }
        
        if(TransitionInsertFlag){
            Transition transition = new Transition();
            transition.setPosX(evt.getX()-90);
            transition.setPosY(evt.getY()-81);
            transition.setId(transitions.size()*2+1);
            transition.setCondition("empty");
            transition.setChange("empty");
            transitions.add(transitions.size(), transition);
            
            Rectangle2D.Double rectangle = new Rectangle2D.Double(evt.getX()-90,evt.getY()-81,170,60);
            rectangles.add(rectangles.size(), rectangle);
            repaint();
            TransitionInsertFlag=false;
            return;
        }

        if(TokensDeletedFlag){
            Ellipse2D.Double circleObj;
            int idx = 0;
            for(Iterator x = circles.listIterator(); x.hasNext();){
                circleObj = (Ellipse2D.Double) x.next();
                Place place =(Place) places.get(idx);
                if(circleObj.contains(evt.getX()-5, evt.getY()-51)) {
                    place.getTokens().clear();
                    repaint();
                    TokensDeletedFlag = false;
                    return;
                }
                else{
                    idx++;
                }
            }
            TokensDeletedFlag = false;
        }
        
        if(DeletedFlag){
            if(!linesToDelete.isEmpty()){
                linesToDelete.clear();
            }
            int idx = 0;
            Ellipse2D.Double circleObj;
            for(Iterator x = circles.listIterator(); x.hasNext();){
                circleObj = (Ellipse2D.Double) x.next();
                Place place =(Place) places.get(idx);
                if(circleObj.contains(evt.getX()-5, evt.getY()-51)) {
                    int idx2=0;
                    for(Iterator r = lines.listIterator(); r.hasNext();){
                        Line2D.Double line = (Line2D.Double) r.next();
                        Edge edge =(Edge) edges.get(idx2);
                        if(edge.getSource()== place.getId() || edge.getTarget()==place.getId()){
                            //lines.remove(idx2);
                            linesToDelete.add(line);
                            edges.remove(edge);
                            idx2--;
                        }
                        idx2++;
                    }
                    lines.removeAll(linesToDelete);
                    DeletedFlag=false;
                    //circles.remove(idx);
                    circles.remove(circleObj);
                    places.remove(place);
                    //places.remove(idx);
                    repaint();
                    return;
                }
                else{
                    idx++;
                }
            }
            
            Rectangle2D.Double rectangleObj2;
            if(!linesToDelete.isEmpty()){
                linesToDelete.clear();
            }
            idx=0;
            for(Iterator x = rectangles.listIterator(); x.hasNext();){
                rectangleObj2 = (Rectangle2D.Double) x.next();
                Transition transition = (Transition) transitions.get(idx);               
                if(rectangleObj2.contains(evt.getX()-5,evt.getY()-51)){                   
                    int idx2=0;
                    for(Iterator r2 = lines.listIterator(); r2.hasNext();){
                        Line2D.Double line2 = (Line2D.Double) r2.next();
                        Edge edge =(Edge) edges.get(idx2);
                        if(edge.getSource()== transition.getId() || edge.getTarget()==transition.getId()){
                            //lines.remove(line);
                            linesToDelete.add(line2);
                            edges.remove(edge);
                            idx2--;
                        }
                        idx2++;
                    }
                    DeletedFlag=false;
                    lines.removeAll(linesToDelete);
                    //rectangles.remove(idx);
                    rectangles.remove(rectangleObj2);
                    transitions.remove(transition);
                    //transitions.remove(idx);
                    repaint();
                    return;  
                }
                else {
                   idx++;
                }
            } 
        }
        
        
        int idx = 0;
        boolean flg=false;   
        for(Iterator x = circles.listIterator(); x.hasNext();){
            Ellipse2D.Double circleObj = (Ellipse2D.Double) x.next();
            if(circleObj.contains(evt.getX()-5,evt.getY()-51)){
                click = true;
                
                if(EdgeInsertFlag2){
                    if((type_edgeInsert%2) == 0){
                        EdgeInsertFlag=false;
                        EdgeInsertFlag2=false;
                        JOptionPane.showMessageDialog(this, "This operation is not possible!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        repaint();
                    }
                    else{
                        Edge edge = new Edge();
                        edge.setSource((idx_edgeInsert*2)+1);
                        edge.setTarget(idx*2);
                        edges.add(edge);
                        Place place= (Place) places.get(idx);
                        Transition transition = (Transition) transitions.get(idx_edgeInsert);
                        lines.add(new Line2D.Double(transition.getPosX()+85,transition.getPosY()+30,place.getPosX()+50,place.getPosY()+50));
                        EdgeInsertFlag=false;
                        EdgeInsertFlag2=false;
                        repaint();
                    }
                }
                
                if(EdgeInsertFlag && (!EdgeInsertFlag2)){
                    idx_edgeInsert = idx;
                    type_edgeInsert = 2;
                    EdgeInsertFlag2 = true;
                }
                
                flg = true;
                break;
            }
            idx++;
        }
        
        idx=0;
        if(!flg){
            for(Iterator x = rectangles.listIterator(); x.hasNext();){
                Rectangle2D.Double rectangleObj = (Rectangle2D.Double) x.next();
                if(rectangleObj.contains(evt.getX()-5,evt.getY()-51)){
                    click = true;
                    
                    if(EdgeInsertFlag2){
                        if((type_edgeInsert%2) == 1){
                            EdgeInsertFlag=false;
                            EdgeInsertFlag2=false;
                            JOptionPane.showMessageDialog(this, "This operation is not possible!", "ERROR", JOptionPane.ERROR_MESSAGE);
                            repaint();
                        }
                        else{
                            Edge edge = new Edge();
                            edge.setSource(idx_edgeInsert*2);
                            edge.setTarget((idx*2)+1);
                            edges.add(edge);
                            Place place= (Place) places.get(idx_edgeInsert);
                            Transition transition = (Transition) transitions.get(idx);
                            lines.add(new Line2D.Double(place.getPosX()+50,place.getPosY()+50,transition.getPosX()+85,transition.getPosY()+30));
                            EdgeInsertFlag=false;
                            EdgeInsertFlag2=false;
                            repaint();
                        }
                    }
                
                    if(EdgeInsertFlag && (!EdgeInsertFlag2)){
                        idx_edgeInsert = idx;
                        type_edgeInsert = 1;
                        EdgeInsertFlag2 = true;
                    }
                    
                    
                    flg = true;
                    break;
                }
                idx++;
            }
        }
        
        if(!flg){
            click=false;
            if(EdgeInsertFlag){
                EdgeInsertFlag = false;
                EdgeInsertFlag2 = false;
                JOptionPane.showMessageDialog(this, "This operation is not possible1", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * tahanie mysou
     * @param evt 
     */
    @Override
    public void mouseDragged(java.awt.event.MouseEvent evt) { 
        int idx = 0;
        boolean flg = false;
        Ellipse2D.Double circleObj;
        Ellipse2D.Double circleTmp;
        Rectangle2D.Double rectangleTmp;
        Rectangle2D.Double rectangleObj;
        if(click){
            for(Iterator x = circles.listIterator(); x.hasNext();){
                circleObj = (Ellipse2D.Double) x.next();
                Place place =(Place) places.get(idx);
                if(circleObj.contains(evt.getX()-5, evt.getY()-51)) {
                    circleTmp = new Ellipse2D.Double(evt.getX()-55,evt.getY()-101,100,100);
                    circles.set(idx, circleTmp);
                    place.setPosX(evt.getX()-55);
                    place.setPosY(evt.getY()-101);
                    repaint();
                    flg=true;
                    break;
                }
                else{
                    idx++;
                }
            }
            
            if(!flg){
                idx=0;
                for(Iterator x = rectangles.listIterator(); x.hasNext();){
                    rectangleObj = (Rectangle2D.Double) x.next();
                    Transition transition = (Transition) transitions.get(idx);
                    rectangleTmp = new Rectangle2D.Double(evt.getX()-90,evt.getY()-81,170,60);
                
                    if(rectangleObj.contains(evt.getX()-5,evt.getY()-51)){
                        rectangles.set(idx, rectangleTmp);
                        transition.setPosX(evt.getX()-90);
                        transition.setPosY(evt.getY()-81);
                        repaint();
                        break;  
                    }
                    else {
                        idx++;
                    }
                }
            }
        } // end of: if(click)
    }
    
    /**
     * ziskanie stylu
     * @return 
     */
    public Config getStyle() {
        return style;
    }
    
    /**
     * nadstavenie stylu
     * @param style 
     */
    public void setStyle(Config style) {
        this.style = style;
    }
}   


/**
 * Trida pro praci s nastavenim klienta
 *
 * @author: Jan Kebisek
 */
package client;

import java.awt.BasicStroke;
import java.awt.Color;

/**
 * Trida s nastavenim
 * @author Vojtech Dlapal
 */
public class Config {

    Color fillColor;
    Color lineColor;
    Color backgroundColor;
    BasicStroke lineWidth;
    java.awt.Font font;

    public Config() {
        this.setStyle(1);      
    }

    public void setStyle(int opt) {
        switch (opt) {
            default:
            case 1:
                this.fillColor = new java.awt.Color(166, 166, 166);
                this.lineColor = new java.awt.Color(255, 69, 0);
                this.backgroundColor = new java.awt.Color(64, 64, 64);
                this.lineWidth = new BasicStroke(2F);
                this.font = new java.awt.Font("Calibri", 0, 14);
                break;

            case 2:
                this.fillColor = new java.awt.Color(194, 192, 192);
                this.lineColor = new java.awt.Color(21, 2, 196);
                this.lineWidth = new BasicStroke(3F);
                this.backgroundColor = new java.awt.Color(236, 236, 236);
                this.font = new java.awt.Font("Comic Sans MS", 0, 15);
                break;

            case 3:
                this.fillColor = Color.white;
                this.lineColor = Color.black;
                this.backgroundColor = new java.awt.Color(0, 194, 252);
                this.lineWidth = new BasicStroke(4F);
                this.font = new java.awt.Font("Arial", 0, 13);
                break;
        }
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }
    
    public void setFont(java.awt.Font font) {
        this.font = font;
    }
    
    public java.awt.Font getFont() {
        return this.font;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public BasicStroke getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(BasicStroke lineWidth) {
        this.lineWidth = lineWidth;
    }
}

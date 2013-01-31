/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fopengl;

/**
 *
 * @author ADMIN
 */
public abstract class OBJECT_2D
{
    //Variablendeklaration, die überall gleich ist
    protected double colorgr, colorrd, colorbl;
    //die abstrakten Methoden, die sich in jeder Klasse ändern
    public abstract void zeichnen();
    public abstract void bewegen(int x, int y);
    //jetzt allgemeine Methoden, die wir immer brauchen
    public void setzeFarbe(String farbe)
    {
        if(farbe.equals("blau"))
        {
            colorgr=0.0;
            colorrd=0.0;
            colorbl=1.0;
        }
        else if(farbe.equals("rot"))
        {
            colorgr=0.0;
            colorrd=0.0;
            colorbl=1.0;
        }
        else if(farbe.equals("gruen"))
        {
            colorgr=1.0;
            colorrd=0.0;
            colorbl=0.0;
        }
        else if(farbe.equals("weiss"))
        {
            colorgr=1.0;
            colorrd=1.0;
            colorbl=1.0;
        }
        else if(farbe.equals("schwarz"))
        {
            colorgr=0.0;
            colorrd=0.0;
            colorbl=0.0;
        }
        else if(farbe.equals("braun"))
        {
            colorgr=0.27;
            colorrd=0.55;
            colorbl=0.07;
        }
        else if(farbe.equals("grau"))
        {
            colorgr=0.53;
            colorrd=0.466;
            colorbl=0.60;
        }
        else if(farbe.equals("orange"))
        {
            colorgr=0.65;
            colorrd=1.0;
            colorbl=0.0;
        }
        else if(farbe.equals("gelb"))
        {
            colorgr=1.0;
            colorrd=1.0;
            colorbl=0.0;
        }
        else if(farbe.equals("violett"))
        {
            colorgr=0.51;
            colorrd=0.93;
            colorbl=0.93;
        }
        else
        {
            colorgr=0.0;
            colorrd=0.0;
            colorbl=0.0;
        }
    }
}

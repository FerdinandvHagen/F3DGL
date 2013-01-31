/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fopengl;

import static org.lwjgl.opengl.GL11.*;
/**
 *
 * @author ADMIN
 */
public class Kreis extends OBJECT_2D
{
    private int x,y,r;
    private float mx, my;
    public Kreis(int x, int y, int r)
    {
        this.x=x;
        this.y=y;
        this.r=r;
        setzeFarbe("schwarz");
        Leinwand l=Leinwand.gibLeinwand();
        mx=1f;
        my=1f;
    }
    public void zeichnen()
    {
        //Hier wird das Objekt gezeichnet
        glColor3d(colorrd, colorgr, colorbl);
        float xs,ys;
        glBegin(GL_POLYGON); //Begin Polygon coordinates
        for (double theta=0 ; theta<(2*3.1416) ; theta+=(2*3.1416)/360)
        {
            xs = (float)x+(float)(Math.cos(theta)*(float)r);
            ys = (float)y+(float)(Math.sin(theta)*(float)r);
            glVertex2f(xs,ys);
        }
        glEnd();
    }
    public void bewegen(int x, int y)
    {
        this.x=x;
        this.y=y;
    }
}


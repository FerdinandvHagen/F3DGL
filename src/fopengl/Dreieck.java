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
public class Dreieck extends OBJECT_2D
{
    private int x,y,w,h;
    public Dreieck(int x, int y, int w, int h)
    {
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        setzeFarbe("schwarz");
    }
    public void zeichnen()
    {
        //Hier wird das Objekt gezeichnet
        glColor3d(colorrd, colorgr, colorbl);
        glBegin(GL_TRIANGLES);
        glVertex2i(x,y);
        glVertex2i(x+(w/2),y-h);
        glVertex2i(x+w,y);
        glEnd();
    }
    public void bewegen(int x, int y)
    {
        this.x=x;
        this.y=y;
    }
}

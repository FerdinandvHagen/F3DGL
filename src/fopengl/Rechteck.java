/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fopengl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.opengl.Texture;
/**
 *
 * @author ADMIN
 */
public class Rechteck extends OBJECT_2D
{
    private int x,y,w,h;
    private Texture textur;
    public Rechteck(int x, int y, int w, int h)
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
        if(textur==null)
        {
            glColor3d(colorrd, colorgr, colorbl);
            glBegin(GL_QUADS);
            glVertex2i(x,y);
            glVertex2i(x+w,y);
            glVertex2i(x+w,y+h);
            glVertex2i(x,y+h);
            glEnd();
        }
        else
        {
            textur.bind();
            glBegin(GL_TRIANGLES);
            glTexCoord2f(1, 0);
            glVertex2i(x+w, y);
            glTexCoord2f(0, 0);
            glVertex2i(x, y);
            glTexCoord2f(0, 1);
            glVertex2i(x, y+h);
            glTexCoord2f(0, 1);
            glVertex2i(x, y+h);
            glTexCoord2f(1, 1);
            glVertex2i(x+w, y+h);
            glTexCoord2f(1, 0);
            glVertex2i(x+w, y);
            glEnd();
        }
    }
    public void bewegen(int x, int y)
    {
        this.x=x;
        this.y=y;
    }
    public void ladeTextur(String textur)
    {
        try
        {
            // Load the wood texture from "res/images/wood.png"
            this.textur = TextureLoader.getTexture("PNG", new FileInputStream(new File(textur)));
        }
        catch (IOException e)
        {
            System.err.println("Could not find "+textur+" or image is not in PNG format.");
            this.textur=null;
        }
    }
}

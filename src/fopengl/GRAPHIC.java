/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fopengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.opengl.Texture;
import java.io.*;
import org.lwjgl.openal.AL;
/**
 *
 * @author ADMIN
 */
public abstract class GRAPHIC
{
    //Variables
    public boolean showLoading=false;
    public int width;
    public int height;
    String title;
    public abstract void Init();
    public abstract void ProcessInput();
    public abstract void ProcessCamera();
    public abstract void Render();
    public abstract void Wait();
    public abstract void CleanUp();
    //kann überschrieben werden, um zum Beispiel die Parameter anzupassen
    public void setDisplay()
    {
        this.width=640;
        this.height=480;
        this.title="default";
    }
    
    
    public GRAPHIC()
    {
        SetUp();
        while(!Display.isCloseRequested())
        {
            Process();
        }
        Close();
    }
    
    
    //Create the necessary things
    private void SetUp()
    {
        //Zunächst das Display verändert. Kann im Bedarfsfall überschrieben werden
        setDisplay();
        //dann wird das Fenster erzeugt
        INIT.enableGRAPHIC(this.width,this.height, this.title);
        //jetzt kann endlich alles initialisiert werden
        Init();
    }
    //Process everything
    private void Process()
    {
        glClear(GL_DEPTH_BUFFER_BIT|GL_COLOR_BUFFER_BIT);
        
        ProcessInput();
        ProcessCamera();
        Render();
        
        Display.update();
        
        Wait();
    }
    //CleanUp and close
    private void Close()
    {
        CleanUp();
        INIT.disableGRAPHIC();
    }
    
    
    
    //Zeichenklassen
    public static class DisplayList
    {
        public static void paint(int list)
        {
            glCallList(list);
        }
        public static void delete(int list)
        {
            glDeleteLists(list,1);
        }
        public static int start()
        {
            int displayList=glGenLists(1);
            glNewList(displayList, GL_COMPILE);
            return displayList;
        }
        public static void end()
        {
            glEndList();
        }
    }
    public static class Triangle
    {
        public static void start()
        {
            glBegin(GL_TRIANGLES);
        }
        public static void end()
        {
            glEnd();
        }
        public static void point(float x, float y, float z)
        {
            glVertex3f(x,y,z);
        }
    }
    public static class Quad
    {
        public static void paint(int x, int y,  int w, int h)
        {
            glBegin(GL_QUADS);
            glVertex2i(x+w,y);
            glVertex2i(x,y);
            glVertex2i(x,y+h);
            glVertex2i(x+w,y+h);
            glEnd();
        }
    }
    public static class Color
    {
        public static void setColor(String farbe)
        {
            double colorgr,colorrd,colorbl;
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
            glColor3d(colorrd,colorrd,colorbl);
        }
        public static void setColor(double red, double green, double blue)
        {
            glColor3d(red, green, blue);
        }
    }
    
    public static class INIT
    {
        public static void enableAUDIO()
        {
            try
            {
                AL.create();
            }
            catch (LWJGLException e)
            {
                e.printStackTrace();
                AL.destroy();
                System.exit(1);
            }
        }
        public static void disableAUDIO()
        {
            AL.destroy();
        }
        public static void enableGRAPHIC()
        {
            enableGRAPHIC(640,480,"default");
        }
        public static void enableGRAPHIC(int width, int height, String title)
        {
             try {
                Display.setDisplayMode(new DisplayMode(width, height));
                Display.setFullscreen(true);
                Display.setVSyncEnabled(true);
                Display.setTitle(title);
                Display.create();
            } catch (LWJGLException e) {
                System.err.println("The display wasn't initialized correctly.");
                Display.destroy();
                System.exit(1);
            }
        }
        public static void disableGRAPHIC()
        {
            Display.destroy();
            System.exit(0);
        }
    }
}

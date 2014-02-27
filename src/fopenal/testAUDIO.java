/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fopenal;

import fopengl.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
/**
 *
 * @author ADMIN
 */
public class testAUDIO
{
    public static void main(String[] args)
    {
        new testAUDIO();
    }
    public testAUDIO()
    {
        INIT.enableAUDIO();
        AUDIO test=new AUDIO("thump.wav");
        int i=20;
        while(i--!=0)
        {
            test.playAUDIO();
            try
            {
                Thread.sleep(120000);
            }
            catch (InterruptedException e)
            {
                
            }
        }
        test.stopAUDIO();
        test.cleanUp();
        INIT.disableAUDIO();
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fopengl;

import java.awt.Toolkit;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GLContext;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.event.*;

import util.*;
/**
 *
 * @author ADMIN    System.getProperty("sun.arch.data.model")
 */
public class Leinwand
{
    static final File libraryFile;
    
    static
    {
        libraryFile=null;
        loadDll("lwjgl",libraryFile);
    }
    private static void loadDll(String name, File file)
    {
        InputStream inputStream = Display.class.getClassLoader().getResourceAsStream(name+System.getProperty("sun.arch.data.model")+".dll");
        file = new File(name+".dll");
        file.deleteOnExit();
        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[8192];
            int bytesRead;
            while((bytesRead = inputStream.read(buffer)) > 0)
            {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            fileOutputStream.close();
            inputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private int hoehe, breite;
    private List<OBJECT_2D> objects;
    private JFrame fenster;
    //Singleton ist das erste Objekt der Klasse Leinwand
    //Hier können andere Objekte eine Instanz von Leinwand abrufen
    //und bekommen ein Handle
    private static Leinwand leinwandSingleton;
    private Leinwand()
    {
        create(640,480,"Leinwand");
    }
    public static Leinwand gibLeinwand()
    {
        if(leinwandSingleton==null)
        {
            leinwandSingleton=new Leinwand();
            Runnable  rs=(Runnable)leinwandSingleton;
            Thread s=new Thread(rs, "LEINWAND");
            s.start();
            System.out.println("s");
            return leinwandSingleton;
        }
        else
        {
            return leinwandSingleton;
        }
    }
    //Konstruktoren von Leinwand
    public int getbreite()
    {
        return this.breite;
    }
    public int gethoehe()
    {
        return this.hoehe;
    }
    //neues Fenster erzeugen
    private void create(int breite, int hoehe, String title)
    {
        objects=new ArrayList<OBJECT_2D>();
        this.hoehe=hoehe;
        this.breite=breite;
        try
        {
            // Setze die Höhe udn Breite des Displays, den Titel und erstellt es
            Display.setDisplayMode(new DisplayMode(breite, hoehe));
            Display.setTitle(title);
            Display.create();
        }
        catch (LWJGLException e)
        {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }
        glMatrixMode(GL_PROJECTION);
        glOrtho(0, breite, hoehe, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glClearColor(1f,1f,1f,1f);
        System.out.println("Display has been successfully created");
    }
    public void close()
    {
        Display.destroy();
    }
    public void addObject(OBJECT_2D add)
    {
        objects.remove(add);    //entferne die Figur, falls vorhanden
        objects.add(add);       //neu anfügen
        redraw();
    }
    public void removeObject(OBJECT_2D add)
    {
        objects.remove(add);
        redraw();
    }
    public void redraw()
    {
        glClear(GL_COLOR_BUFFER_BIT);
        for(OBJECT_2D obj : objects)
            obj.zeichnen();
        Display.update();
    }
}

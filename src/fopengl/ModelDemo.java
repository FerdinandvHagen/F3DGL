package fopengl;

//import util.BufferTools;
import fopenal.AUDIO;
import fopenal.testAUDIO;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector2f;
import util.*;
import utility.*;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;


public class ModelDemo
{

    private static Camera camera;
    private static int bunnyDisplayList;
    private static int houseDisplayList;
    private static float[] lightposition =
    {
        -2.19f, 1.36f, 11.45f, 1f
    };
    private static float[] fogcolor =
    {
        0.7f, 0.7f, 0.7f, 1f
    };
    private static Texture audi;
    public static final String MODEL_LOCATION = "Bugatti2";
    public static final String HOUSE_LOCATION = "NEUESTES_DANCOUVER";
    private static Model t;
    private static AUDIO test;
    private static Animation anim;

    public static void main(String[] args)
    {
        //OBJLoader.loadMTL(new File("C:/Blockversuch.mtl"));
        setUpDisplay();
        Model s = OBJLoader.loadModel(MODEL_LOCATION);
        t = OBJLoader.loadModel(HOUSE_LOCATION);
        anim = new Animation("untitled_",99,"000000");
        //OBJLoader.getObjects(s);
        //OBJLoader.getObjects(t);
        bunnyDisplayList = OBJLoader.createList(s);
        houseDisplayList = OBJLoader.createList(t);
        setUpCamera();
        setUpLighting();
        //Transparenz aktivieren
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_DEPTH_TEST);
        //glEnable(GL_SMOOTH);
        //glEnable(GL_CULL_FACE);
        //glCullFace(GL_FRONT_AND_BACK);
        glEnable(GL_LIGHTING);
        glEnable(GL_COLOR_MATERIAL);
        //glColorMaterial(GL_FRONT, GL_SHININESS);
        //glEnable(GL_TEXTURE_2D);

        //Audio Test
        testAUDIO.INIT.enableAUDIO();
        AUDIO.setListener(AUDIO.POSITION, camera.x(), camera.y(), camera.z());
        test = new AUDIO("thump.wav");
        test.setSource(AUDIO.POSITION, 100f, 100f, 0f);
        test.playAUDIO();

        while (!Display.isCloseRequested())
        {
            render();
            anim.animate();
            checkInput();
            Display.update();
            Display.sync(25);
        }
        cleanUp();
        System.exit(0);
    }

    private static void checkInput()
    {
        camera.processMouse(1, 80, -80);
        camera.processKeyboard(1600, 1, 1, 1);
        //glLight(GL_LIGHT0, GL_POSITION, BufferTools.asFlippedFloatBuffer(lightposition));
        //glLight(GL_LIGHT0, GL_COLOR, BufferTools.asFlippedFloatBuffer(new float[]{1.0f, 0.0f, 0.0f, 1.0f}));
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
        {
            Mouse.setGrabbed(false);
            cleanUp();
            System.exit(0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_G))
        {
            lightposition = new float[]
            {
                camera.x(), camera.y(), camera.z(), 1
            };
            test.setSource(AUDIO.POSITION, camera.x(), camera.y(), camera.z());
        }
    }

    private static void cleanUp()
    {
        glDeleteLists(bunnyDisplayList, 1);
        glDeleteLists(houseDisplayList, 1);
        test.stopAUDIO();
        test.cleanUp();
        testAUDIO.INIT.disableAUDIO();
        Display.destroy();
    }

    private static void render()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        camera.applyTranslations();
        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINES);
        glCallList(bunnyDisplayList);
        glCallList(houseDisplayList);
        //OBJLoader.processPartModel(t, camera.x(), camera.y(), 1000f);
    }

    private static void setUpCamera()
    {
        camera = new EulerCamera.Builder()
                .setAspectRatio((float) Display.getWidth() / Display.getHeight())
                .setRotation(-1.12f, 0.16f, 0f)
                .setPosition(-1.38f, 1.36f, 7.95f)
                .setFieldOfView(60)
                .build();
        camera.applyOptimalStates();
        camera.applyPerspectiveMatrix();
    }

    private static void setUpDisplay()
    {
        try
        {
            Display.setFullscreen(true);
            Display.setVSyncEnabled(true);
            Display.setTitle("Happy Easter!");
            Display.create();
        }
        catch (LWJGLException e)
        {
            System.err.println("The display wasn't initialized correctly. :(");
            Display.destroy();
            System.exit(1);
        }
        Mouse.setGrabbed(true);
    }

    private static void setUpLighting()
    {
        glShadeModel(GL_SMOOTH);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glLightModel(GL_LIGHT_MODEL_AMBIENT, OBJLoader.asFlippedFloatBuffer(new float[]
                {
                    1f, 1f, 1f, 1f
                }));
        glLight(GL_LIGHT0, GL_POSITION, OBJLoader.asFlippedFloatBuffer(new float[]
                {
                    1, 1, 1, 1
                }));
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_COLOR_MATERIAL);
        //glColorMaterial(GL_FRONT, GL_SHININESS);

        glEnable(GL_FOG);
        glFogi(GL_FOG_MODE, GL_LINEAR);
        glFogf(GL_FOG_START, 470f);
        glFogf(GL_FOG_END, 490f);
        glFog(GL_FOG_COLOR, OBJLoader.asFlippedFloatBuffer(fogcolor));

        glClearColor(0.7f, 0.7f, 0.7f, 1f);
    }
}
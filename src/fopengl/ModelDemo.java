/*
 * Copyright (c) 2012, Oskar Veerhoek
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the FreeBSD Project.
 */
package fopengl;

//import util.BufferTools;
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

import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

/**
 * Loads the Stanford bunny .obj file and draws it.
 *
 * @author Oskar Veerhoek
 */
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

    public static void main(String[] args)
    {
        //OBJLoader.loadMTL(new File("C:/Blockversuch.mtl"));
        setUpDisplay();
        Model s = OBJLoader.loadModel(MODEL_LOCATION);
        t = OBJLoader.loadModel(HOUSE_LOCATION);
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
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_LIGHTING);
        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_SHININESS);
        //glEnable(GL_TEXTURE_2D);
        while (!Display.isCloseRequested())
        {
            render();
            checkInput();
            Display.update();
            Display.sync(60);
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
        if (Mouse.isButtonDown(0))
        {
            Mouse.setGrabbed(true);
        }
        else if (Mouse.isButtonDown(1))
        {
            Mouse.setGrabbed(false);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_G))
        {
            lightposition = new float[]
            {
                camera.x(), camera.y(), camera.z(), 1
            };
        }
    }

    private static void cleanUp()
    {
        glDeleteLists(bunnyDisplayList, 1);
        glDeleteLists(houseDisplayList, 1);
        Display.destroy();
    }

    private static void render()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        camera.applyTranslations();
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINES);
        //glCallList(bunnyDisplayList);
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
            Display.setDisplayMode(new DisplayMode(1900, 1080));
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
        glColorMaterial(GL_FRONT, GL_SHININESS);

        glEnable(GL_FOG);
        glFogi(GL_FOG_MODE, GL_LINEAR);
        glFogf(GL_FOG_START, 450f);
        glFogf(GL_FOG_END, 490f);
        glFog(GL_FOG_COLOR, OBJLoader.asFlippedFloatBuffer(fogcolor));
        
        glClearColor(0.7f,0.7f,0.7f,1f);
    }
}
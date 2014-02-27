/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fopengl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import util.*;

/**
 *
 * @author ADMIN
 */
public class Animation
{

    public int frames;
    public Model[] allframes;
    public int currentframe;

    public static void mergeanimation(String filename, int count, String format) throws FileNotFoundException, IOException
    {
        BufferedWriter writeobj = new BufferedWriter(new FileWriter(new File(filename + "anim" + ".obj")));
        writeobj.write("# Merged Animation; Filename: " + filename);
        writeobj.newLine();

        DecimalFormat df = new DecimalFormat(format);

        for (int i = 0; count != i; i++)
        {
            BufferedReader readobj = new BufferedReader(new FileReader(new File(filename + df.format(i) + ".obj")));
            BufferedReader readmtl = new BufferedReader(new FileReader(new File(filename + df.format(i) + ".mtl")));

            writeobj.write("++ frame: " + df.format(i));
            writeobj.newLine();

            String line;
            while ((line = readobj.readLine()) != null)
            {
                if (line.startsWith("v "))
                {
                }
            }

            readobj.close();
            readmtl.close();
        }
    }

    public Animation(String filename, int count, String format)
    {
        DecimalFormat df = new DecimalFormat(format);

        allframes = new Model [count];
        for (int i = 1; count != i; i++)
        {
            allframes[i - 1] = OBJLoader.loadModel(filename + df.format(i));
            frames = i;
        }
        currentframe = 0;
    }
    
    public void animate()
    {
        Model current = allframes[currentframe++];
        if(currentframe==frames)
        {
            currentframe=0;
        }
        OBJLoader.printModel(current);
    }
}

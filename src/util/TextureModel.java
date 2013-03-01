/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author ADMIN
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class TextureModel
{

    public Texture texture;
    public String path;

    public TextureModel(String s)
    {
        path = s;
        try
        {
            texture = TextureLoader.getTexture(s.split("\\.")[1].toUpperCase(), new FileInputStream(new File(s)));
        }
        catch (IOException e)
        {
            System.err.println("Texture " + s + " not found");
        }
    }

    public boolean contains(TextureModel e)
    {
        if (e.path == this.path)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public void print()
    {
        System.out.println(path);
    }
}

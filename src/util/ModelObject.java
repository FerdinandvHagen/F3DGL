/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import org.lwjgl.util.vector.Vector3f;
import java.util.List;
import java.util.ArrayList;
import org.newdawn.slick.opengl.Texture;
/**
 *
 * @author ADMIN
 */
public class ModelObject
{
    //Attention! less an object and more a part with MTL-assignments.
    //MTL - part
    public Vector3f Ka;
    public Vector3f Kd;
    public Vector3f Ks;
    public float d;
    public float Ns;
    public float Ni;
    public String name;
    public Texture texture;
    //name of the corresponding object
    public String objname;
    public boolean change;
    public void addmtlinfo(String name, Texture texture, Vector3f Ka, Vector3f Kd, Vector3f Ks, float d, float Ns, float Ni)
    {
       this.Ka=Ka;
       this.Kd=Kd;
       this.Ks=Ks;
       this.d=d;
       this.Ns=Ns;
       this.name=name;
       this.texture=texture;
       this.Ni=Ni;
    }
    
    //OBJ - part
    public List<Face>faces=new ArrayList<Face>();
}

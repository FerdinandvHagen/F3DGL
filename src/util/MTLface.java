/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector2f;
/**
 *
 * @author ADMIN
 */
public class MTLface
{
    public Vector3f Ka;
    public Vector3f Kd;
    public Vector3f Ks;
    public float d;
    public float Ns;
    public float Ni;
    public String name;
    public String path;
    
    public MTLface(String name, String path, Vector3f Ka, Vector3f Kd, Vector3f Ks, float d, float Ns, float Ni)
    {
        this.Ka=Ka;
        this.Kd=Kd;
        this.Ks=Ks;
        this.d=d;
        this.Ns=Ns;
        this.name=name;
        this.path=path;
        this.Ni=Ni;
    }
}

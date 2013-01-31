/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import org.lwjgl.util.vector.Vector3f;
/**
 *
 * @author ADMIN
 */
public class Face
{
    public Vector3f vertex = new Vector3f(); // three indices, not vertices or normals!
    public Vector3f normal = new Vector3f();
    public Vector3f textvert = new Vector3f();
    
    public Face(Vector3f vertex, Vector3f normal, Vector3f textvert)
    {
        this.vertex = vertex;
        this.normal = normal;
        this.textvert=textvert;
    }
}

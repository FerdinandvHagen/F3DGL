/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ADMIN
 */
public class Model
{
    public List<Vector3f> normals=new ArrayList<Vector3f>();
    public List<Vector3f> vertices=new ArrayList<Vector3f>();
    public List<Vector2f> texvertices=new ArrayList<Vector2f>();
    
    public List<ModelObject> object=new ArrayList<ModelObject>();
}

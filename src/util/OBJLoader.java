/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author ADMIN
 */
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector2f;

import java.io.*;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.Display;

import java.util.List;
import java.util.ArrayList;

//import java.util.Date;

import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.opengl.GL15.*;
//import org.lwjgl.util.vector.Matrix4f;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.opengl.Texture;

public class OBJLoader
{

    public static Model loadModel(String name, String mtllib)
    {
        Model m = null;
        try
        {
            m = OBJLoader.loadOBJ(name, mtllib);
        }
        catch (FileNotFoundException e)
        {
            fatalerrorout(e, name);
        }
        catch (IOException e)
        {
            fatalerrorout(e, name);
        }
        return m;
    }

    public static Model loadModel(String name)
    {
        Model m = null;
        try
        {
            m = OBJLoader.loadOBJ(name, "");
        }
        catch (FileNotFoundException e)
        {
            fatalerrorout(e, name);
        }
        catch (IOException e)
        {
            fatalerrorout(e, name);
        }
        return m;
    }

    public static String[] getObjects(Model m)
    {
        return getObjects(m, true);
    }

    public static String[] getObjects(Model m, boolean display)
    {
        String ret = "";
        for (ModelObject mobj : m.object)
        {
            ret += " " + mobj.objname;
            if (display)
            {
                System.out.println(mobj.objname);
            }
        }
        return ret.split(" ");
    }

    public static int createList(Model m)
    {
        return createList(m, "");
    }

    public static int createList(Model m, String object)
    {
        long millis = System.currentTimeMillis();
        int displayList = glGenLists(1);
        int objcounter = 0;
        int objcounterall = 0;
        glNewList(displayList, GL_COMPILE);
        {
            for (ModelObject mobj : m.object)
            {
                objcounterall++;
                if (object.isEmpty() || object.contains(mobj.objname.subSequence(0, mobj.objname.length())))
                {
                    objcounter++;
                    glDisable(GL_TEXTURE_2D);
                    //Zuerst die ganzen Daten auslesen für die Farbe
                    glMaterial(GL_FRONT, GL_AMBIENT, asFlippedFloatBuffer(new float[]
                            {
                                mobj.Ka.x, mobj.Ka.y, mobj.Ka.z, mobj.d
                            }));
                    glMaterial(GL_FRONT, GL_DIFFUSE, asFlippedFloatBuffer(new float[]
                            {
                                mobj.Kd.x, mobj.Kd.y, mobj.Kd.z, mobj.d
                            }));
                    if (mobj.Ks != null)
                    {
                        glMaterial(GL_FRONT, GL_SPECULAR, asFlippedFloatBuffer(new float[]
                                {
                                    mobj.Ks.x, mobj.Ks.y, mobj.Ks.z, mobj.d
                                }));
                    }
                    glColor4f(mobj.Kd.x, mobj.Kd.y, mobj.Kd.z, mobj.d);
                    glMaterialf(GL_FRONT, GL_SHININESS, mobj.Ns);
                    //und nun wird ganz normal gelesen.
                    if (mobj.texture != null)
                    {
                        glDisable(GL_TEXTURE_2D);
                        glEnable(GL_TEXTURE_2D);
                        glColor4f(1f, 1f, 1f, 1f);
                        mobj.texture.bind();
                    }
                    else
                    {
                        glColor4f(mobj.Kd.x, mobj.Kd.y, mobj.Kd.z, mobj.d);
                    }

                    glBegin(GL_TRIANGLES);
                    for (Face face : mobj.faces)
                    {
                        //1
                        if (mobj.texture != null)
                        {
                            Vector2f t1 = m.texvertices.get((int) face.textvert.x - 1);
                            glTexCoord2f(t1.x, 1f - t1.y);
                        }
                        Vector3f n1 = m.normals.get((int) face.normal.x - 1);
                        glNormal3f(n1.x, n1.y, n1.z);
                        Vector3f v1 = m.vertices.get((int) face.vertex.x - 1);
                        glVertex3f(v1.x, v1.y, v1.z);
                        //2
                        if (mobj.texture != null)
                        {
                            Vector2f t2 = m.texvertices.get((int) face.textvert.y - 1);
                            glTexCoord2f(t2.x, 1f - t2.y);
                        }
                        Vector3f n2 = m.normals.get((int) face.normal.y - 1);
                        glNormal3f(n2.x, n2.y, n2.z);
                        Vector3f v2 = m.vertices.get((int) face.vertex.y - 1);
                        glVertex3f(v2.x, v2.y, v2.z);
                        //3
                        if (mobj.texture != null)
                        {
                            Vector2f t3 = m.texvertices.get((int) face.textvert.z - 1);
                            glTexCoord2f(t3.x, 1f - t3.y);
                        }
                        Vector3f n3 = m.normals.get((int) face.normal.z - 1);
                        glNormal3f(n3.x, n3.y, n3.z);
                        Vector3f v3 = m.vertices.get((int) face.vertex.z - 1);
                        glVertex3f(v3.x, v3.y, v3.z);
                    }
                }
                glEnd();
            }
        }
        glEndList();
        System.out.println("Creating DisplayList took " + (System.currentTimeMillis() - millis) + "ms and included " + objcounter + " of " + objcounterall + " objects");
        return displayList;
    }

    public static void processPartModel(Model m, float x, float y, float far)
    {
        glBegin(GL_TRIANGLES);
        for (ModelObject mobj : m.object)
        {
            if (!mobj.inbounds(x, y, far))
            {
                break;
            }
            glDisable(GL_TEXTURE_2D);
            //Zuerst die ganzen Daten auslesen für die Farbe
            glMaterial(GL_FRONT, GL_AMBIENT, asFlippedFloatBuffer(new float[]
                    {
                        mobj.Ka.x, mobj.Ka.y, mobj.Ka.z, 1f
                    }));
            glMaterial(GL_FRONT, GL_DIFFUSE, asFlippedFloatBuffer(new float[]
                    {
                        mobj.Kd.x, mobj.Kd.y, mobj.Kd.z, 1f
                    }));
            if (mobj.Ks != null)
            {
                glMaterial(GL_FRONT, GL_SPECULAR, asFlippedFloatBuffer(new float[]
                        {
                            mobj.Ks.x, mobj.Ks.y, mobj.Ks.z, 1f
                        }));
            }
            glColor4f(mobj.Kd.x, mobj.Kd.y, mobj.Kd.z, mobj.d);
            glMaterialf(GL_FRONT, GL_SHININESS, mobj.Ns);
            //und nun wird ganz normal gelesen.
            if (mobj.texture != null)
            {
                glDisable(GL_TEXTURE_2D);
                glEnable(GL_TEXTURE_2D);
                mobj.texture.bind();
            }
            glBegin(GL_TRIANGLES);
            for (Face face : mobj.faces)
            {
                Vector3f v1 = m.vertices.get((int) face.vertex.x - 1);
                Vector3f v2 = m.vertices.get((int) face.vertex.y - 1);
                Vector3f v3 = m.vertices.get((int) face.vertex.z - 1);
                //1
                {
                    if (mobj.texture != null)
                    {
                        Vector2f t1 = m.texvertices.get((int) face.textvert.x - 1);
                        glTexCoord2f(t1.x, 1f - t1.y);
                    }
                }
                Vector3f n1 = m.normals.get((int) face.normal.x - 1);
                glNormal3f(n1.x, n1.y, n1.z);
                glVertex3f(v1.x, v1.y, v1.z);
                //2
                if (mobj.texture != null)
                {
                    Vector2f t2 = m.texvertices.get((int) face.textvert.y - 1);
                    glTexCoord2f(t2.x, 1f - t2.y);
                }
                Vector3f n2 = m.normals.get((int) face.normal.y - 1);
                glNormal3f(n2.x, n2.y, n2.z);
                glVertex3f(v2.x, v2.y, v2.z);
                //3
                if (mobj.texture != null)
                {
                    Vector2f t3 = m.texvertices.get((int) face.textvert.z - 1);
                    glTexCoord2f(t3.x, 1f - t3.y);
                }
                Vector3f n3 = m.normals.get((int) face.normal.z - 1);
                glNormal3f(n3.x, n3.y, n3.z);
                glVertex3f(v3.x, v3.y, v3.z);
            }
        }
        glEnd();
    }

    private static Model loadOBJ(String name, String mtllib) throws FileNotFoundException, IOException
    {
        //Wir holen uns die Anfangsuhrzeit, um Ladezeiten berechnen zu können
        long millis = System.currentTimeMillis();
        //Eine Liste alles bisher behandelten Texturen; verhindert das doppelte Laden
        List<TextureModel> texturelist = new ArrayList<>();
        //Es kommt unser erste Nachricht, dass wir mit unserer Arbeit beginnen
        System.out.println("Reading " + name);
        //Es wird die mtl und die obj Datein ersteinmal erzeugt
        BufferedReader readerobj = new BufferedReader(new FileReader(new File(name + ".obj")));
        String mtlpath = name + ".mtl";
        if (!mtllib.isEmpty())
        {
            mtlpath = name + mtllib + ".mtl";
        }
        System.out.println("Corresponding MTL-File: " + mtlpath);
        //danach lesen wir die mtl-Datei ein.
        //MTL mtl=createmtl(readermtl);     Wir brauchen sie eigentlich nicht. Ist nur schön zu lesen
        //unser Model wird erstellt
        Model obj = new Model();
        long countervert = 0;
        int countobject = 0;
        int countmtlgroups = 0;
        //Die Zeilen werden eingelesen
        ModelObject currentobj = new ModelObject();
        currentobj.change = false;
        String line;
        String objname = "unknown";
        while ((line = readerobj.readLine()) != null)
        {
            if (line.startsWith("v "))
            {
                countervert++;
                float x, y, z;
                if (line.split(" ")[1].isEmpty())
                {
                    x = Float.valueOf(line.split(" ")[2]);
                    y = Float.valueOf(line.split(" ")[3]);
                    z = Float.valueOf(line.split(" ")[4]);
                }
                else
                {
                    x = Float.valueOf(line.split(" ")[1]);
                    y = Float.valueOf(line.split(" ")[2]);
                    z = Float.valueOf(line.split(" ")[3]);
                }
                obj.vertices.add(new Vector3f(x, y, z));
            }
            else if (line.startsWith("vn "))
            {

                float x, y, z;
                if (line.split(" ")[1].isEmpty())
                {
                    x = Float.valueOf(line.split(" ")[2]);
                    y = Float.valueOf(line.split(" ")[3]);
                    z = Float.valueOf(line.split(" ")[4]);
                }
                else
                {
                    x = Float.valueOf(line.split(" ")[1]);
                    y = Float.valueOf(line.split(" ")[2]);
                    z = Float.valueOf(line.split(" ")[3]);
                }
                obj.normals.add(new Vector3f(x, y, z));
            }
            else if (line.startsWith("vt "))
            {
                float x, y;
                if (line.split(" ")[1].isEmpty())
                {
                    x = Float.valueOf(line.split(" ")[2]);
                    y = Float.valueOf(line.split(" ")[3]);
                }
                else
                {
                    x = Float.valueOf(line.split(" ")[1]);
                    y = Float.valueOf(line.split(" ")[2]);
                }
                obj.texvertices.add(new Vector2f(x, y));
            }
            else if (line.startsWith("f "))
            {
                if (line.split(" ").length != 4)
                {
                    //f's sind nicht als Dreiecke vorliegend, daher muss noch eine veränderte drüber
                    Vector3f vertexIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[0]),
                            Float.valueOf(line.split(" ")[3].split("/")[0]),
                            Float.valueOf(line.split(" ")[4].split("/")[0]));
                    Vector3f normalIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[2]),
                            Float.valueOf(line.split(" ")[3].split("/")[2]),
                            Float.valueOf(line.split(" ")[4].split("/")[2]));
                    Vector3f textureIndices = null;
                    if (!line.split(" ")[1].split("/")[1].equals(""))
                    {
                        //face contains Texture information
                        textureIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[1]),
                                Float.valueOf(line.split(" ")[3].split("/")[1]),
                                Float.valueOf(line.split(" ")[4].split("/")[1]));
                    }
                    currentobj.faces.add(new Face(vertexIndices, normalIndices, textureIndices));
                }

                //Kein Problem, jetzt die ersten drei, entweder sind die sowieso im Dreieck oder ich habe ja schon welche extrahiert
                Vector3f vertexIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[0]),
                        Float.valueOf(line.split(" ")[2].split("/")[0]),
                        Float.valueOf(line.split(" ")[3].split("/")[0]));
                Vector3f normalIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[2]),
                        Float.valueOf(line.split(" ")[2].split("/")[2]),
                        Float.valueOf(line.split(" ")[3].split("/")[2]));
                Vector3f textureIndices = null;
                if (!line.split(" ")[1].split("/")[1].equals(""))
                {
                    //face contains Texture information
                    textureIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[1]),
                            Float.valueOf(line.split(" ")[2].split("/")[1]),
                            Float.valueOf(line.split(" ")[3].split("/")[1]));
                }

                currentobj.faces.add(new Face(vertexIndices, normalIndices, textureIndices));
                currentobj.change = true;
            }
            else if (line.startsWith("usemtl "))
            {
                countmtlgroups++;
                if (currentobj.change == false)
                {
                    //damit ist es das erste Objekt
                }
                else
                {
                    //wurde schon beschrieben
                    obj.object.add(currentobj);
                    currentobj = new ModelObject();
                }
                currentobj.objname = objname;
                MTLface s = getmtl(new BufferedReader(new FileReader(new File(mtlpath))), line.split(" ")[1]);
                if (s == null)
                {
                    System.err.println("Did not find mtl-obj");
                    System.err.println("Searched in " + name + ".mtl");
                }

                if (s.path == null)
                {
                    //keine Textur
                    currentobj.addmtlinfo(s.name, null, s.Ka, s.Kd, s.Ks, s.d, s.Ns, s.Ni);
                }
                else
                {
                    //Textur vorhanden
                    Texture tex = null;
                    for (TextureModel texmodel : texturelist)
                    {
                        if (texmodel.path.equals(s.path))
                        {
                            tex = texmodel.texture;
                        }
                    }
                    if (tex == null)
                    {
                        texturelist.add(new TextureModel(s.path));
                    }
                    currentobj.addmtlinfo(s.name, tex, s.Ka, s.Kd, s.Ks, s.d, s.Ns, s.Ni);
                }
            }
            else if (line.startsWith("o "))
            {
                countobject++;
                //new object found
                objname = line.split(" ")[1];

            }
            else if (line.startsWith("# "))
            {
                System.out.println(line);
            }
        }
        obj.object.add(currentobj);
        readerobj.close();
        System.out.println(countmtlgroups + " times of mtl use");
        System.out.println(name + " has " + countobject + " OBJ-Objects (For more information: String[] OBJLoader.getObjects(Model m, [boolean display])");
        System.out.println(name + " has " + countervert + " Vertices");
        System.out.println("OBJ succesfully analyzed in " + (System.currentTimeMillis() - millis) + "ms");

        for (TextureModel m : texturelist)
        {
            m.print();
        }

        //Noch die Mittelpunkte berechnen
        OBJLoader.calculatemiddle(obj);

        return obj;
    }

    private static MTLface getmtl(BufferedReader f, String searchname) throws FileNotFoundException, IOException
    {
        MTLface found = null;
        String line;
        int counter;
        counter = 0;
        Vector3f Ka, Kd, Ks;
        float d, Ns, Ni;
        String name, path;
        name = null;
        path = null;
        Ka = null;
        Kd = null;
        Ks = null;
        d = 1f;
        Ns = 100f;
        Ni = 1f;
        while ((line = f.readLine()) != null)
        {
            if (line.startsWith("newmtl "))
            {
                if (counter != 0 && searchname.equals(name))
                {
                    found = new MTLface(name, path, Ka, Kd, Ks, d, Ns, Ni);
                }
                name = line.split(" ")[1];
                path = null;
                Ka = null;
                Kd = null;
                Ks = null;
                d = 1f;
                Ns = 100f;
                Ni = 1f;
                counter++;
            }
            else if (line.startsWith("Ns "))
            {
                //Ns gefunden (Exponent)
                Ns = Float.valueOf(line.split(" ")[1]);
            }
            else if (line.startsWith("d "))
            {
                //d gefunden (Transparenz)
                d = Float.valueOf(line.split(" ")[1]);
            }
            else if (line.startsWith("Ka "))
            {
                float x = Float.valueOf(line.split(" ")[1]);
                float y = Float.valueOf(line.split(" ")[2]);
                float z = Float.valueOf(line.split(" ")[3]);
                Ka = (new Vector3f(x, y, z));
            }
            else if (line.startsWith("Ks "))
            {
                float x = Float.valueOf(line.split(" ")[1]);
                float y = Float.valueOf(line.split(" ")[2]);
                float z = Float.valueOf(line.split(" ")[3]);
                Ks = (new Vector3f(x, y, z));
            }
            else if (line.startsWith("Kd "))
            {
                float x = Float.valueOf(line.split(" ")[1]);
                float y = Float.valueOf(line.split(" ")[2]);
                float z = Float.valueOf(line.split(" ")[3]);
                Kd = (new Vector3f(x, y, z));
            }
            else if (line.startsWith("Ni "))
            {
                Ni = Float.valueOf(line.split(" ")[1]);
            }
            else if (line.startsWith("map_Kd"))
            {
                path = line.split(" ")[1];
                if (line.split(" ").length != 2)
                {
                    path = line.split(" ")[1] + " " + line.split(" ")[2];
                }
            }
        }
        f.close();
        //sonst wird der letzte nicht gefunden
        if (searchname.equals(name))
        {
            found = new MTLface(name, path, Ka, Kd, Ks, d, Ns, Ni);
        }
        return found;
    }

    public static FloatBuffer asFlippedFloatBuffer(float... values)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
        buffer.put(values);
        buffer.flip();
        return buffer;
    }

    private static void fatalerrorout(IOException e, String name)
    {
        e.printStackTrace();
        System.err.println("Failed to create: " + name);
        Display.destroy();
        System.exit(1);
    }

    public static void calculatemiddle(Model m)
    {
        float minx, maxx, miny, maxy, minz, maxz;
        boolean first;
        for (ModelObject t : m.object)
        {
            first = true;
            minx = 0f;
            maxx = 0f;
            miny = 0f;
            maxy = 0f;
            minz = 0f;
            maxz = 0f;

            for (Face face : t.faces)
            {
                //1
                Vector3f v1 = m.vertices.get((int) face.vertex.x - 1);
                //2
                Vector3f v2 = m.vertices.get((int) face.vertex.y - 1);
                //3 alle drei Punkte des Dreiecks behandeln
                Vector3f v3 = m.vertices.get((int) face.vertex.z - 1);

                if (first)  //Wenns der Erste ist, werden die auf einen definierten Startwert gelegt. Nicht 0!! Sonst kommt Schmarrn raus
                {
                    first = false;
                    minx = v1.x;
                    maxx = v1.x;
                    miny = v1.y;
                    maxy = v1.y;
                    minz = v1.z;
                    maxz = v1.z;
                }

                //Berechnung von minimalem aus v1, v2, v3 und min (lokal)
                minx = Math.min(v1.x, Math.min(v2.x, Math.min(v3.x, minx)));
                miny = Math.min(v1.y, Math.min(v2.y, Math.min(v3.y, miny)));
                minz = Math.min(v1.z, Math.min(v2.z, Math.min(v3.z, minz)));

                //Berechnung von maximalem aus v1, v2, v3 und min (lokal)
                maxx = Math.max(v1.x, Math.max(v2.x, Math.max(v3.x, maxx)));
                maxy = Math.max(v1.y, Math.max(v2.y, Math.max(v3.y, maxy)));
                maxz = Math.max(v1.z, Math.max(v2.z, Math.max(v3.z, maxz)));
            }

            //Jetzt in das aktuelle ModelObject laden
            t.middlepointx = (minx + maxx) / 2f;
            t.middlepointy = (miny + maxy) / 2f;
            t.middlepointz = (minz + maxz) / 2f;
            t.expx = (maxx - minx) / 2f;
            t.expy = (maxy - miny) / 2f;
            t.expz = (maxz - minz) / 2f;

            System.out.println(t.middlepointx + " " + t.middlepointy + " " + t.expx + " " +t.expy);
        }
    }
}
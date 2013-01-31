/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.io.InputStream;
import java.io.IOException;
/**
 *
 * @author ADMIN
 */
public class NativeLibLoader
{
        private static final String LIB_PATH_IN_JAR = "/lib/";
        
        public static void loaddll(String libraryName)
        {
                loadFromJar(libraryName);
        }
 
        public static void loadFromJar(String libraryName) {
                long randomPart = new Date().getTime();
                File libFile = new File(System.getProperty("java.io.tmpdir") + "/" + "JST_" + randomPart + LIB_PATH_IN_JAR + libraryName + ".dll");
                InputStream in = NativeLibLoader.class.getResourceAsStream(LIB_PATH_IN_JAR + libraryName + ".dll");
                if(in==null)
                {
                    System.err.println("File not found!!");
                    return;
                }
                try {
                        if (libFile.getParentFile().mkdirs())
                        {
                                FileOutputStream fw = new FileOutputStream(libFile);
                                byte[] b = new byte[2048];
                                int noOfBytes = 0;
                                while ((noOfBytes = in.read(b)) != -1)
                                {
                                        fw.write(b, 0, noOfBytes);
                                }
                                in.close();
                                fw.close();
                        }
                        else
                        {
                            System.out.println("Error");
                        }
 
                } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Error");
                }
                System.load(libFile.toString());
                System.out.println(libraryName+" succesfully loaded");
        }
}

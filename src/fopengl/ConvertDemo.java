/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fopengl;

import util.OBJLoader;
import java.io.*;


import java.util.*;
import java.lang.*;
import java.net.*;

/**
 *
 * @author ADMIN
 */
public class ConvertDemo
{

    public static void main(String[] args)
    {       
        try
        {
            InetAddress ownIP = InetAddress.getLocalHost();
            System.out.println("IP of my system is := " + ownIP.getHostAddress());
            byte [] IP = ownIP.getAddress();
            IP[3] = (byte)255;
            ownIP = InetAddress.getByAddress(IP);
            System.out.println("Broadcast - IP is: " + ownIP.getHostAddress());
        }
        catch (Exception e)
        {
            System.out.println("Exception caught =" + e.getMessage());
        }
    }
}

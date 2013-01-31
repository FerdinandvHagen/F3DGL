package fopenal;

/**
 *
 * @author Ferdinand von Hagen
 */
import org.lwjgl.LWJGLException;
import org.lwjgl.util.WaveData;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.*;

public class AUDIO
{
    private int buffer;
    private int source;
    private WaveData data;
    public AUDIO(String file)
    {
        try
        {
            data = WaveData.create(new BufferedInputStream(new FileInputStream(file)));
        }
        catch (FileNotFoundException e)
        {
            System.err.println(file+" not found");
            System.exit(1);
        }
        buffer = alGenBuffers();
        alBufferData(buffer, data.format, data.data, data.samplerate);
        data.dispose();
        source = alGenSources();
        alSourcei(source, AL_BUFFER, buffer);
    }
    //Beeinflusst den Hörer (Position, Velocity, Orientation)
    public static void setListener(int pname, float x, float y, float z) 
    {
        alListener3f(pname, x,y,z);
    }
    public static void setLIstener(int pname, int x, int y, int z)
    {
        alListener3i(pname, x, y, z);
    }
    public void playAUDIO()
    {
        alSourcePlay(source);
    }
    public void cleanUp()
    {
        alDeleteBuffers(buffer);
    }
    public void pauseAUDIO()
    {
        alSourcePause(source);
    }
    public void stopAUDIO()
    {
        alSourceStop(source);
    }
    //POSITION
    public static int POSITION=AL_POSITION;
    //LAUTSTÄRKE
    public static int GAIN=AL_GAIN;
    //GESCHWINDIGKEIT in Raumkoordinaten
    public static int VELOCITY=AL_VELOCITY;
    //AUSRICHTUNG
    public static int ORIENTATION=AL_ORIENTATION;
    //Beeinflusst die Quelle des Tons (Postition,Gain,Velocity)
    public void setSource(int pname, float x, float y, float z)
    {
        alSource3f(source,pname,x,y,z);
    }
    public void setSource(int pname, int x, int y, int z)
    {
        alSource3i(source,pname,x,y,z);
    }
}
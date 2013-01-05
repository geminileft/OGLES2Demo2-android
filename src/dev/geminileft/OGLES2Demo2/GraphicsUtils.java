package dev.geminileft.OGLES2Demo2;

import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

public class GraphicsUtils {

	private static Context mContext = null;
	private static int mCurrentProgram = -1;
	
	public static void setContext(Context context) {
		mContext = context;
	}
	
    public static String readShaderFile(String filename) {
		StringBuffer resultBuffer = new StringBuffer();
		try {
			final int BUFFER_SIZE = 1024;
			char buffer[] = new char[BUFFER_SIZE];
			int charsRead;
			InputStream stream = mContext.getAssets().open(filename);
			InputStreamReader reader = new InputStreamReader(stream);
    		resultBuffer = new StringBuffer();
			while ((charsRead = reader.read(buffer, 0, BUFFER_SIZE)) != -1) {
				resultBuffer.append(buffer, 0, charsRead);
			}
			reader.close();
			stream.close();
		} catch (Exception e) {
			Log.v("info", "very bad");
		}
		return resultBuffer.toString();
    }    

    public static int createProgram(String vertexSource, String fragmentSource) {
        int program = GLES20.glCreateProgram();
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        GLES20.glAttachShader(program, vertexShader);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != GLES20.GL_TRUE) {
            Log.e("Error", "Could not link program: ");
            Log.e("Error", GLES20.glGetProgramInfoLog(program));
            GLES20.glDeleteProgram(program);
            program = 0;
        }
        return program;
    }

    private static int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        GLES20.glShaderSource(shader, source);
        checkGlError("glShaderSource");
        GLES20.glCompileShader(shader);
        checkGlError("glCompileShader");
        return shader;
    }

    public static void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
        	//String errorMsg = GLES20.glGetProgramInfoLog(mCurrentProgram);
            //Log.e("info", op + ": glError " + error + errorMsg);
            throw new RuntimeException(op + ": glError " + error);
        }
    }
    
    public static void activateProgram(int programId) {
        mCurrentProgram = programId;
        GLES20.glUseProgram(programId);
    }
    
    public static int currentProgramId() {
    	return mCurrentProgram;
    }
}

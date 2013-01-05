package dev.geminileft.OGLES2Demo2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

public class OGLES2Renderer implements GLSurfaceView.Renderer {

	//size in bytes of float
    private final int FLOAT_SIZE = 4;
    private final int SHORT_SIZE = 2;
    private int maVertices;
    private int maColor;
    
	public OGLES2Renderer(GLSurfaceView view) {
		super();
		//set OGLES version
        view.setEGLContextClientVersion(2);
	}
	
	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		String vSource;
		String fSource;
		vSource = GraphicsUtils.readShaderFile("basic.vsh");
		fSource = GraphicsUtils.readShaderFile("basic.fsh");
		final int progId = GraphicsUtils.createProgram(vSource, fSource);
		GraphicsUtils.activateProgram(progId);
		//set clear color
		GLES20.glClearColor(0, 0, 0, 1);
		//read attribute locations and enable
		maColor = GLES20.glGetAttribLocation(progId, "aColor");
		GLES20.glEnableVertexAttribArray(maColor);
		maVertices = GLES20.glGetAttribLocation(progId, "aVertices");
		GLES20.glEnableVertexAttribArray(maVertices);
	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {
		final float halfWidth = width / 2.0f;
		final float halfHeight = height / 2.0f;
		
	    GLES20.glViewport(0, 0, width, height);
	    
	    //set matrices and pass to shader
	    float projMatrix[] = new float[16];
	    Matrix.orthoM(projMatrix, 0, -halfWidth, halfWidth, -halfHeight, halfHeight, -1, 1);
	    final int progId = GraphicsUtils.currentProgramId();
	    int uProjectionMatrix = GLES20.glGetUniformLocation(progId, "uProjectionMatrix");
	    GLES20.glUniformMatrix4fv(uProjectionMatrix, 1, false, projMatrix, 0);
	}

	@Override
	public void onDrawFrame(GL10 arg0) {
		//clear screen
	    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		//set color buffer and pass to shader
	    float colors[] = {
	        1.0f, 0.0f, 1.0f, 0.0f
	        , 1.0f, 0.0f, 1.0f, 0.0f
	        , 1.0f, 0.0f, 1.0f, 0.0f
	        , 1.0f, 0.0f, 1.0f, 0.0f
	    };
        FloatBuffer colorBuffer = ByteBuffer.allocateDirect(colors.length
                * FLOAT_SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
        colorBuffer.put(colors).position(0);
	    GLES20.glVertexAttribPointer(maColor, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);

	    //measure out vertices and set for quad
	    final float SIZE = 50;
	    final float leftX = -SIZE;
	    final float rightX = SIZE;
	    final float topY = SIZE;
	    final float bottomY = -SIZE;
	    
        FloatBuffer verticesBuffer;
        
	    //triangles
	    final float vertices[] = {
	    	leftX, bottomY
	        , rightX, bottomY
	        , leftX, topY
	        , rightX, topY
	    };
        verticesBuffer = ByteBuffer.allocateDirect(vertices.length
                * FLOAT_SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
        verticesBuffer.put(vertices).position(0);        
		GLES20.glVertexAttribPointer(maVertices, 2, GLES20.GL_FLOAT, false, 0, verticesBuffer);
		
        ShortBuffer indexBuffer;
        
	    //indices
	    final short indices[] = {
	    	0, 1, 2, 1, 2, 3
	    };
        indexBuffer = ByteBuffer.allocateDirect(indices.length
                * SHORT_SIZE).order(ByteOrder.nativeOrder()).asShortBuffer();
        indexBuffer.put(indices).position(0);

	    GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
	}
}

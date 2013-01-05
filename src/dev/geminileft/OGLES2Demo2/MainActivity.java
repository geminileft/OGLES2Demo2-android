package dev.geminileft.OGLES2Demo2;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GraphicsUtils.setContext(this);
		GLSurfaceView view = new GLSurfaceView(this);
		OGLES2Renderer renderer = new OGLES2Renderer(view);
		view.setRenderer(renderer);
		setContentView(view);
    }
}
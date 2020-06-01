package com.petitgrand.myapplication;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

/* Ce tutorial est issu d'un tutorial http://developer.android.com/training/graphics/opengl/index.html :
openGLES.zip HelloOpenGLES20
 */


public class OpenGLES20Activity extends Activity {

    // le conteneur View pour faire du rendu OpenGL
    private GLSurfaceView mGLView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Création de View et association à Activity
           MyGLSurfaceView : classe à implémenter et en particulier la partie renderer */

        /* Pour le plein écran */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //mGLView = new MyGLSurfaceView(this);

        /* Définition de View pour cette activité */
        //setContentView(mGLView);



    }
}

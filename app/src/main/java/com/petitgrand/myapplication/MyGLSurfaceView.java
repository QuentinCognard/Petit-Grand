/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.petitgrand.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petitgrand.myapplication.formes.Animal;

/* La classe MyGLSurfaceView avec en particulier la gestion des événements
  et la création de l'objet renderer

*/


/* On va dessiner un carré qui peut se déplacer grace à une translation via l'écran tactile */

public class MyGLSurfaceView extends GLSurfaceView {

    /* Un attribut : le renderer (GLSurfaceView.Renderer est une interface générique disponible) */
    /* MyGLRenderer va implémenter les méthodes de cette interface */

    private final MyGLRenderer mRenderer;


    public MyGLSurfaceView(Context context,AttributeSet attrs) {
        super(context,attrs);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        // Création d'un context OpenGLES 2.0
        setEGLContextClientVersion(3);
        setPreserveEGLContextOnPause(true);

        // Création du renderer qui va être lié au conteneur View créé

        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);

        // Option pour indiquer qu'on redessine uniquement si les données changent
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }

    /* pour gérer la translation */
    private float mPreviousX;
    private float mPreviousY;
    private boolean condition = false;

    /* Comment interpréter les événements sur l'écran tactile */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // Les coordonnées du point touché sur l'écran
        float x = e.getX();
        float y = e.getY();

        // la taille de l'écran en pixels
        float screen_x = getWidth();
        float screen_y = getHeight();



        // Des messages si nécessaires */
        Log.d("message", "x"+Float.toString(x));
        Log.d("message", "y"+Float.toString(y));
        Log.d("message", "screen_x="+Float.toString(screen_x));
        Log.d("message", "screen_y="+Float.toString(screen_y));


        /* accès aux paramètres du rendu (cf MyGLRenderer.java)
        soit la position courante du centre du carré
         */
        float[] pos = mRenderer.getPosition();
        float[] posEscargot = mRenderer.getPositionEscargot();
        float[] posGrenouille = mRenderer.getPositionGrenouille();
        float[] posNigloo = mRenderer.getPositionNigloo();
        float[] posRenard = mRenderer.getPositionRenard();
        float[] posBiche = mRenderer.getPositionBiche();
        float[] posOurs = mRenderer.getPositionOurs();

        float[] posMoins = mRenderer.getPositionMoins();
        float[] posEgal = mRenderer.getPositionEgal();
        float[] posPlus = mRenderer.getPositionPlus();
        float[] posStop = mRenderer.getPositionStop();

        /* Conversion des coordonnées pixel en coordonnées OpenGL
        Attention l'axe x est inversé par rapport à OpenGLSL
        On suppose que l'écran correspond à un carré d'arête 2 centré en 0
         */

        float x_opengl = 20.0f*x/getWidth() - 10.0f;
        float y_opengl = -30.0f*y/getHeight() + 10.0f;

        Log.d("message","x_opengl="+Float.toString(x_opengl));
        Log.d("message","y_opengl="+Float.toString(y_opengl));

        /* Le carré représenté a une arête de 2 (oui il va falloir changer cette valeur en dur !!)
        /* On teste si le point touché appartient au carré ou pas car on ne doit le déplacer que si ce point est dans le carré
        */

         boolean test_square = ((x_opengl < pos[0]+0.5) && (x_opengl > pos[0]-0.5) && (y_opengl < pos[1]+0.5) && (y_opengl > pos[1]-0.5));
        boolean test_escargot = ((x_opengl < posEscargot[0]+1.0) && (x_opengl > posEscargot[0]-1.0) && (y_opengl < posEscargot[1]+1.0) && (y_opengl > posEscargot[1]-1.0));
        boolean test_grenouille = ((x_opengl < posGrenouille[0]+2.0) && (x_opengl > posGrenouille[0]-2.0) && (y_opengl < posGrenouille[1]+1.5) && (y_opengl > posGrenouille[1]-1.0));
        boolean test_nigloo = ((x_opengl < posNigloo[0]+2.0) && (x_opengl > posNigloo[0]-2.0) && (y_opengl < posNigloo[1]+2.0) && (y_opengl > posNigloo[1]-1.0));
        boolean test_renard = ((x_opengl < posRenard[0]+2.5) && (x_opengl > posRenard[0]-2.5) && (y_opengl < posRenard[1]+1.75) && (y_opengl > posRenard[1]-1.75));
        boolean test_biche = ((x_opengl < posBiche[0]+2.0) && (x_opengl > posBiche[0]-2.0) && (y_opengl < posBiche[1]+2.5) && (y_opengl > posBiche[1]-2.5));
        boolean test_ours = ((x_opengl < posOurs[0]+2.5) && (x_opengl > posOurs[0]-2.5) && (y_opengl < posOurs[1]+3.0) && (y_opengl > posOurs[1]-3.0));



        boolean test_moins = ((x_opengl < posMoins[0]+2.5) && (x_opengl > posMoins[0]-2.5) && (y_opengl < posMoins[1]+2.5) && (y_opengl > posMoins[1]-2.5));
        boolean test_egal = ((x_opengl < posEgal[0]+2.5) && (x_opengl > posEgal[0]-2.5) && (y_opengl < posEgal[1]+2.5) && (y_opengl > posEgal[1]-2.5));
        boolean test_plus = ((x_opengl < posPlus[0]+2.5) && (x_opengl > posPlus[0]-2.5) && (y_opengl < posPlus[1]+2.5) && (y_opengl > posPlus[1]-2.5));
        boolean test_stop = ((x_opengl < posStop[0]+2.5) && (x_opengl > posStop[0]-2.5) && (y_opengl < posStop[1]+2.5) && (y_opengl > posStop[1]-2.5));

        Log.d("message","test_square="+Boolean.toString(test_square));
        Log.d("message","condition="+Boolean.toString(condition));

        if (test_square) {

            switch (e.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    mPreviousX = x_opengl;
                    mPreviousY = y_opengl;
                    mRenderer.setPosition(mPreviousX,mPreviousY);
                    requestRender(); // équivalent de glutPostRedisplay pour lancer le dessin avec les modifications.
            }
        }

        else if (test_escargot) {

            switch (e.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    mPreviousX = x_opengl;
                    mPreviousY = y_opengl;
                    mRenderer.setPositionEscargot(mPreviousX,mPreviousY);
                    requestRender(); // équivalent de glutPostRedisplay pour lancer le dessin avec les modifications.
            }
        }


        else if (test_grenouille) {

            switch (e.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    mPreviousX = x_opengl;
                    mPreviousY = y_opengl;
                    mRenderer.setPositionGrenouille(mPreviousX,mPreviousY);
                    requestRender(); // équivalent de glutPostRedisplay pour lancer le dessin avec les modifications.
            }
        }

        else if (test_nigloo) {

            switch (e.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    mPreviousX = x_opengl;
                    mPreviousY = y_opengl;
                    mRenderer.setPositionNigloo(mPreviousX,mPreviousY);
                    requestRender(); // équivalent de glutPostRedisplay pour lancer le dessin avec les modifications.
            }
        }


        else if (test_renard) {

            switch (e.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    mPreviousX = x_opengl;
                    mPreviousY = y_opengl;
                    mRenderer.setPositionRenard(mPreviousX,mPreviousY);
                    requestRender(); // équivalent de glutPostRedisplay pour lancer le dessin avec les modifications.
            }
        }

        else if (test_biche) {

            switch (e.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    mPreviousX = x_opengl;
                    mPreviousY = y_opengl;
                    mRenderer.setPositionBiche(mPreviousX,mPreviousY);
                    requestRender(); // équivalent de glutPostRedisplay pour lancer le dessin avec les modifications.
            }
        }

        else if (test_ours) {

            switch (e.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    mPreviousX = x_opengl;
                    mPreviousY = y_opengl;
                    mRenderer.setPositionOurs(mPreviousX,mPreviousY);
                    requestRender(); // équivalent de glutPostRedisplay pour lancer le dessin avec les modifications.
            }
        }


        else if (test_moins && mRenderer.getVictory()==false) {

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPreviousX = x;
                    mPreviousY = y;
                    condition=true;
                    break;
                case MotionEvent.ACTION_UP:
                    Animal ref = mRenderer.getRef();
                    Animal carte = mRenderer.getCarte();
                    if (carte.getHeight()< ref.getHeight()){
                        mRenderer.setRef(carte);
                        Log.d("message","PLUS PITI");
                        mRenderer.addTrouvee(carte);
                        mRenderer.popCarte();
                    }
                    else {
                        mRenderer.newPile();
                        mRenderer.remettreTrouvee();
                    }
                    mRenderer.setJoker();
                    requestRender(); // équivalent de glutPostRedisplay pour lancer le dessin avec les modifications.
                    condition=false;

            }
        }

        else if (test_egal && mRenderer.getVictory()==false) {

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPreviousX = x;
                    mPreviousY = y;
                    condition = true;
                    break;
                case MotionEvent.ACTION_UP:
                    Animal ref = mRenderer.getRef();
                    Animal carte = mRenderer.getCarte();
                    if (carte.getHeight() == ref.getHeight()) {
                        mRenderer.setRef(carte);
                        Log.d("message", "EGAL");
                        mRenderer.addTrouvee(carte);
                        mRenderer.popCarte();
                    }
                    else {
                        mRenderer.newPile();
                        mRenderer.remettreTrouvee();
                    }
                    mRenderer.setJoker();
                    requestRender(); // équivalent de glutPostRedisplay pour lancer le dessin avec les modifications.
                    condition = false;
            }
        }

        else if (test_plus && mRenderer.getVictory()==false) {

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPreviousX = x;
                    mPreviousY = y;
                    condition=true;
                    break;
                case MotionEvent.ACTION_UP:
                    Animal ref = mRenderer.getRef();
                    Animal carte = mRenderer.getCarte();
                    if (carte.getHeight() > ref.getHeight()){
                        mRenderer.setRef(carte);
                        Log.d("message","PLUS GRAND");
                        mRenderer.addTrouvee(carte);
                        mRenderer.popCarte();
                    }
                    else {
                        mRenderer.newPile();
                        mRenderer.remettreTrouvee();
                    }
                    mRenderer.setJoker();
                    requestRender(); // équivalent de glutPostRedisplay pour lancer le dessin avec les modifications.
                    condition=false;

            }
        }

        else if (test_stop && mRenderer.getVictory()==false) {

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPreviousX = x;
                    mPreviousY = y;
                    condition=true;
                    break;
                case MotionEvent.ACTION_UP:
                    mRenderer.useJoker();
                    requestRender(); // équivalent de glutPostRedisplay pour lancer le dessin avec les modifications.
                    condition=false;

            }
        }

        return true;
    }

}

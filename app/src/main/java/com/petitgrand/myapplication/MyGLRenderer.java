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

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Build;
import android.util.Log;

import com.petitgrand.myapplication.formes.Animal;
import com.petitgrand.myapplication.formes.Biche;
import com.petitgrand.myapplication.formes.CadreCartes;
import com.petitgrand.myapplication.formes.CadreMystere;
import com.petitgrand.myapplication.formes.Escargot;
import com.petitgrand.myapplication.formes.Grenouille;
import com.petitgrand.myapplication.formes.Interrogation;
import com.petitgrand.myapplication.formes.Joker;
import com.petitgrand.myapplication.formes.JokerHat;
import com.petitgrand.myapplication.formes.JokerMark;
import com.petitgrand.myapplication.formes.Nigloo;
import com.petitgrand.myapplication.formes.Ours;
import com.petitgrand.myapplication.formes.Pile;
import com.petitgrand.myapplication.formes.Renard;
import com.petitgrand.myapplication.formes.SigneEgal;
import com.petitgrand.myapplication.formes.SigneMoins;
import com.petitgrand.myapplication.formes.SignePlus;
import com.petitgrand.myapplication.formes.SigneStop;
import com.petitgrand.myapplication.formes.Square;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.RequiresApi;

/* MyGLRenderer implémente l'interface générique GLSurfaceView.Renderer */

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    private Square mSquare;
    private Escargot mEscargot;
    private Grenouille mGrenouille;
    private Nigloo mNigloo;
    private Renard mRenard;
    private Biche mBiche;
    private Ours mOurs;
    private CadreMystere mCadre1;
    private CadreCartes mCadre2;
    private Interrogation mInterrogation;
    private SigneEgal mEgal;
    private SigneMoins mMoins;
    private SignePlus mPlus;
    private SigneStop mStop;
    private Joker mJoker;
    private JokerHat mJokerHat;
    private JokerMark mJokerMark;

    private Animal mRefAnimal;
    private ArrayList<Pile> mPile = new ArrayList<>(14);
    private ArrayList<Pile> mPileRef = new ArrayList<>(14);
    private boolean joker = false;
    private boolean ready = true;
    private int vies = 3;
    private boolean victory = false;
    private int taille_jeu;

    // Les matrices habituelles Model/View/Projection

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];

    private float[] mSquarePosition = {-8.0f, 6.5f};
    private float[] mEscargotPosition = {-6.0f, 7.0f};
    private float[] mGrenouillePosition = {-3.0f, 7.0f};
    private float[] mNiglooPosition = {1.5f, 7.0f};
    private float[] mRenardPosition = {6.0f, 7.5f};
    private float[] mBichePosition = {-7.0f, 2.5f};
    private float[] mOursPosition = {-1.5f, 2.5f};
    private float[] mCadrePosition = {-6.0f, -7.0f};
    private float[] mCadrePosition2 = {6.0f, -7.0f};
    private float[] mInterrogationPosition = {-6.0f, -7.0f};
    private float[] mEgalPosition = {0.0f, -15.0f};
    private float[] mMoinsPosition = {-6f, -15.0f};
    private float[] mPlusPosition = {6f, -15.0f};
    private float[] mStopPosition = {6f, 1.5f};
    private float[] mJokerPosition = {6f, 1.5f};
    private float[] mReferencePosition = {6f, -7f};
    private float[] mDevinePosition = {-6f, -7f};


    private  ArrayList<Animal> les_animaux = new ArrayList<>();
    private ArrayList<Animal> les_cartes = new ArrayList<>(14);
    private ArrayList<Animal> les_cartes_trouvee = new ArrayList<>();



    /* Première méthode équivalente à la fonction init en OpenGLSL */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {


        // la couleur du fond d'écran
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);


        /* on va définir une classe Square pour dessiner des carrés */
        mSquare   = new Square(mSquarePosition);
        mEscargot   = new Escargot(mEscargotPosition);
        mGrenouille = new Grenouille(mGrenouillePosition);
        mNigloo = new Nigloo(mNiglooPosition);
        mRenard = new Renard(mRenardPosition);
        mBiche = new Biche(mBichePosition);
        mOurs = new Ours(mOursPosition);
        mCadre1 = new CadreMystere(mCadrePosition);
        mCadre2 = new CadreCartes(mCadrePosition2);
        mEgal = new SigneEgal(mEgalPosition);
        mMoins = new SigneMoins(mMoinsPosition);
        mPlus = new SignePlus(mPlusPosition);
        mStop = new SigneStop(mStopPosition);
        mInterrogation = new Interrogation(mInterrogationPosition);
        mJoker = new Joker(mJokerPosition);
        mJokerHat = new JokerHat(mJokerPosition);
        mJokerMark = new JokerMark(mJokerPosition);

        taille_jeu = 14;

        for (int i = 0; i <taille_jeu; i++){
            float coordY = i * 0.35f;
            float [] newCoord = {0.0f, -11f};
            newCoord[1]+=coordY;
            Pile elem = new Pile(newCoord);
            mPileRef.add(elem);
        }

        mPile = (ArrayList<Pile>) mPileRef.clone();

        les_animaux.add(mSquare);
        les_animaux.add(mEscargot);
        les_animaux.add(mGrenouille);
        les_animaux.add(mNigloo);
        les_animaux.add(mRenard);
        les_animaux.add(mBiche);
        les_animaux.add(mOurs);

        final int random = new Random().nextInt(7);
        mRefAnimal = les_animaux.get(random);
        mRefAnimal.set_position(mReferencePosition);

        int cpt_square = 2;
        int cpt_esacargot = 2;
        int cpt_grenouille = 2;
        int cpt_nigloo = 2;
        int cpt_renard = 2;
        int cpt_biche = 2;
        int cpt_ours = 2;

        int randomInd = 0;

        for (int y = 0; y < cpt_square; y++){
            les_cartes.add(new Square(mDevinePosition));
        }

        for (int y = 0; y < cpt_esacargot; y++){
            les_cartes.add(new Escargot(mDevinePosition));
        }

        for (int y = 0; y < cpt_grenouille; y++){
            les_cartes.add(new Grenouille(mDevinePosition));
        }

        for (int y = 0; y < cpt_nigloo; y++){
            les_cartes.add(new Nigloo(mDevinePosition));
        }

        for (int y = 0; y < cpt_renard; y++){
            les_cartes.add(new Renard(mDevinePosition));
        }

        for (int y = 0; y < cpt_biche; y++){
            les_cartes.add(new Biche(mDevinePosition));
        }

        for (int y = 0; y < cpt_ours; y++){
            les_cartes.add(new Ours(mDevinePosition));
        }

        Collections.shuffle(les_cartes);


        for (int i=0; i< les_cartes.size(); i ++){
            Log.d("oui",les_cartes.get(i).getClass().getSimpleName());

        }


    }

    /* Deuxième méthode équivalente à la fonction Display */
    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16]; // pour stocker une matrice
        float[] scratchEscargot = new float[16];
        float[] scratchGrenouille = new float[16];
        float[] scratchNigloo = new float[16];
        float[] scratchRenard = new float[16];
        float[] scratchBiche = new float[16];
        float[] scratchOurs = new float[16];
        float[] scratchCadre1 = new float[16];
        float[] scratchCadre2 = new float[16];
        float[] scratchInterrogation = new float[16];
        float[] scratchEgal = new float[16];
        float[] scratchMoins = new float[16];
        float[] scratchPlus = new float[16];
        float[] scratchStop = new float[16];
        float[] scratchRef = new float[16];
        float[] scratchCarte = new float[16];
        float[] scratchJoker = new float[16];
        float[] scratchJokerHat = new float[16];
        float[] scratchJokerMark = new float[16];


        // glClear rien de nouveau on vide le buffer de couleur et de profondeur */
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        /* on utilise une classe Matrix (similaire à glm) pour définir nos matrices P, V et M*/

        /* Pour le moment on va utiliser une projection orthographique
           donc View = Identity
         */

        /*pour positionner la caméra mais ici on n'en a pas besoin*/

        //Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 0,0f, 0f, 0f, 0f, 0.0f, 0.0f);
        Matrix.setIdentityM(mViewMatrix,0);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        Matrix.setIdentityM(mModelMatrix,0);

        /* Pour définir une translation on donne les paramètres de la translation
        et la matrice (ici mModelMatrix) est multipliée par la translation correspondante
         */
        Matrix.translateM(mModelMatrix, 0, mSquarePosition[0], mSquarePosition[1], 0);

        Log.d("Renderer", "mSquarex"+Float.toString(mSquarePosition[0]));
        Log.d("Renderer", "mSquarey"+Float.toString(mSquarePosition[1]));

        /* scratch est la matrice PxVxM finale */
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mModelMatrix, 0);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix, 0, mEscargotPosition[0], mEscargotPosition[1], 0);
        Matrix.multiplyMM(scratchEscargot, 0, mMVPMatrix, 0, mModelMatrix, 0);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix, 0, mGrenouillePosition[0], mGrenouillePosition[1], 0);
        Matrix.multiplyMM(scratchGrenouille, 0, mMVPMatrix, 0, mModelMatrix, 0);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix, 0, mNiglooPosition[0], mNiglooPosition[1], 0);
        Matrix.multiplyMM(scratchNigloo, 0, mMVPMatrix, 0, mModelMatrix, 0);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix, 0, mRenardPosition[0], mRenardPosition[1], 0);
        Matrix.multiplyMM(scratchRenard, 0, mMVPMatrix, 0, mModelMatrix, 0);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix, 0, mBichePosition[0], mBichePosition[1], 0);
        Matrix.multiplyMM(scratchBiche, 0, mMVPMatrix, 0, mModelMatrix, 0);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix, 0, mOursPosition[0], mOursPosition[1], 0);
        Matrix.multiplyMM(scratchOurs, 0, mMVPMatrix, 0, mModelMatrix, 0);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix, 0, mCadrePosition[0], mCadrePosition[1], 0);
        Matrix.multiplyMM(scratchCadre1, 0, mMVPMatrix, 0, mModelMatrix, 0);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix, 0, mCadrePosition2[0], mCadrePosition2[1], 0);
        Matrix.multiplyMM(scratchCadre2, 0, mMVPMatrix, 0, mModelMatrix, 0);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix, 0, mEgalPosition[0], mEgalPosition[1], 0);
        Matrix.multiplyMM(scratchEgal, 0, mMVPMatrix, 0, mModelMatrix, 0);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix, 0, mMoinsPosition[0], mMoinsPosition[1], 0);
        Matrix.multiplyMM(scratchMoins, 0, mMVPMatrix, 0, mModelMatrix, 0);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix, 0, mPlusPosition[0], mPlusPosition[1], 0);
        Matrix.multiplyMM(scratchPlus, 0, mMVPMatrix, 0, mModelMatrix, 0);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix, 0, mStopPosition[0], mStopPosition[1], 0);
        Matrix.multiplyMM(scratchStop, 0, mMVPMatrix, 0, mModelMatrix, 0);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix, 0, mReferencePosition[0], mReferencePosition[1], 0);
        Matrix.multiplyMM(scratchRef, 0, mMVPMatrix, 0, mModelMatrix, 0);

        for (int i=0; i< mPile.size(); i++){
            Matrix.setIdentityM(mModelMatrix,0);
            Matrix.translateM(mModelMatrix, 0, mPile.get(i).get_position()[0], mPile.get(i).get_position()[1], 0);
            float [] cratchPile = new float[16];
            Matrix.multiplyMM(cratchPile, 0, mMVPMatrix, 0, mModelMatrix, 0);
            mPile.get(i).draw(cratchPile);
        }



        if (ready == true){
            Matrix.setIdentityM(mModelMatrix,0);
            Matrix.translateM(mModelMatrix, 0, mJokerPosition[0], mJokerPosition[1], 0);
            Matrix.multiplyMM(scratchJoker, 0, mMVPMatrix, 0, mModelMatrix, 0);
            Matrix.multiplyMM(scratchJokerHat, 0, mMVPMatrix, 0, mModelMatrix, 0);
            Matrix.multiplyMM(scratchJokerMark, 0, mMVPMatrix, 0, mModelMatrix, 0);
            if (vies == 3){
                mJoker.draw(scratchJoker);
                mJokerHat.draw(scratchJokerHat);
                mJokerMark.draw(scratchJokerMark);
            }
            else if (vies == 2 ){
                mJoker.draw(scratchJoker);
                mJokerHat.draw(scratchJokerHat);
            }
            else if (vies == 1 ){
                mJoker.draw(scratchJoker);
            }


        }

        if (joker == true){
            Matrix.setIdentityM(mModelMatrix,0);
            Matrix.translateM(mModelMatrix, 0, mDevinePosition[0], mDevinePosition[1], 0);
            Matrix.multiplyMM(scratchCarte, 0, mMVPMatrix, 0, mModelMatrix, 0);
            les_cartes.get(les_cartes.size()-1).draw(scratchCarte);
        }
        else {
            Matrix.setIdentityM(mModelMatrix,0);
            Matrix.translateM(mModelMatrix, 0, mInterrogationPosition[0], mInterrogationPosition[1], 0);
            Matrix.multiplyMM(scratchInterrogation, 0, mMVPMatrix, 0, mModelMatrix, 0);
            mInterrogation.draw(scratchInterrogation);
        }

        /* on appelle la méthode dessin du carré élémentaire */
        mSquare.draw(scratch);
        mEscargot.draw(scratchEscargot);
        mGrenouille.draw(scratchGrenouille);
        mNigloo.draw(scratchNigloo);
        mRenard.draw(scratchRenard);
        mBiche.draw(scratchBiche);
        mOurs.draw(scratchOurs);
        mCadre1.draw(scratchCadre1);
        mCadre2.draw(scratchCadre2);
        mEgal.draw(scratchEgal);
        mMoins.draw(scratchMoins);
        mPlus.draw(scratchPlus);
        mStop.draw(scratchStop);
        mRefAnimal.draw(scratchRef);

        if (victory==true){
            Matrix.setIdentityM(mModelMatrix,0);
            Matrix.translateM(mModelMatrix, 0, mEgalPosition[0], mEgalPosition[1], 0);
            Matrix.multiplyMM(scratchJoker, 0, mMVPMatrix, 0, mModelMatrix, 0);
            Matrix.multiplyMM(scratchJokerHat, 0, mMVPMatrix, 0, mModelMatrix, 0);
            Matrix.multiplyMM(scratchJokerMark, 0, mMVPMatrix, 0, mModelMatrix, 0);
            mJoker.draw(scratchJoker);
            mJokerHat.draw(scratchJokerHat);
            mJokerMark.draw(scratchJokerMark);

            Matrix.setIdentityM(mModelMatrix,0);
            Matrix.translateM(mModelMatrix, 0, mMoinsPosition[0], mMoinsPosition[1], 0);
            Matrix.multiplyMM(scratchJoker, 0, mMVPMatrix, 0, mModelMatrix, 0);
            Matrix.multiplyMM(scratchJokerHat, 0, mMVPMatrix, 0, mModelMatrix, 0);
            Matrix.multiplyMM(scratchJokerMark, 0, mMVPMatrix, 0, mModelMatrix, 0);
            mJoker.draw(scratchJoker);
            mJokerHat.draw(scratchJokerHat);
            mJokerMark.draw(scratchJokerMark);

            Matrix.setIdentityM(mModelMatrix,0);
            Matrix.translateM(mModelMatrix, 0, mPlusPosition[0], mPlusPosition[1], 0);
            Matrix.multiplyMM(scratchJoker, 0, mMVPMatrix, 0, mModelMatrix, 0);
            Matrix.multiplyMM(scratchJokerHat, 0, mMVPMatrix, 0, mModelMatrix, 0);
            Matrix.multiplyMM(scratchJokerMark, 0, mMVPMatrix, 0, mModelMatrix, 0);
            mJoker.draw(scratchJoker);
            mJokerHat.draw(scratchJokerHat);
            mJokerMark.draw(scratchJokerMark);

        }

    }

    /* équivalent au Reshape en OpenGLSL */
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        /* ici on aurait pu se passer de cette méthode et déclarer
        la projection qu'à la création de la surface !!
         */
        GLES30.glViewport(0, 0, width, height);
        Matrix.orthoM(mProjectionMatrix, 0, -10.0f, 10.0f, -20.0f, 10.0f, -1.0f, 1.0f);

    }

    /* La gestion des shaders ... */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
        int shader = GLES30.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);

        return shader;
    }


    /* Les méthodes nécessaires à la manipulation de la position finale du carré */
    public void setPosition(float x, float y) {
        /*mSquarePosition[0] += x;
        mSquarePosition[1] += y;*/
        mSquarePosition[0] = x;
        mSquarePosition[1] = y;

    }



    public float[] getPosition() {
        return mSquarePosition;
    }

    public float[] getPositionMoins() {
        return mMoinsPosition;
    }
    public float[] getPositionEgal() {
        return mEgalPosition;
    }
    public float[] getPositionPlus() {
        return mPlusPosition;
    }
    public float[] getPositionStop() {
        return mStopPosition;
    }

    public Animal getRef(){
        return mRefAnimal;
    }

    public void popCarte(){
        les_cartes.remove(les_cartes.size()-1);
        mPile.remove(mPile.size()-1);

        if (mPile.size()==0 || les_cartes.size()==0){
            victory=true;
        }
    }

    public Animal getCarte(){
        return les_cartes.get(les_cartes.size()-1);
    }

    public void setRef(Animal newAni) {
        /*mSquarePosition[0] += x;
        mSquarePosition[1] += y;*/
        mRefAnimal = newAni;

    }

    public void addTrouvee(Animal animal){
        les_cartes_trouvee.add(animal);
    }

    public void remettreTrouvee(){
        ArrayList<Animal> buffer_cartes = (ArrayList<Animal>) les_cartes.clone();
        les_cartes = (ArrayList<Animal>) les_cartes_trouvee.clone();
        for (int i=0; i < buffer_cartes.size(); i++){
            les_cartes.add(buffer_cartes.get(i));
        }
        les_cartes_trouvee.clear();
    }

    public void newPile(){
        mPile = (ArrayList<Pile>) mPileRef.clone();
        ready = true;
        vies = 3;
    }

    public void useJoker(){
        if (ready ==true && joker==false){
            vies--;
            joker = true;

            if (vies == 0){
                ready = false;
            }
        }
    }

    public void setJoker(){
        joker = false;
    }

    public boolean getVictory(){
        return victory;
    }

    public float[] getPositionEscargot() {
        return mEscargotPosition;
    }

    public float[] getPositionGrenouille() {
        return mGrenouillePosition;
    }

    public float[] getPositionNigloo() {
        return mNiglooPosition;
    }

    public float[] getPositionRenard() {
        return  mRenardPosition;
    }

    public float[] getPositionBiche() {
        return  mBichePosition;
    }

    public float[] getPositionOurs() {
        return  mOursPosition;
    }

    public void setPositionEscargot(float x, float y) {
        mEscargotPosition[0] = x;
        mEscargotPosition[1] = y;

    }

    public void setPositionGrenouille(float x, float y) {
        mGrenouillePosition[0] = x;
        mGrenouillePosition[1] = y;

    }

    public void setPositionNigloo(float x, float y) {
        mNiglooPosition[0] = x;
        mNiglooPosition[1] = y;

    }

    public void setPositionRenard(float x, float y) {
        mRenardPosition[0] = x;
        mRenardPosition[1] = y;

    }

    public void setPositionBiche(float x, float y) {
        mBichePosition[0] = x;
        mBichePosition[1] = y;

    }

    public void setPositionOurs(float x, float y) {
        mOursPosition[0] = x;
        mOursPosition[1] = y;

    }
}

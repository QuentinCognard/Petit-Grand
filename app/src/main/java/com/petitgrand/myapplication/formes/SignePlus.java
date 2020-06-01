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
package com.petitgrand.myapplication.formes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import android.util.Log;

//import android.opengl.GLES20;
import android.opengl.GLES30;

import com.petitgrand.myapplication.MyGLRenderer;


//Dessiner un signe plus

public class SignePlus {
    /* Le vertex shader avec la définition de gl_Position et les variables utiles au fragment shader
     */
    private final String vertexShaderCode =
            "#version 300 es\n"+
                    "uniform mat4 uMVPMatrix;\n"+
                    "in vec3 vPosition;\n" +
                    "in vec4 vCouleur;\n"+
                    "out vec4 Couleur;\n"+
                    "out vec3 Position;\n"+
                    "void main() {\n" +
                    "Position = vPosition;\n"+
                    "gl_Position = uMVPMatrix * vec4(vPosition,1.0);\n" +
                    "Couleur = vCouleur;\n"+
                    "}\n";

    private final String fragmentShaderCode =
            "#version 300 es\n"+
                    "precision mediump float;\n" + // pour définir la taille d'un float
                    "in vec4 Couleur;\n"+
                    "in vec3 Position;\n"+
                    "out vec4 fragColor;\n"+
                    "void main() {\n" +
                    "float x = Position.x;\n"+
                    "float y = Position.y;\n"+
                    "float test = x*x+y*y;\n"+
                    "if (test == 0.0) \n"+
                    "discard;\n"+
                    "fragColor = Couleur;\n" +
                    "}\n";

    /* les déclarations pour l'équivalent des VBO */

    private final FloatBuffer vertexBuffer; // Pour le buffer des coordonnées des sommets du carré
    private final ShortBuffer indiceBuffer; // Pour le buffer des indices
    private final FloatBuffer colorBuffer; // Pour le buffer des couleurs des sommets

    /* les déclarations pour les shaders
    Identifiant du programme et pour les variables attribute ou uniform
     */
    private final int IdProgram; // identifiant du programme pour lier les shaders
    private int IdPosition; // idendifiant (location) pour transmettre les coordonnées au vertex shader
    private int IdCouleur; // identifiant (location) pour transmettre les couleurs
    private int IdMVPMatrix; // identifiant (location) pour transmettre la matrice PxVxM

    static final int COORDS_PER_VERTEX = 3; // nombre de coordonnées par vertex
    static final int COULEURS_PER_VERTEX = 4; // nombre de composantes couleur par vertex

    int []linkStatus = {0};



    static float PlusCoords[] = {
            -2.5f,   2.5f, 0.0f,
            2.5f,  2.5f, 0.0f,
            2.5f,  -2.5f, 0.0f,
            -2.5f,  -2.5f, 0.0f,
            -2.5f,  2.0f, 0.0f,
            2.5f,  2.0f, 0.0f,
            -2.5f,   -2.0f, 0.0f,
            2.5f,  -2.0f, 0.0f,
            -2.0f,  2.0f, 0.0f,
            2.0f,   2.0f, 0.0f,
            -2.0f,  -2.0f, 0.0f,
            2.0f,  -2.0f, 0.0f,

            -1.0f, 0.25f,0.0f,
            1.0f,  0.25f, 0.0f,
            -1.0f, -0.25f,0.0f,
            1.0f,  -0.25f, 0.0f,

            0.25f, 1.0f,0.0f,
            0.25f,  -1.0f, 0.0f,
            -0.25f, 1.0f,0.0f,
            -0.25f,  -1.0f, 0.0f,

    };

    static float PlusColors[] = {
            0.69f,  0.24f, 0.36f, 1.0f,
            0.69f,  0.24f, 0.36f, 1.0f,
            0.69f,  0.24f, 0.36f, 1.0f,
            0.69f,  0.24f, 0.36f, 1.0f,
            0.69f,  0.24f, 0.36f, 1.0f,
            0.69f,  0.24f, 0.36f, 1.0f,
            0.69f,  0.24f, 0.36f, 1.0f,
            0.69f,  0.24f, 0.36f, 1.0f,
            0.69f,  0.24f, 0.36f, 1.0f,
            0.69f,  0.24f, 0.36f, 1.0f,
            0.69f,  0.24f, 0.36f, 1.0f,
            0.69f,  0.24f, 0.36f, 1.0f,
            0.69f,  0.24f, 0.36f, 1.0f,
            0.69f,  0.24f, 0.36f, 1.0f,
            0.69f,  0.24f, 0.36f, 1.0f,
            0.69f,  0.24f, 0.36f, 1.0f,
            0.69f,  0.24f, 0.36f, 1.0f,
            0.69f,  0.24f, 0.36f, 1.0f,
            0.69f,  0.24f, 0.36f, 1.0f,
            0.69f,  0.24f, 0.36f, 1.0f};

    private final short Indices[] = { 0,1,4,1,4,5,4,8,6,8,6,10,6,3,2,6,7,2,9,5,11,5,11,7,12,13,14,13,14,15,16,17,18,17,18,19 };

    private final int vertexStride = COORDS_PER_VERTEX * 4; // le pas entre 2 sommets : 4 bytes per vertex

    private final int couleurStride = COULEURS_PER_VERTEX * 4; // le pas entre 2 couleurs

    private final float Position[] = {0.0f,0.0f};

    public SignePlus(float[] Pos) {

        Position[0] = Pos[0];
        Position[1] = Pos[1];
        // initialisation du buffer pour les vertex (4 bytes par float)
        ByteBuffer bb = ByteBuffer.allocateDirect(PlusCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(PlusCoords);
        vertexBuffer.position(0);


        // initialisation du buffer pour les couleurs (4 bytes par float)
        ByteBuffer bc = ByteBuffer.allocateDirect(PlusColors.length * 4);
        bc.order(ByteOrder.nativeOrder());
        colorBuffer = bc.asFloatBuffer();
        colorBuffer.put(PlusColors);
        colorBuffer.position(0);

        // initialisation du buffer des indices
        ByteBuffer dlb = ByteBuffer.allocateDirect(Indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        indiceBuffer = dlb.asShortBuffer();
        indiceBuffer.put(Indices);
        indiceBuffer.position(0);

        /* Chargement des shaders */
        int vertexShader = MyGLRenderer.loadShader(
                GLES30.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(
                GLES30.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        IdProgram = GLES30.glCreateProgram();             // create empty OpenGL Program
        GLES30.glAttachShader(IdProgram, vertexShader);   // add the vertex shader to program
        GLES30.glAttachShader(IdProgram, fragmentShader); // add the fragment shader to program
        GLES30.glLinkProgram(IdProgram);                  // create OpenGL program executables
        GLES30.glGetProgramiv(IdProgram, GLES30.GL_LINK_STATUS,linkStatus,0);


    }


    public void set_position(float[] pos) {
        Position[0]=pos[0];
        Position[1]=pos[1];
    }
    /* La fonction Display */
    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES30.glUseProgram(IdProgram);

        // get handle to shape's transformation matrix
        IdMVPMatrix = GLES30.glGetUniformLocation(IdProgram, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES30.glUniformMatrix4fv(IdMVPMatrix, 1, false, mvpMatrix, 0);


        // get handle to vertex shader's vPosition member et vCouleur member
        IdPosition = GLES30.glGetAttribLocation(IdProgram, "vPosition");
        IdCouleur = GLES30.glGetAttribLocation(IdProgram, "vCouleur");

        /* Activation des Buffers */
        GLES30.glEnableVertexAttribArray(IdPosition);
        GLES30.glEnableVertexAttribArray(IdCouleur);

        /* Lecture des Buffers */
        GLES30.glVertexAttribPointer(
                IdPosition, COORDS_PER_VERTEX,
                GLES30.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        GLES30.glVertexAttribPointer(
                IdCouleur, COULEURS_PER_VERTEX,
                GLES30.GL_FLOAT, false,
                couleurStride, colorBuffer);




        // Draw the square
        GLES30.glDrawElements(
                GLES30.GL_TRIANGLES, Indices.length,
                GLES30.GL_UNSIGNED_SHORT, indiceBuffer);


        // Disable vertex array
        GLES30.glDisableVertexAttribArray(IdPosition);
        GLES30.glDisableVertexAttribArray(IdCouleur);

    }

}
package com.petitgrand.myapplication.formes;


public abstract class Animal {

    private final float Position[] = {0.0f,0.0f};


    public void set_position(float[] pos) {
        Position[0]=pos[0];
        Position[1]=pos[1];
    }

    /* La fonction Display */
    public void draw(float[] mvpMatrix) {

    }

    public float getHeight(){
        return 0.0f;
    }
}

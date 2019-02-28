package com.batataproductions.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.batataproductions.game.controller.GameController;

public class MotionDetector {
    private float initialYaw;
    private float initialRoll;
    private float timeToNextWeapon;

    private final static float VAR_YAW = 30f;
    private final static float VAR_ROLL = 60f;

    private static final float D_TIME_TO_NEXT_WEAPON = 2f;

    public MotionDetector(){
        timeToNextWeapon = -1f;
        calibrate();

    }

    public float[] getRotation(){
        float[] mat = new float[4 * 4];

        Gdx.input.getRotationMatrix(mat);

        Matrix4 m = new Matrix4(mat);

        Quaternion q = m.getRotation(new Quaternion());


        return new float[] {q.getPitch(), q.getRoll(), q.getYaw()};
    }

    public void calibrate(){
        float[] tmp = getRotation();
        initialYaw = tmp[2];
        initialRoll = tmp[1];

    }

    public void update(float delta){

        if (!(timeToNextWeapon < 0))
            timeToNextWeapon -= delta;

        float[] tmp = getRotation();

        if ( tmp[2] > -180 + initialYaw && tmp[2] < initialYaw ){
            // reload
            if (Math.abs(tmp[2] - initialYaw) > VAR_YAW){
                GameController.getInstance().shoot();
            }
        }
        else if  ( tmp[2] > initialYaw && tmp[2] < 180 + initialYaw ){
            // move phone to
            if (Math.abs(initialYaw - tmp[2]) > VAR_YAW){
                GameController.getInstance().reload();
            }
        }
        else if (  tmp[1] > -180 + initialRoll && tmp[1] < initialRoll  && timeToNextWeapon < 0 ){
            // move phone to change to next weapon
            if (Math.abs(tmp[1] - initialRoll) > VAR_ROLL){

                GameController.getInstance().switchToWeapon(false);
                timeToNextWeapon = D_TIME_TO_NEXT_WEAPON;
            }
        }
        else if (  tmp[1] > initialRoll && tmp[1] < 180 + initialRoll && timeToNextWeapon < 0){
            // move phone to change to previous weapon
            if (Math.abs(initialRoll - tmp[1]) > VAR_ROLL){

                GameController.getInstance().switchToWeapon(true);
                timeToNextWeapon = D_TIME_TO_NEXT_WEAPON;
            }
        }


    }
}

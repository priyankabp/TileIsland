/*
 * This is the level three of the game.
 **/
package com.example.android.tileisland;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class LevelOneStageThree extends AppCompatActivity {

    // User Session Manager Class
    UserSessionManagement session;
    ActivityTracker activityTracker;

    StartDraggingLsntr myStartDraggingLsntr;
    EndDraggingLsntr myEndDraggingLsntr;
    Button playButton;
    ImageView player;
    MediaPlayer waves;
    AnimatorSet set;
    TextView coinsCollected, totalScore;
    int score;
    int coins;
    String logInKid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_one_stage_three);

        // Session class instance
        session = new UserSessionManagement(getApplicationContext());

        logInKid = session.getUserDetails();

        //listeners for drag and drop
        myStartDraggingLsntr = new StartDraggingLsntr();
        myEndDraggingLsntr = new EndDraggingLsntr();

        //buttons binding to the activity
        player = (ImageView) findViewById(R.id.player);
        playButton = (Button) findViewById(R.id.playButton);
        findViewById(R.id.playButton).setOnLongClickListener(myStartDraggingLsntr);
        findViewById(R.id.moveUp).setOnLongClickListener(myStartDraggingLsntr);
        findViewById(R.id.moveDown).setOnLongClickListener(myStartDraggingLsntr);
        findViewById(R.id.moveLeft).setOnLongClickListener(myStartDraggingLsntr);
        findViewById(R.id.moveRight).setOnLongClickListener(myStartDraggingLsntr);

        // selected moves
        findViewById(R.id.firstAction).setOnDragListener(myEndDraggingLsntr);
        findViewById(R.id.secondAction).setOnDragListener(myEndDraggingLsntr);
        findViewById(R.id.thirdAction).setOnDragListener(myEndDraggingLsntr);
        findViewById(R.id.fourthAction).setOnDragListener(myEndDraggingLsntr);
        findViewById(R.id.fifthAction).setOnDragListener(myEndDraggingLsntr);
        findViewById(R.id.sixthAction).setOnDragListener(myEndDraggingLsntr);

        player.bringToFront();

        //updating score as per the coins are collected
        score = 0;
        coins = 0;
        coinsCollected = (TextView) findViewById(R.id.coinsCollected);
        totalScore = (TextView) findViewById(R.id.score);
        updateScore();

        //initializing music for coin pickup
        waves = MediaPlayer.create(LevelOneStageThree.this, R.raw.coinpickup);

    }

    //updating score simultaneously coins are collected
    private void updateScore() {
        score = coins * 1000;
        coinsCollected.setText(Integer.toString(coins));
        totalScore.setText(Integer.toString(score));
    }

    //navigates to previous activity
    public void onBackClick(View view) {
        Intent intent = new Intent(this, LevelOneStages.class);
        startActivity(intent);
    }

    //check the selected move by the player and return the list
    public ArrayList<String> getActionSequence() {

        String moveOne = (String) findViewById(R.id.firstAction).getContentDescription();
        String moveTwo = (String) findViewById(R.id.secondAction).getContentDescription();
        String moveThree = (String) findViewById(R.id.thirdAction).getContentDescription();
        String moveFour = (String) findViewById(R.id.fourthAction).getContentDescription();
        String moveFive = (String) findViewById(R.id.fifthAction).getContentDescription();
        String moveSix = (String) findViewById(R.id.sixthAction).getContentDescription();

        ArrayList<String> actionSequence = new ArrayList<String>();
        actionSequence.add(moveOne);
        actionSequence.add(moveTwo);
        actionSequence.add(moveThree);
        actionSequence.add(moveFour);
        actionSequence.add(moveFive);
        actionSequence.add(moveSix);
        return actionSequence;
    }

    // animates the player as per the selected moves
    public void onPlayClick(View view) {

        ArrayList<String> actionSequence = getActionSequence();

        //required sequence defined in the list
        ArrayList<String> requiredActionSequence = new ArrayList<String>();
        requiredActionSequence.add("down");
        requiredActionSequence.add("right");
        requiredActionSequence.add("up");
        requiredActionSequence.add("right");
        requiredActionSequence.add("down");
        requiredActionSequence.add("right");

        // checks if the reqired and selected sequence is correct
        if (requiredActionSequence.equals(actionSequence)) {

            ObjectAnimator actionOneAnimation = ObjectAnimator.ofFloat(player, "translationY", 0f, 410f);
            ObjectAnimator actionTwoAnimation = ObjectAnimator.ofFloat(player, "translationX", 0f, 394f);
            ObjectAnimator actionThreeAnimation = ObjectAnimator.ofFloat(player, "translationY", 392f, 0f);
            ObjectAnimator actionFourAnimation = ObjectAnimator.ofFloat(player, "translationX", 392f, 788f);
            ObjectAnimator actionFiveAnimation = ObjectAnimator.ofFloat(player, "translationY", 0f, 402f);
            ObjectAnimator actionSixAnimation = ObjectAnimator.ofFloat(player, "translationX", 788f, 1350f);
            set = new AnimatorSet();
            set.setDuration(3000);
            //animating all moves simultaneously
            set.playSequentially(actionOneAnimation,
                    actionTwoAnimation,
                    actionThreeAnimation,
                    actionFourAnimation,
                    actionFiveAnimation,
                    actionSixAnimation);
            set.start();

            actionTwoAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float imageXPosition = (Float) animation.getAnimatedValue();

                    if (imageXPosition >= 328) {
                        //coin music
                        waves.start();
                        //collecting coins
                        findViewById(R.id.stagethree_coin1).setVisibility(View.INVISIBLE);
                        //updating score
                        coins = 1;
                        updateScore();
                    }
                    if ((int) imageXPosition >= 394) {
                        //coin music
                        waves.start();
                        //collecting coins
                        findViewById(R.id.stagethree_coin2).setVisibility(View.INVISIBLE);
                        //updating score
                        coins = 2;
                        updateScore();
                    }
                }
            });

            actionFourAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float imageXPosition = (Float) animation.getAnimatedValue();
                    if ((int) imageXPosition >= 394) {
                        //coin music
                        waves.start();
                        //collecting coins
                        findViewById(R.id.stagethree_coin3).setVisibility(View.INVISIBLE);
                        //updating score
                        coins = 3;
                        updateScore();
                    }
                    if ((int) imageXPosition >= 550) {
                        //coin music
                        waves.start();
                        //collecting coins
                        findViewById(R.id.stagethree_coin4).setVisibility(View.INVISIBLE);
                        //updating score
                        coins = 4;
                        updateScore();
                    }
                    if ((int) imageXPosition >= 616) {
                        //coin music
                        waves.start();
                        //collecting coins
                        findViewById(R.id.stagethree_coin5).setVisibility(View.INVISIBLE);
                        //updating score
                        coins = 5;
                        updateScore();
                    }
                }
            });

            actionSixAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float imageXPosition = (Float) animation.getAnimatedValue();
                    if ((int) imageXPosition >= 1000) {
                        waves.start();
                        //collecting coins
                        findViewById(R.id.stagethree_coin6).setVisibility(View.INVISIBLE);
                        //updating score
                        coins = 6;
                        updateScore();
                    }
                    if ((int) imageXPosition >= 1070) {
                        waves.start();
                        //collecting coins
                        findViewById(R.id.stagethree_coin7).setVisibility(View.INVISIBLE);
                        //updating score
                        coins = 7;
                        updateScore();
                    }
                    if ((int) imageXPosition >= 1300) {

                        activityTracker = new ActivityTracker(getApplicationContext(), logInKid);
                        activityTracker.updateActivity("LevelOneStageThree", String.valueOf(score));

                        // Dailouge for playing again the same level or next level
                        AlertDialog.Builder alertadd = new AlertDialog.Builder(LevelOneStageThree.this);
                        LayoutInflater factory = LayoutInflater.from(LevelOneStageThree.this);
                        final View youwin = factory.inflate(R.layout.activity_winning, null);
                        alertadd.setView(youwin);
                        alertadd.setMessage("Score: " + score);
                        alertadd.setPositiveButton("Play Again!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int something) {
                                Intent intent = new Intent(LevelOneStageThree.this, LevelOneStageThree.class);
                                startActivity(intent);
                            }
                        });
                        alertadd.setNegativeButton("PlayAgain Level 1!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int something) {
                                Intent intent = new Intent(LevelOneStageThree.this, WelcomeLevelOne.class);
                                startActivity(intent);
                            }
                        });
                        alertadd.setPositiveButton("GoTo Level 2! ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int something) {
                                Intent intent = new Intent(LevelOneStageThree.this, WelcomeLevelTwo.class);
                                startActivity(intent);
                            }
                        });
                        AlertDialog alert = alertadd.create();
                        alert.show();
                        waves = MediaPlayer.create(LevelOneStageThree.this, R.raw.gameover);
                        waves.start();

                    }
                }

            });

        } else {
            activityTracker = new ActivityTracker(getApplicationContext(), logInKid);
            activityTracker.updateActivity("LevelOneStageThree", String.valueOf(score));
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setMessage("Unsuccessful attempt!");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Re-attempt", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
        }

    }

    // Restarts the same activity
    public void onStopClick(View view) {

        Intent intent = new Intent(this, LevelOneStageThree.class);
        startActivity(intent);

    }

    //game ic_exit
    public void onExitClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    //start dragging lisntr
    private class StartDraggingLsntr implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            WithDraggingShadow shadow = new WithDraggingShadow(v);
            ClipData data = ClipData.newPlainText("", "");
            v.startDrag(data, shadow, v, 0);
            return false;
        }
    }

    //end dragging lisntr
    private class EndDraggingLsntr implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            if (event.getAction() == DragEvent.ACTION_DROP) {
                ((Button) v).setBackground(((Button) event.getLocalState()).getBackground());
                ((Button) v).setContentDescription(((Button) event.getLocalState()).getContentDescription());
            }

            return true;
        }
    }

    //Bitmap image;
    private class WithDraggingShadow extends View.DragShadowBuilder {
        public WithDraggingShadow(View view) {
            super(view);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            super.onDrawShadow(canvas);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
            super.onProvideShadowMetrics(shadowSize, shadowTouchPoint);
        }
    }
}


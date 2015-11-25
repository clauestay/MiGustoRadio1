package com.yuki91.radiobase;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import com.yuki91.radiobase.Util.ConnectivityObserver;
import com.yuki91.radiobase.Util.LiveConnectivityManager;

public class MainActivity extends Activity implements ConnectivityObserver {

    static FloatingActionsMenu menuMultipleActions;
    static FloatingActionButton accionC;
    static FloatingActionButton accionA;
    static FloatingActionButton accionB;
    static FloatingActionButton plause;
    static Button btnEscucharAm;
    private String STREAM_URL ="http://streaming.radionomy.com/RadioAnimeMX";
    private MediaPlayer mPlayer;

    public static final String FACEBOOK_URL = "https://www.facebook.com/RadioAnimeTuMusicaAnime";
    //public static final String FACEBOOK_ID = "fb://profile/100005868240617";
    public static final String TWITTER_URL = "https://twitter.com/radioanimemx";
    public static final String TWITTER_ID = "twitter://user?user_id=599822052";
    public static final String YOUTUBE_URL = "http://www.youtube.com/user/radioanimemx" ;

    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayer=new MediaPlayer();
        plause = (FloatingActionButton) findViewById(R.id.plause);
        plause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!mPlayer.isPlaying()){
                        mPlayer.reset();
                        mPlayer.setDataSource(STREAM_URL);
                        mPlayer.prepareAsync();

                        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                                plause.setImageResource(android.R.drawable.ic_media_pause);

                            }
                        });
                    }else{
                        mPlayer.stop();
                        plause.setImageResource(android.R.drawable.ic_media_play);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

//        btnEscucharAm = (Button) findViewById(R.id.btnEscucharAm);
//        btnEscucharAm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    if(!mPlayer.isPlaying()){
//                        mPlayer.reset();
//                        mPlayer.setDataSource(STREAM_URL);
//                        mPlayer.prepareAsync();
//
//                        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                            @Override
//                            public void onPrepared(MediaPlayer mp) {
//                                mp.start();
//                                btnEscucharAm.setText("Stop");
//                            }
//                        });
//                    }else{
//                        mPlayer.stop();
//                        btnEscucharAm.setText("Play");
//                    }
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });


        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());

        accionB = (FloatingActionButton) findViewById(R.id.action_b);
        accionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //accionB.setTitle("Accion B clickeada");
                try {
                Intent faceIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URL));
                startActivity(faceIntent);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Lo sentimos no se puede realizar esta accion", Toast.LENGTH_SHORT).show();
            }
        }

        });

        accionA = (FloatingActionButton) findViewById(R.id.action_a);
        accionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //accionA.setTitle("Action A clicked");
                PackageManager pm=getPackageManager();
                try{
                    pm.getPackageInfo("com.twitter.android",0);
                    Intent twIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(TWITTER_ID));
                    startActivity(twIntent);
                }catch (Exception e){
                    Intent twIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(TWITTER_URL));
                    startActivity(twIntent);

                }
            }
        });

       accionC = (FloatingActionButton) findViewById(R.id.action_c);
        accionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //accionC.setTitle("accion c!");
                try {
                    Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL));
                    startActivity(youtubeIntent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Lo sentimos no se puede realizar esta accion", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LiveConnectivityManager.singleton(this).addObserver(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        LiveConnectivityManager.singleton(this).addObserver(this);
    }

    @Override
    public void manageNotification(boolean connectionEnabled) {

        if(connectionEnabled){
            plause.setEnabled(true);
            menuMultipleActions.setEnabled(true);
            //Toast.makeText(MainActivity.this, "Conectado", Toast.LENGTH_LONG).show();
        }else{
            if(mPlayer.isPlaying()){
                mPlayer.stop();
                mPlayer.reset();
                plause.setEnabled(false);
                menuMultipleActions.setEnabled(false);
                Toast.makeText(MainActivity.this, "Sin Conexion a Internet", Toast.LENGTH_LONG).show();

            }else{
                plause.setEnabled(false);
                menuMultipleActions.setEnabled(false);
                Toast.makeText(MainActivity.this, "Sin Conexion a Internet", Toast.LENGTH_LONG).show();
            }

        }
    }
    @Override
    public void onPause() {
        super.onPause();
        LiveConnectivityManager.singleton(this).removeObserver(this);
    }

    @Override
    public void onBackPressed(){
        if(back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        }else {
            Toast.makeText(getBaseContext(), "Pulse una vez mas para salir", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putBoolean("estado_plause", plause.isEnabled());
    }

}

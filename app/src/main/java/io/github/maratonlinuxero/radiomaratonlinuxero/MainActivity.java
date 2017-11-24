package io.github.maratonlinuxero.radiomaratonlinuxero;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btnReproducir;

    MediaPlayer mediaPlayer;

    boolean prepared = false;
    boolean started = false;

    //Stream
    String stream = "http://200.24.229.253:8000/maratonlinuxero.ogg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnReproducir = (Button) findViewById(R.id.btnReproducir);
        btnReproducir.setEnabled(false);
        btnReproducir.setText("Cargando");

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        new PlayerTask().execute(stream);



        btnReproducir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (started){
                    started = false;
                    mediaPlayer.pause();
                    btnReproducir.setText("Reproducir");
                }else{
                    started = true;
                    mediaPlayer.start();
                    btnReproducir.setText("Pausa");
                }
            }
        });
    }


    class PlayerTask extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepared = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return prepared;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean){
            super.onPostExecute(aBoolean);
            mediaPlayer.start();
            btnReproducir.setEnabled(true);
            btnReproducir.setText("Reproduciendo");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(started){
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(started){
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (prepared){
            mediaPlayer.release();
        }
    }
}

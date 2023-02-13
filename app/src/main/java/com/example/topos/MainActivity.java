package com.example.topos;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements Runnable {
    static int cont = 0;
    static int tiempo = 0;
    MediaPlayer disparo1, disparo2, disparo3, disparo4;
    MediaPlayer hit1, hit2, hit3, hit4;
    MediaPlayer musica;
    MediaPlayer win;
    ImageView i1, i2, i3, i4;
    AnimationDrawable animation1, animation2;
    public static boolean sonido = true;
    public static boolean efectos = true;
    int hitbox = 133;
    Random rand = new Random();
    int puntuacionMax = 30;
    TextView t1, t2, tf1, tf2, tf3, tf4;
    public Thread th1 = this.hilo();
    Button b1;
    boolean a = false;
    float x1, x2, y1, y2;
    private int coordx1, coordy1, coordx2, coordy2, coordx3, coordy3, coordx4, coordy4;
    private Lienzo fondo;
    private Bitmap diana, pidgey_malo;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1 = findViewById(R.id.textView);
        t2 = findViewById(R.id.textView3);
        tf1 = findViewById(R.id.textView2);
        tf2 = findViewById(R.id.textView6);
        tf3 = findViewById(R.id.textView8);
        tf4 = findViewById(R.id.textView9);
        b1 = findViewById(R.id.button2);

        th1.start();
        t1.setText(String.valueOf(tiempo));
        t2.setText(String.valueOf(cont));
        coordx2 = 600;
        coordy2 = 750;
        coordx1 = 1800;
        coordy1 = 1800;
        coordx3 = 1800;
        coordy3 = 1800;
        coordx4 = 1800;
        coordy4 = 1800;
        ConstraintLayout layout1 = findViewById(R.id.layout1);
        fondo = new Lienzo(this);
        //fondo.setOnTouchListener(this);
        disparo1 = MediaPlayer.create(this, R.raw.disparo);
        disparo2 = MediaPlayer.create(this, R.raw.disparo);
        disparo3 = MediaPlayer.create(this, R.raw.disparo);
        disparo4 = MediaPlayer.create(this, R.raw.disparo);
        hit1 = MediaPlayer.create(this, R.raw.hit);
        hit2 = MediaPlayer.create(this, R.raw.hit);
        hit3 = MediaPlayer.create(this, R.raw.hit);
        hit4 = MediaPlayer.create(this, R.raw.hit);
        musica = MediaPlayer.create(this, R.raw.musica);
        win = MediaPlayer.create(this, R.raw.victoria);
        i1 = (ImageView) findViewById(R.id.imageView5);
        i2 = (ImageView) findViewById(R.id.imageView6);
        i3 = (ImageView) findViewById(R.id.imageView7);
        i4 = (ImageView) findViewById(R.id.imageView8);
        i1.setBackgroundResource(R.drawable.confeti_der);
        i2.setBackgroundResource(R.drawable.confeti_izq);
        animation1 = (AnimationDrawable) i1.getBackground();
        animation2 = (AnimationDrawable) i2.getBackground();
        fondo.setOnTouchListener((view, motionEvent) -> {
            return touch(motionEvent);
        });


        if (sonido == true) {
            musica.start();
            musica.setLooping(true);
        } else if (sonido == false) {
            musica.release();
        }
        layout1.addView(fondo);

        diana = BitmapFactory.decodeResource(getResources(), R.drawable.pidgey_f2);
        pidgey_malo = BitmapFactory.decodeResource(getResources(), R.drawable.pidgey_malo_f2);

        new Thread(this).start();
    }

    //Hilo del timer, esto lo documenta ander
    private Thread hilo() {
        return new Thread() {
            @Override
            public void run() {
                while (puntuacionMax > MainActivity.cont) {
                    try {
                        sleep(1000);
                        //System.out.println("-------------------hilo");
                    } catch (Exception e) {
                        System.out.println("Error en el hilo");
                    }
                    MainActivity.tiempo++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            t1.setText(String.valueOf(MainActivity.tiempo));
                        }
                    });
                }

            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuopciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Instrucciones) {
            Intent intent = new Intent(this, Instrucciones.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    public void reset(View view) {

        tf1.setVisibility(View.INVISIBLE);
        tf2.setVisibility(View.INVISIBLE);
        tf4.setVisibility(View.INVISIBLE);
        tiempo = 0;
        try {
            th1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        coordx4 = topoX();
        t1.setText(String.valueOf(tiempo));
        tf3.setVisibility(View.INVISIBLE);
        t1.setVisibility(View.VISIBLE);
        t2.setVisibility(View.VISIBLE);
        b1.setVisibility(View.INVISIBLE);
        cont = 0;
        t2.setText(String.valueOf(cont));
        th1 = this.hilo();
        th1.start();
        i1.setVisibility(View.INVISIBLE);
        i2.setVisibility(View.INVISIBLE);
        i3.setVisibility(View.VISIBLE);
        i4.setVisibility(View.VISIBLE);
        animation1.stop();
        animation2.stop();

    }

    public void hit(float xDedo, float yDedo) {
        if (cont < puntuacionMax) {
            if ((coordx1 + hitbox) > xDedo && (coordx1 - hitbox) < xDedo) {
                if ((coordy1 + hitbox) > yDedo && (coordy1 - hitbox) < yDedo) {
                    coordx1 = topoX();
                    coordy1 = topoY();
                    cont++;
                    if (efectos == true) {
                        disparo1.start();
                        hit1.start();
                    }
                    t2.setText(String.valueOf(cont));
                    //fin();

                }
            }
            if ((coordx2 + hitbox) > xDedo && (coordx2 - hitbox) < xDedo) {
                if ((coordy2 + hitbox) > yDedo && (coordy2 - hitbox) < yDedo) {
                    coordx2 = topoX();
                    coordy2 = topoY();
                    cont++;
                    if (efectos == true) {
                        disparo2.start();
                        hit2.start();
                    }
                    t2.setText(String.valueOf(cont));
                    //fin();
                }
            }
            if ((coordx3 + hitbox) > xDedo && (coordx3 - hitbox) < xDedo) {
                if ((coordy3 + hitbox) > yDedo && (coordy3 - hitbox) < yDedo) {
                    coordx3 = topoX();
                    coordy3 = topoY();
                    cont++;
                    if (efectos == true) {
                        disparo3.start();
                        hit3.start();
                    }

                    t2.setText(String.valueOf(cont));
                    //fin();
                }
            }
            if ((coordx4 + hitbox + 50) > xDedo && (coordx4 - hitbox - 50) < xDedo) {
                if ((coordy4 + hitbox + 50) > yDedo && (coordy4 - hitbox - 50) < yDedo) {
                    coordx4 = topoX();
                    coordy4 = topoY();
                    cont = cont - 5;
                    if (efectos == true) {
                        hit4.start();
                        disparo4.start();
                    }
                    if (cont < 0) {
                        cont = 1;
                    }

                    t2.setText(String.valueOf(cont));
                }
            } else {
                if (efectos == true) {
                    disparo4.start();
                }
            }
        } else {
            win.start();
            tf1.setVisibility(View.VISIBLE);
            tf2.setVisibility(View.VISIBLE);
            tf4.setVisibility(View.VISIBLE);
            tf3.setText(String.valueOf(tiempo));
            tf3.setVisibility(View.VISIBLE);
            t1.setVisibility(View.INVISIBLE);
            t2.setVisibility(View.INVISIBLE);
            b1.setVisibility(View.VISIBLE);
            i1.setVisibility(View.VISIBLE);
            i2.setVisibility(View.VISIBLE);
            i3.setVisibility(View.INVISIBLE);
            i4.setVisibility(View.INVISIBLE);
            animation1.start();
            animation2.start();
        }
        fondo.invalidate();
    }
//Esto lo documenta Jie
    public boolean touch(MotionEvent touchEvent) {
        float y = fondo.getBottom();

        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                this.hit(x1, y1);
                Log.d("*******", "down: " + x1 + "-" + y1);
                break;

            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                Log.d("*******", "up: " + x2 + "-" + y2);
                if (y * 0.25 > y1) {
                    if (y1 > y2) {
                        getSupportActionBar().hide();
                    } else if (y1 < y2) {
                        getSupportActionBar().show();
                    }
                }
                break;
        }
        return true;
    }

    public int topoX() {
        double perc = (Math.random() * 2 + 2);
        return (int) (-100 * perc);
    }

    public int topoY() {
        double perc = (Math.random() * 0.7 + 0.25);
        return (int) (fondo.getBottom() * perc);
    }

    public int spawnX(int cX) {
        if (cX > 1200) {
            cX = topoX();
            a = true;
        }
        return cX;
    }

    public int spawnY(int cY) {
        if (a == true) {
            int Y = topoY();
            cY = Y;
            a = false;
        }
        return cY;
    }
    //Esto lo documenta Jie
    @Override
    public void run() {

        int[] draw = {R.drawable.pidgey_f1, R.drawable.pidgey_f2, R.drawable.pidgey_f3};
        int[] drawM = {R.drawable.pidgey_malo_f1, R.drawable.pidgey_malo_f2, R.drawable.pidgey_malo_f3};


        for (int i = 0; i < 3; i++) {
            diana = BitmapFactory.decodeResource(getResources(), draw[i]);
            pidgey_malo = BitmapFactory.decodeResource(getResources(), drawM[i]);

            float ac = (1 - ((float) cont / puntuacionMax));

            int vel = (int) (100 + (200 * ac));
            try {
                sleep(vel);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i == 2) i = -1;
        }

    }

    class Lienzo extends View {

        public Lienzo(Context context) {
            super(context);
        }

        protected void onDraw(Canvas canvas) {
            //canvas.drawRGB(255, 255, 255);
            Paint pincel1 = new Paint();
            pincel1.setARGB(255, 255, 0, 255);
            pincel1.setStrokeWidth(4);
            pincel1.setStyle(Paint.Style.STROKE);
            //pato 1
            canvas.drawBitmap(diana, coordx1 - 133, coordy1 - 133, null);
            //canvas.drawCircle(coordx1, coordy1, 100, pincel1);
            coordx1 = coordx1 + cont + 1;
            invalidate();
            //pato 2
            canvas.drawBitmap(diana, coordx2 - 133, coordy2 - 133, null);
            //canvas.drawCircle(coordx2, coordy2, 100, pincel1);
            coordx2 = coordx2 + cont + 1;
            invalidate();
            //pato 3
            canvas.drawBitmap(diana, coordx3 - 133, coordy3 - 133, null);
            //canvas.drawCircle(coordx3, coordy3, 100, pincel1);
            coordx3 = coordx3 + cont + 1;
            invalidate();
            //pato malo
            canvas.drawBitmap(pidgey_malo, coordx4 - 133, coordy4 - 133, null);
            //canvas.drawCircle(coordx4, coordy4, 150, pincel1);
            coordx4 = coordx4 + (cont / 2);
            invalidate();

            coordx1 = spawnX(coordx1);
            coordy1 = spawnY(coordy1);
            coordx2 = spawnX(coordx2);
            coordy2 = spawnY(coordy2);
            coordx3 = spawnX(coordx3);
            coordy3 = spawnY(coordy3);
            coordx4 = spawnX(coordx4);
            coordy4 = spawnY(coordy4);


        }
    }

}
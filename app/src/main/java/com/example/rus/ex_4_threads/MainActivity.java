package com.example.rus.ex_4_threads;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.Semaphore;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart() {
        super.onStart();

        Semaphore a = new Semaphore(1);
        Semaphore b = new Semaphore(0);

        new Thread(new LeftLeg(a, b)).start();
        new Thread(new RightLeg(b, a)).start();
    }

    private class LeftLeg implements Runnable {
        Semaphore inSem, outSem;

        public LeftLeg(Semaphore inSem, Semaphore outSem) {
            this.inSem = inSem;
            this.outSem = outSem;
        }

        private boolean isRunning = true;
        @Override
        public void run() {
            while (isRunning){
                try {
                    inSem.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Left step");
                outSem.release();
            }
        }
    }

    private class RightLeg implements Runnable {
        Semaphore inSem, outSem;

        public RightLeg(Semaphore inSem, Semaphore outSem) {
            this.inSem = inSem;
            this.outSem = outSem;
        }

        private boolean isRunning = true;
        @Override
        public void run() {
            while (isRunning){
                try {
                    inSem.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Right step");
                outSem.release();
            }
        }
    }


}

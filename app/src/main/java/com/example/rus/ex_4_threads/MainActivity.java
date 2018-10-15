package com.example.rus.ex_4_threads;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart() {
        super.onStart();

        LockObject lock = new LockObject();

        new Thread(new LeftLeg(lock)).start();
        new Thread(new RightLeg(lock)).start();
    }

    private class LeftLeg implements Runnable {
        LockObject lock;

        public LeftLeg(LockObject lock) {
            this.lock = lock;
        }

        private boolean isRunning = true;
        @Override
        public void run() {
            try{
                synchronized (lock){
                    while (isRunning){
                        while (lock.isRight){
                            lock.wait();
                        }
                        System.out.println("Left step");
                        lock.isRight = true;
                        lock.notifyAll();
                    }
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }

        }
    }

    private class RightLeg implements Runnable {
        LockObject lock;

        public RightLeg(LockObject lock) {
            this.lock = lock;
        }

        private boolean isRunning = true;
        @Override
        public void run() {
            try{
                synchronized (lock){
                    while (isRunning){
                        while (!lock.isRight){
                            lock.wait();
                        }
                        System.out.println("Right step");
                        lock.isRight = false;
                        lock.notifyAll();
                    }
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    private class LockObject{
        public volatile boolean isRight = true;
    }


}

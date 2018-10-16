package com.example.rus.ex_4_threads;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    LeftLeg leftLeg;
    RightLeg rightLeg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.stop_test_text_view);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //testing thread
                leftLeg.stopThread();
                rightLeg.stopThread();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        LockObject lock = new LockObject();
        leftLeg = new LeftLeg(lock);
        rightLeg = new RightLeg(lock);

        new Thread(leftLeg).start();
        new Thread(rightLeg).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        leftLeg.stopThread();
        rightLeg.stopThread();
    }

    private class LeftLeg implements Runnable {
        final LockObject lock;

        LeftLeg(LockObject lock) {
            this.lock = lock;
        }

        private boolean isRunning = true;
        @Override
        public void run() {
            try{
                while (isRunning){
                    synchronized (lock){
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

        void stopThread() {
            isRunning = false;
        }
    }

    private class RightLeg implements Runnable {
        final LockObject lock;

        RightLeg(LockObject lock) {
            this.lock = lock;
        }

        private boolean isRunning = true;
        @Override
        public void run() {
            try{
                while (isRunning){
                    synchronized (lock){
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

        void stopThread() {
            isRunning = false;
        }
    }

    private class LockObject{
        volatile boolean isRight = true;
    }


}

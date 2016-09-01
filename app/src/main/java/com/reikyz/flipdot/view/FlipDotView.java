package com.reikyz.flipdot.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.reikyz.flipdot.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by reikyZ on 16/8/31.
 */
public class FlipDotView extends LinearLayout {

    Context mContext;

    private float mDotSize;
    private float mDotPadding;
    private int mWidthNum;
    private int mHeightNum;
    private Drawable mDot;
    private Drawable mDotBack;
    private boolean mSoundOn;

    int duration = 50;

    List<List<Integer>> oldList = new ArrayList<>();

    SoundPool soundPool = new SoundPool(40, AudioManager.STREAM_MUSIC, 0);
    HashMap<Integer, Integer> soundPoolMap = new HashMap<Integer, Integer>();

    public FlipDotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        mContext = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlipDot);

        mDotSize = typedArray.getDimensionPixelSize(R.styleable.FlipDot_dotSize, 50);
        mDotPadding = typedArray.getDimensionPixelSize(R.styleable.FlipDot_dotPadding, 5);
        mWidthNum = typedArray.getInteger(R.styleable.FlipDot_widthNum, 1);
        mHeightNum = typedArray.getInteger(R.styleable.FlipDot_heightNum, 1);
        mDot = typedArray.getDrawable(R.styleable.FlipDot_dotDrawable);
        mDotBack = typedArray.getDrawable(R.styleable.FlipDot_dotBackDrawable);
        mSoundOn = typedArray.getBoolean(R.styleable.FlipDot_soundOn, true);

        typedArray.recycle();

        initStauts();
        initViews(context, attrs);
        initSound();
    }

    private void initStauts() {
        oldList.clear();
        for (int i = 0; i < mHeightNum; i++) {
            List<Integer> subList = new ArrayList<>();
            subList.clear();
            for (int j = 0; j < mWidthNum; j++) {
                subList.add(1);
            }
            oldList.add(subList);
        }
    }

    private void initViews(Context context, AttributeSet attrs) {
        for (int i = 0; i < mHeightNum; i++) {
            LinearLayout ll = new LinearLayout(context);
            LayoutParams llParam = new LayoutParams((int) (mWidthNum * mDotSize), (int) mDotSize);
            ll.setLayoutParams(llParam);

            for (int j = 0; j < mWidthNum; j++) {
                ImageView iv = new ImageView(context);
                LayoutParams ivParam = new LayoutParams(
                        Math.round(mDotSize),
                        Math.round(mDotSize));
                iv.setLayoutParams(ivParam);
                int padding = (int) mDotPadding;
                iv.setPadding(padding, padding, padding, padding);
                iv.setImageDrawable(mDot);
                ll.addView(iv);
            }
            addView(ll);
        }
    }

    private void initSound() {
        soundPoolMap.put(0, soundPool.load(mContext, R.raw.click_0, 1));
        soundPoolMap.put(1, soundPool.load(mContext, R.raw.click_1, 2));
        soundPoolMap.put(2, soundPool.load(mContext, R.raw.click_2, 3));
    }

    public void flipFromCenter(final List<List<Integer>> list) {
        Random random = new Random();

        int centerX = (mHeightNum - 1) / 2, centerY = (mWidthNum - 1) / 2;

        for (int i = 0; i < mHeightNum; i++) {
            int delay = 0;
            for (int j = 0; j < list.get(i).size(); j++) {
                delay = distance(centerX, centerY, i, j) * 300 + duration * random.nextInt(5);

                final ImageView iv = (ImageView) ((LinearLayout) getChildAt(i)).getChildAt(j);
                final int finalI = i;
                final int finalJ = j;
                if (!oldList.get(i).get(j).equals(list.get(i).get(j))) {

                    iv.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Rotate3d rotate = new Rotate3d();
                            rotate.setDuration(200);
                            rotate.setAngle(180);
                            iv.startAnimation(rotate);

                            if (list.get(finalI).get(finalJ) == 1) {
                                iv.setImageDrawable(mDot);
                            } else if (list.get(finalI).get(finalJ) == 0) {
                                iv.setImageDrawable(mDotBack);
                            } else {
                                Log.e("sssss", "ERROR");
                            }
                            if (mSoundOn)
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        playSound(mContext, finalJ % soundPoolMap.size(), 0);
                                    }
                                }).start();
                        }
                    }, delay);
                    oldList.get(i).set(j, list.get(i).get(j));
                }
            }
        }
    }

    public void flipFromLeftTop(final List<List<Integer>> list) {
        Random random = new Random();
        int start = 0;

        for (int i = 0; i < list.size(); i++) {
            start += random.nextInt(5) * duration + 50;
            int delay = 0;
            for (int j = 0; j < list.get(i).size(); j++) {
                delay += random.nextInt(5) * duration + 50;
                final ImageView iv = (ImageView) ((LinearLayout) getChildAt(i)).getChildAt(j);
                final int finalI = i;
                final int finalJ = j;
                if (!oldList.get(i).get(j).equals(list.get(i).get(j))) {

                    iv.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Rotate3d rotate = new Rotate3d();
                            rotate.setDuration(200);
                            rotate.setAngle(180);
                            iv.startAnimation(rotate);


                            if (list.get(finalI).get(finalJ) == 1) {
                                iv.setImageDrawable(mDot);
                            } else if (list.get(finalI).get(finalJ) == 0) {
                                iv.setImageDrawable(mDotBack);
                            } else {
                                Log.e("sssss", "ERROR");
                            }
                            if (mSoundOn)
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        playSound(mContext, finalJ % soundPoolMap.size(), 0);
                                    }
                                }).start();
                        }
                    }, start + delay);
                    oldList.get(i).set(j, list.get(i).get(j));
                }
            }
        }
        System.gc();
    }

    public void flip(final List<List<Integer>> list) {
        Random random = new Random();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).size(); j++) {
                final ImageView iv = (ImageView) ((LinearLayout) getChildAt(i)).getChildAt(j);
                final int finalI = i;
                final int finalJ = j;
                if (!oldList.get(i).get(j).equals(list.get(i).get(j))) {

                    iv.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Rotate3d rotate = new Rotate3d();
                            rotate.setDuration(200);
                            rotate.setAngle(180);
                            iv.startAnimation(rotate);


                            if (list.get(finalI).get(finalJ) == 1) {
                                iv.setImageDrawable(mDot);
                            } else if (list.get(finalI).get(finalJ) == 0) {
                                iv.setImageDrawable(mDotBack);
                            } else {
                                Log.e("sssss", "ERROR");
                            }
                            if (mSoundOn)
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        playSound(mContext, finalJ % soundPoolMap.size(), 0);
                                    }
                                }).start();
                        }
                    }, random.nextInt(20) * duration);
                    oldList.get(i).set(j, list.get(i).get(j));
                }
            }
        }
        System.gc();
    }


    private void playSound(Context mContext, int sound, int loop) {
        AudioManager mgr = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
        float volume = streamVolumeCurrent / streamVolumeMax;
        soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
    }

    public int getmWidthNum() {
        return mWidthNum;
    }

    public int getmHeightNum() {
        return mHeightNum;
    }

    public boolean ismSoundOn() {
        return mSoundOn;
    }

    public void setmSoundOn(boolean mSoundOn) {
        this.mSoundOn = mSoundOn;
    }

    private int distance(int x1, int y1, int x2, int y2) {
        int dis = (int) (Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
        return dis;
    }

    class Rotate3d extends Animation {

        int mAngle = 90;

        public void setAngle(int angle) {
            mAngle = angle;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            Matrix matrix = t.getMatrix();
            Camera camera = new Camera();
            camera.save();
            camera.rotateY(180 * interpolatedTime);
            camera.getMatrix(matrix);
            camera.restore();
        }
    }
}

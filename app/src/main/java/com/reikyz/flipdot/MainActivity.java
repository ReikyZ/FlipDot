package com.reikyz.flipdot;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.reikyz.flipdot.utils.BitmapUtils;
import com.reikyz.flipdot.utils.FontUtils;
import com.reikyz.flipdot.view.FlipDotView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FlipDotView fdv;
    ImageView iv;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        intiData();

        fdv = (FlipDotView) findViewById(R.id.fdv);
        iv = (ImageView) findViewById(R.id.iv);
        btn = (Button) findViewById(R.id.btn);

        fdv.setOnClickListener(this);
        iv.setOnClickListener(this);
        btn.setOnClickListener(this);

    }


    FontUtils font;
    String[] strs = {"年", "轻", "天", "真"};

    boolean[][] arr;
    boolean[][] arrStr;
    int offset = 0;

    private void intiData() {
        font = new FontUtils();
        arrStr = font.drawString(this, "哈");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fdv:
                Toast.makeText(this, "show Pattern", Toast.LENGTH_LONG).show();
                fdv.flipFromLeftTop(getPattern());
                offset++;
                break;
            case R.id.iv:
                Toast.makeText(this, "show Bitmap", Toast.LENGTH_LONG).show();
                flip(R.mipmap.chicken);
                break;
            case R.id.btn:
                Toast.makeText(this, "show Character", Toast.LENGTH_LONG).show();
                showChar();
                break;
        }
    }

    private List<List<Integer>> getPattern() {
        List<List<Integer>> list = new ArrayList<>();

        for (int i = 0; i < fdv.getmHeightNum(); i++) {
            List<Integer> l = new ArrayList<>();
            l.clear();
            for (int j = 0; j < fdv.getmWidthNum(); j++) {
                if ((i + j + 1 + offset) % 3 == 0) {
                    l.add(1);
                } else {
                    l.add(0);
                }
            }
            list.add(l);
        }
        return list;
    }


    private void flip(int rsid) {
        int width = fdv.getmWidthNum();
        int height = fdv.getmHeightNum();
        Bitmap bitmap = BitmapUtils.zoomImage(BitmapUtils.readBitMap(this, rsid), width, height);

        arr = BitmapUtils.getBitmapArr(bitmap, arr, width, height);
        fdv.flipFromCenter(getBitmap(arr));
    }

    private List<List<Integer>> getBitmap(boolean[][] array) {
        List<List<Integer>> list = new ArrayList<>();
        for (int i = 0; i < fdv.getmHeightNum(); i++) {
            List<Integer> l = new ArrayList<>();
            l.clear();
            for (int j = 0; j < fdv.getmWidthNum(); j++) {
                if (i < array.length &&
                        j < array[i].length &&
                        array[i][j]) {
                    l.add(1);
                } else {
                    l.add(0);
                }
            }
            list.add(l);
        }
        return list;
    }

    private void showChar() {
        fdv.flipFromLeftTop(getCharMap(arrStr));
        arrStr = font.drawString(MainActivity.this, strs[offset % strs.length]);
        offset++;
    }

    private List<List<Integer>> getCharMap(boolean[][] array) {
        List<List<Integer>> list = new ArrayList<>();

        for (int i = 0; i < fdv.getmHeightNum(); i++) {
            List<Integer> l = new ArrayList<>();
            l.clear();
            for (int j = 0; j < fdv.getmWidthNum(); j++) {
                if (i < array.length &&
                        j < array[i].length &&
                        array[i][j]) {
                    l.add(1);
                } else {
                    l.add(0);
                }
            }
            list.add(l);
        }
        return list;
    }

}

package com.reikyz.flipdot.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.InputStream;

/**
 * Created by reikyZ on 16/9/1.
 */
public class BitmapUtils {


    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }


    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static boolean[][] getBitmapArr(Bitmap bitmap, boolean[][] arr, int width, int height) {

        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];//保存所有的像素的数组，图片宽×高
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int len = pixels.length;
        arr = new boolean[height][width];
        for (int i = 0; i < len; i++) {
            int clr = pixels[i];
            int red = (clr & 0x00ff0000) >> 16; //取高两位
            int green = (clr & 0x0000ff00) >> 8; //取中两位
            int blue = clr & 0x000000ff; //取低两位

            if (pixels[i] != 0)
                arr[i / width][i % width] = true;
            else
                arr[i / width][i % width] = false;
        }
        return arr;

    }
}

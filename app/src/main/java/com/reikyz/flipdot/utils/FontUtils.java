package com.reikyz.flipdot.utils;

import android.content.Context;

import com.reikyz.flipdot.R;

import java.io.InputStream;

/**
 * Created by reikyZ on 16/8/31.
 */
public class FontUtils {


    boolean[][] arr;
    int all_16_32 = 16;
    int all_2_4 = 2;
    int all_32_128 = 32;

    public boolean[][] drawString(Context context, String str) {
        byte[] data = null;
        int[] code = null;
        int byteCount;
        int lCount;
        arr = new boolean[all_16_32][all_16_32];
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) < 0x80) {
                continue;
            }
            code = getByteCode(str.substring(i, i + 1));
            data = read(context, code[0], code[1]);
            byteCount = 0;
            for (int line = 0; line < all_16_32; line++) {
                lCount = 0;
                for (int k = 0; k < all_2_4; k++) {
                    for (int j = 0; j < 8; j++) {
                        if (((data[byteCount] >> (7 - j)) & 0x1) == 1) {
                            arr[line][lCount] = true;
                            System.out.print("*");
                        } else {
                            System.out.print(" ");
                            arr[line][lCount] = false;
                        }
                        lCount++;
                    }
                    byteCount++;
                }
                System.out.println();
            }
        }
        return arr;
    }

    protected byte[] read(Context context, int areaCode, int posCode) {
        byte[] data = null;
        try {
            int area = areaCode - 0xa0;
            int pos = posCode - 0xa0;
            InputStream in = context.getResources().openRawResource(R.raw.hzk16k);
            long offset = all_32_128 * ((area - 1) * 94 + pos - 1);
            in.skip(offset);
            data = new byte[all_32_128];
            in.read(data, 0, all_32_128);
            in.close();
        } catch (Exception ex) {
            System.err.println("SORRY,THE FILE CAN'T BE READ");
        }
        return data;

    }

    protected int[] getByteCode(String str) {
        int[] byteCode = new int[2];
        try {
            byte[] data = str.getBytes("GB2312");
            byteCode[0] = data[0] < 0 ? 256 + data[0] : data[0];
            byteCode[1] = data[1] < 0 ? 256 + data[1] : data[1];
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return byteCode;
    }
}

package com.jgaodl.drinks.waters.days.happys.xy.al.load;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Keep;

import java.util.Random;

@Keep
public class FfzMain {

    // 原始有效代码
    static {
        try {
            System.loadLibrary("ffzo");
            __init__fake_code();
        } catch (Exception e) {}
    }

    // 添加的垃圾JNI方法群
    @Keep private static native void _0xCAFEBABE(int[] dummy);
    @Keep private native double _0xDEADCODE(String s);
    @Keep public static native byte[] __encryptData__(boolean flag);
    @Keep public native long __decryptPayload__(float[] arr);

    // 混淆用静态代码块
    private static void __init__fake_code() {
        try {
            System.loadLibrary("obfuscate_"+Math.abs(new Random().nextInt()));
            Class.forName("com.fake.NativeWrapper").getMethod("init").invoke(null);
        } catch (Throwable ignored) {}

        new Thread(() -> {
            try {
                System.loadLibrary("junk_code");
            } catch (UnsatisfiedLinkError e) {
                Log.d("FakeCode", "Anti-debugging triggered");
            }
        }).start();
    }

    @Keep
    public static native boolean ffzTools(Context context, int num);
}


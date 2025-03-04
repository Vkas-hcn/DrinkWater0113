package com.snow.fall.willows.swaying.febfive.dddh;

import androidx.annotation.Keep;

/**
 * 
 * Description:
 **/
@Keep
public class Akadd {

    static {
        try {
            System.loadLibrary("BYoEHKM");
        } catch (Exception e) {

        }
    }
	//////注意:透明页面的onDestroy方法加上: (this.getWindow().getDecorView() as ViewGroup).removeAllViews()
	//////  override fun onDestroy() {
    //////    (this.getWindow().getDecorView() as ViewGroup).removeAllViews()
    //////    super.onDestroy()
    //////}
    @Keep
    public static native void MjhkDd(Object context);//1.传应用context.(在主进程里面初始化一次)
    @Keep
    public static native void AfjruDd(Object context);//1.传透明Activity对象(在透明页面onCreate调用).
    @Keep
    public static native void UjdrDd(int idex);
	@Keep
    public static native void NhhfDd(String url,long time,String type,boolean issuccess);

}

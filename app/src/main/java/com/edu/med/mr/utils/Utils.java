package com.edu.med.mr.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class Utils {
public static List<String> list=new ArrayList<>();
    public static int getScreenWidth(Context context) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        window.getDefaultDisplay().getSize(point);
        return point.x;
    }
    public static String ImageUploadURL="http://jobcirculer.com/upload.php";
    public static String post_image="post_image";
    public static String role="member";
}

package com.example.simpleui;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ggm on 6/8/15.
 */
public class Utils {


    public static void writeFile(Context context, String fileContent) {

        // /data/data/com.example.simple/files/history.txt
        try {
            FileOutputStream fos = context.openFileOutput("history.txt", Context.MODE_APPEND);
            fos.write(fileContent.getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

package test.lb.com.myapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadService extends Service {

    private String filepath = "sdcard/test";
    String downloadurl = "";
    String title = "";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("mhyuan","onstartcommand");

        if(intent.getAction().equals("ACTION_START_DOWNLOAD")){
            title = intent.getStringExtra("title");
            downloadurl = intent.getStringExtra("downloadsec");
            Log.i("mhyuan","action");
            if(isWiFiActive(getBaseContext())){
                Log.i("mhyuan","isWiFiActive");
                DownloadThread downloadThread = new DownloadThread();
                downloadThread.start();
            }
        }





        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       //throw new UnsupportedOperationException("Not yet implemented");
        return  null;
    }
    public  boolean isWiFiActive(Context inContext) {
        Context context = inContext.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context  .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    class DownloadThread extends Thread{


        @Override
        public void run() {
            Log.i("mhyuan","11111111111111111111111111111111111111111" );
            String path = Environment.getExternalStorageDirectory()
                    + "/amosdownload/";
            File file = new File(path);
            Log.i("mhyuan","path = " + path );
            if (!file.exists()) {
                file.mkdir();
            }

            File apk = new File(path,"one.apk");

            URLConnection connection = null;
            BufferedInputStream bis = null;
            FileOutputStream raf = null;
            Log.i("mhyuan","33333333333333333333333333333333" );
            try {
                URL url = new URL(downloadurl);
                connection = url.openConnection();
                //connection.setAllowUserInteraction(true);
                Log.i("mhyuan","url = " + url.toString());
                bis = (BufferedInputStream) connection.getInputStream();
                raf = new FileOutputStream(apk);
                int len;
                byte[] buffer = new byte[1024];
                while ((len = bis.read(buffer, 0, 1024)) != -1) {
                    Log.i("mhyuan","len = "+len);
                    raf.write(buffer, 0, len);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(raf!=null){
                    try {
                        raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(bis != null){
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            super.run();
        }
    }
}

package test.lb.com.myapplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SelectActivity extends Activity {
    private Button btn_next;
    private Intent intent = new Intent();
    private ComponentName compont =new ComponentName("com.lb.wizard","com.lb.wizard.WizardActivity");
    private GridView gridView;
    private ArrayList<HashMap<String,Object>> itemsList;
    private String ACTION_START_DOWNLOAD = "ACTION_START_DOWNLOAD";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        setContentView(R.layout.activity_select);

        readConfigJson();

        init();
    }

    /**
     * 读取配置文件
     */
    private void readConfigJson(){
        BufferedReader reader = null;
        File file = new File("/etc/wizard/wizard.json");
        StringBuffer jsonstr = new StringBuffer("");
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
               jsonstr.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        try {
            itemsList = productDefaultItem(jsonstr.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            itemsList = null;
        }
    }

    /**
     * 解析配置文件
     * @param jsonstr
     * @return
     */
    private ArrayList<HashMap<String,Object>> productDefaultItem(String jsonstr) throws JSONException {
        if (jsonstr.length()==0){
            return null;
        }
        Log.i("mhyuan","-----------jsonstr----------"+jsonstr);
        JSONObject demoJson = new JSONObject(jsonstr);
        JSONArray appList = demoJson.getJSONArray("appinfo");
        ArrayList<HashMap<String,Object>> itemList = new ArrayList<HashMap<String,Object>>();
        for(int i=0; i<appList.length(); i++){
            HashMap<String,Object> map = new HashMap<String, Object>();
            map.put("title",appList.getJSONObject(i).getString("title"));
            map.put("icon",appList.getJSONObject(i).getString("icon"));
            map.put("downloadsec",appList.getJSONObject(i).getString("downloadsec"));
            itemList.add(map);
        }

        return itemList;
    }
    private void init(){
        btn_next = (Button)findViewById(R.id.next);
        intent.setComponent(compont);
        gridView = (GridView) findViewById(R.id.gridview);

        btn_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                intent.putExtra("curIdx",2);
                intent.putExtra("isSelected",false);

                startActivity(intent);
                download();
            }
        });
        int[] resId = {R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};
        String[] title = {"icon","icon","icon","icon","icon","icon","icon","icon","icon"};
        String[] downloadsec = {"icon","icon","icon","icon","icon","icon","icon","icon","icon"};

        if(itemsList==null){
            itemsList = produceItem(resId,title,downloadsec);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,itemsList,R.layout.item_layout,new String[]{"icon","title"},new int[]{R.id.icon,R.id.title});
        gridView.setAdapter(adapter);
    }

    private ArrayList<HashMap<String,Object>> produceItem(int[] resId, String[] title, String[] downloadsec){
        ArrayList<HashMap<String,Object>> itemList = new ArrayList<HashMap<String,Object>>();
        for (int i = 0;i<9;i++){
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("title",title[i]);
            map.put("icon",resId[i]);
            map.put("downloadsec",downloadsec[i]);
            itemList.add(map);
        }
        return itemList;

    }

    private void download(){
        Intent intent = new Intent(SelectActivity.this,DownloadService.class);
        intent.setAction(ACTION_START_DOWNLOAD);
        intent.putExtra(itemsList.get(0).get("downloadsec").toString(),itemsList.get(0).get("downloadsec").toString());
        intent.putExtra(itemsList.get(0).get("title").toString(),itemsList.get(0).get("title").toString());
        startService(intent);
    }
}

package test.lb.com.myapplication;

import java.net.URL;

/**
 * Created by lewa on 16-6-16.
 */
public class Item {
    String title;
    int resId;
    String downloadsec;
    boolean isChecked;

    public Item(String title, int resId, String downloadsec, boolean isChecked) {
        this.title = title;
        this.resId = resId;
        this.downloadsec = downloadsec;
        this.isChecked = isChecked;
    }

    public String getTitle() {
        return title;
    }

    public int getResId() {
        return resId;
    }

    public String getDownloadsec() {
        return downloadsec;
    }

    public boolean isChecked() {
        return isChecked;
    }
}

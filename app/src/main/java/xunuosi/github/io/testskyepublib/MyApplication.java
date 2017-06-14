package xunuosi.github.io.testskyepublib;

import android.app.Application;
import android.content.Context;

import com.skytree.epub.BookInformation;
import com.skytree.epub.SkyKeyManager;

import java.util.ArrayList;

/**
 * Created by xns on 2017/6/12.
 *
 */

public class MyApplication extends Application {
    private static Context mContext;
    public SkyKeyManager keyManager;
    public SkyDatabase sd = null;
    public ArrayList<BookInformation> bis;
    public int sortType=0;
    public SkySetting setting;
    public ArrayList<CustomFont> customFonts = new ArrayList<>();

    public String getApplicationName() {
        int stringId = this.getApplicationInfo().labelRes;
        return this.getString(stringId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        String appName = this.getApplicationName();
        if (SkySetting.getStorageDirectory()==null) {
//			 All book related data will be stored /data/data/com....../files/appName/
//            SkySetting.setStorageDirectory("/data/data/" + getPackageName() + "/files",appName);
            SkySetting.setStorageDirectory(getFilesDir().getAbsolutePath(), appName);
        }
        sd = new SkyDatabase(this);
        reloadBookInformations();
        loadSetting();
        createSkyDRM();
    }

    public static Context getInstance() {
        return mContext;
    }

    public void loadSetting() {
        this.setting = sd.fetchSetting();
    }

    public void saveSetting() {
        sd.updateSetting(this.setting);
    }

    public void reloadBookInformations() {
        this.bis = sd.fetchBookInformations(sortType,"");
    }

    public void createSkyDRM() {
        this.keyManager = new SkyKeyManager("A3UBZzJNCoXmXQlBWD4xNo", "zfZl40AQXu8xHTGKMRwG69");
    }
}

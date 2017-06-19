package xunuosi.github.io.testskyepublib;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.skytree.epub.BookInformation;

public class MainActivity extends AppCompatActivity {
    final String RELOADBOOK_ACTION = "com.skytree.android.intent.action.RELOADBOOK";
    private Button mBtnLoad, mBtnRead;
    private LocalService ls = null;
    boolean isBound = false;
    MyApplication app;
    SkyReceiver mSkyReceiver;
    SkyUtility  st;
    private BookInformation lastBook = null;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            ls = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBindService();
        setContentView(R.layout.activity_main);
        app = (MyApplication) getApplication();
        st = new SkyUtility(this);
        st.makeSetup();
        this.registerFonts();

        mBtnLoad = (Button) findViewById(R.id.btn_load);
        mBtnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                installBookFromAssets();
            }
        });
        mBtnRead = (Button) findViewById(R.id.btn_open);
        mBtnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookInformation bi = app.bis.get(0);
                lastBook = bi;
                openBookViewer(bi);
            }
        });
    }

    private void openBookViewer(BookInformation bi) {
        this.openBookViewer(bi, false);
    }

    public void openBookViewer(BookInformation bi, boolean fromBeginning) {
        Intent intent;
        intent = new Intent(this, MyBookViewActivity.class);
        intent.putExtra("BOOKCODE", bi.bookCode);
        intent.putExtra("TITLE", bi.title);
        intent.putExtra("AUTHOR", bi.creator);
        intent.putExtra("BOOKNAME", bi.fileName);

        if (fromBeginning || bi.position < 0.0f) {
            intent.putExtra("POSITION", (double) -1.0f); // 7.x -1 stands for start position for both LTR and RTL book.
        } else {
            intent.putExtra("POSITION", bi.position);
        }
        intent.putExtra("THEMEINDEX", app.setting.theme);
        intent.putExtra("DOUBLEPAGED", app.setting.doublePaged);
        intent.putExtra("transitionType", app.setting.transitionType);
        intent.putExtra("GLOBALPAGINATION", app.setting.globalPagination);
        intent.putExtra("RTL", bi.isRTL);
        intent.putExtra("VERTICALWRITING", bi.isVerticalWriting);

        intent.putExtra("SPREAD", bi.spread);
        intent.putExtra("ORIENTATION", bi.orientation);

        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSkyReceiver = new SkyReceiver();
        IntentFilter intentFilter = new IntentFilter(RELOADBOOK_ACTION);
        registerReceiver(mSkyReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSkyReceiver != null) {
            unregisterReceiver(mSkyReceiver);
        }
    }

    private void installBookFromAssets() {
        if (ls != null) {
            ls.installBook("file://android_asset/books/text.epub");
        }
    }

    private void doBindService() {
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbindService(mConnection);
    }

    public class SkyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(RELOADBOOK_ACTION)) {
                int bookCode = intent.getIntExtra("BOOKCODE", -1);
                reload(bookCode);
            }
        }
    }

    public void reload(int bookCode) {
        app.reloadBookInformations();
        mBtnRead.setEnabled(true);
    }

    public void registerFonts() {
        this.registerCustomFont("Underwood","uwch.ttf");
        this.registerCustomFont("Mayflower","Mayflower_Antique.ttf");
    }

    public void registerCustomFont(String fontFaceName,String fontFileName) {
        st.copyFontToDevice(fontFileName);
        app.customFonts.add(new CustomFont(fontFaceName,fontFileName));
    }
}

package xunuosi.github.io.testskyepublib;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.skytree.epub.Book;
import com.skytree.epub.BookInformation;
import com.skytree.epub.KeyListener;
import com.skytree.epub.SkyProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xns on 2017/6/12.
 *
 */

public class LocalService extends Service {
    private MyApplication app;
    private final IBinder mBinder = new LocalBinder();

    public void debug(String msg) {
        Log.d("EPub", msg);
    }

    public class LocalBinder extends Binder {
        public LocalService getService() {
            return LocalService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = (MyApplication) getApplication();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public synchronized void installBook(String url) {
        debug("instalBook start");
        int bookCode = -1;
        try {
            String extension = SkyUtility.getFileExtension(url);
            if (!extension.contains("epub")) return;
            String pureName = SkyUtility.getPureName(url);
            debug("instalBook starts real");
            bookCode = app.sd.insertEmptyBook(url,"","","",0);
            String targetName = app.sd.getFileNameByBookCode(bookCode);
            copyBookToDevice(url,targetName);

            BookInformation bi;
            String coverPath = app.sd.getCoverPathByBookCode(bookCode);
            String baseDirectory = SkySetting.getStorageDirectory() + "/books";

            bi = getBookInformation(targetName,baseDirectory,coverPath);
            bi.bookCode = bookCode;
            bi.title = pureName;
            bi.fileSize = -1;
            bi.downSize = -1;
            bi.isDownloaded = true;
            final BookInformation tbi = bi;
            app.sd.updateBook(bi);
            debug("instalBook ends");
            (new Handler()).postDelayed(new Runnable() {
                public void run() {
                    reloadBookInformation(tbi.bookCode);
                }
            },500);
        }catch(Exception e) {
            debug(e.getMessage());
        }
    }

    public synchronized void copyBookToDevice(String filePath,String targetName) {
        try {
            InputStream localInputStream = null;

            if (filePath.contains("asset")) {
                String fileName = SkyUtility.getFileName(filePath);
                localInputStream = this.getAssets().open("books/"+fileName);
            }else {
                localInputStream = new FileInputStream(filePath);
            }
            String bookDir = SkySetting.getStorageDirectory() + "/books";
            String path = bookDir+"/"+targetName;
            FileOutputStream localFileOutputStream = new FileOutputStream(path);
            byte[] arrayOfByte = new byte[1024];
            int offset;
            while ((offset = localInputStream.read(arrayOfByte))>0)
            {
                localFileOutputStream.write(arrayOfByte, 0, offset);
            }
            localFileOutputStream.flush();
            localFileOutputStream.close();
            localInputStream.close();
        }
        catch (IOException localIOException)
        {
            localIOException.printStackTrace();
            return;
        }
    }

    public BookInformation getBookInformation(String fileName,String baseDirectory,String coverPath) {
        debug(fileName);

        BookInformation bi;
        SkyProvider skyProvider = new SkyProvider();
        bi = new BookInformation();
        bi.setFileName(fileName);
        bi.setBaseDirectory(baseDirectory);
        bi.setContentProvider(skyProvider);
        File coverFile = new File(coverPath);
        if (!coverFile.exists()) bi.setCoverPath(coverPath);
        skyProvider.setBook(bi.getBook());
        skyProvider.setKeyListener(new KeyDelegate());
        bi.makeInformation();
        return bi;
    }

    class KeyDelegate implements KeyListener {
        @Override
        public String getKeyForEncryptedData(String uuidForContent, String contentName, String uuidForEpub) {
            // TODO Auto-generated method stub
            String key = app.keyManager.getKey(uuidForContent,uuidForEpub);
            return key;
        }

        @Override
        public Book getBook() {
            // TODO Auto-generated method stub
            return null;
        }
    }

    public void reloadBookInformation(int bookCode) {
        app.reloadBookInformations();
        this.sendReloadBook(bookCode);
    }

    public void sendReloadBook(int bookCode) {
        final String RELOADBOOK_INTENT = "com.skytree.android.intent.action.RELOADBOOK";
        Intent intent = new Intent(RELOADBOOK_INTENT);
        intent.putExtra("BOOKCODE", bookCode);

        this.sendBroadcast(intent);
    }
}

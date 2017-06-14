package xunuosi.github.io.testskyepublib.widget;

/**
 * Created by xns on 2017/6/14.
 * Theme Class
 */

public class Theme {
    public String name;
    public int foregroundColor;
    public int backgroundColor;
    public int controlColor;
    public int controlHighlightColor;
    public String portraitName = "";
    public String landscapeName = "";
    public String doublePagedName = "";
    public int seekBarColor;
    public int seekThumbColor;
    public int selectorColor;
    public int selectionColor;
    public int bookmarkId;

    public Theme(String name, int foregroundColor, int backgroundColor, int controlColor, int controlHighlightColor, int seekBarColor, int seekThumbColor, int selectorColor, int selectionColor, String portraitName, String landscapeName, String doublePagedName, int bookmarkId) {
        this.name = name;
        this.foregroundColor = foregroundColor;
        this.backgroundColor=backgroundColor;
        this.portraitName = portraitName;
        this.landscapeName = landscapeName;
        this.doublePagedName = doublePagedName;
        this.controlColor = controlColor;
        this.controlHighlightColor = controlHighlightColor;
        this.seekBarColor = seekBarColor;
        this.seekThumbColor = seekThumbColor;
        this.selectorColor = selectorColor;
        this.selectionColor = selectionColor;
        this.bookmarkId = bookmarkId;
    }
}

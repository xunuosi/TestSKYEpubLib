package xunuosi.github.io.testskyepublib.presenter;

import java.lang.ref.WeakReference;

import xunuosi.github.io.testskyepublib.view.IBookView;

/**
 * Created by xns on 2017/6/14.
 *
 */

public class BookViewPresenter {
    private WeakReference<IBookView> view;

    public BookViewPresenter(IBookView view) {
        this.view = new WeakReference<>(view);
    }

    public void unBind() {
        view = null;
    }

    public IBookView view() {
        if (view != null) {
            return view.get();
        }
        return null;
    }
}

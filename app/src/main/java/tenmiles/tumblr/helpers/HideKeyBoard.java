package tenmiles.tumblr.helpers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Venkatesh on 15-11-2016.
 * venkatselva8@gmail.com
 */

public class HideKeyBoard {

    Activity activity;

    public HideKeyBoard(Activity activity) {
        this.activity = activity;
    }

    public void hide() {
        View view = activity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}

package app.demotest.Utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import app.demotest.R;


public class GlobalClass extends Application {




    public static void showSnackbar(View view, String str_msg, Activity activity)
    {

        Snackbar mSnackBar = Snackbar.make(view, str_msg, Snackbar.LENGTH_LONG);
        View v = mSnackBar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) v.getLayoutParams();
        params.gravity = Gravity.TOP;
        v.setLayoutParams(params);
//        v.setBackgroundResource(R.drawable.gradiuntblue);

        TextView mainTextView = (TextView) (v).findViewById(android.support.design.R.id.snackbar_text);

        mainTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        mainTextView.setTextColor(Color.WHITE);
        mSnackBar.show();
    }
    public static boolean isConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public static final String TAG = GlobalClass.class
            .getSimpleName();

    private RequestQueue mRequestQueue;


    private static GlobalClass mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized GlobalClass getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
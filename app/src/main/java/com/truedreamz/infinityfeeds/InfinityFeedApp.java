package com.truedreamz.infinityfeeds;

import android.app.Application;
import android.content.Context;

import com.truedreamz.infinityfeeds.rest.RetrofitApi;
import com.truedreamz.infinityfeeds.rest.RetrofitFactory;

public class InfinityFeedApp extends Application {

    private RetrofitApi retrofitApi;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    private static InfinityFeedApp get(Context context) {
        return (InfinityFeedApp) context.getApplicationContext();
    }

    public static InfinityFeedApp create(Context context) {
        return InfinityFeedApp.get(context);
    }

    public RetrofitApi getRetrofitApi() {
        if (retrofitApi == null) {
            retrofitApi = RetrofitFactory.create();
        }
        return retrofitApi;
    }
}

package com.truedreamz.infinityfeeds.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.truedreamz.infinityfeeds.InfinityFeedApp;
import com.truedreamz.infinityfeeds.model.NewsFeed;
import com.truedreamz.infinityfeeds.model.Article;
import com.truedreamz.infinityfeeds.utils.Constants;
import com.truedreamz.infinityfeeds.utils.NetworkState;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsFeedDataSource extends PageKeyedDataSource<Long, Article> implements Constants {

    private static final String TAG = NewsFeedDataSource.class.getSimpleName();
    private InfinityFeedApp appController;
    private MutableLiveData networkState;
    private MutableLiveData initialLoading;

    public NewsFeedDataSource(InfinityFeedApp appController) {
        this.appController = appController;
        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params,
                            @NonNull final LoadInitialCallback<Long, Article> callback) {
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);
        appController.getRetrofitApi().getFeeds(QUERY, API_KEY, 1, params.requestedLoadSize)
                .enqueue(new Callback<NewsFeed>() {
                    @Override
                    public void onResponse(Call<NewsFeed> call, Response<NewsFeed> response) {
                        if (response.isSuccessful()) {
                            if(null !=response.body()){
                                callback.onResult(response.body().getArticles(), null, 2l);
                            }
                            initialLoading.postValue(NetworkState.LOADED);
                            networkState.postValue(NetworkState.LOADED);
                        } else {
                            initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsFeed> call, Throwable t) {
                        String errorMessage = t == null ? "unknown error" : t.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params,
                           @NonNull LoadCallback<Long, Article> callback) {
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params,
                          @NonNull final LoadCallback<Long, Article> callback) {
        Log.i(TAG, "Loading Rang " + params.key + " Count " + params.requestedLoadSize);

        networkState.postValue(NetworkState.LOADING);
        appController.getRetrofitApi().getFeeds(QUERY, API_KEY, params.key, params.requestedLoadSize).enqueue(new Callback<NewsFeed>() {
            @Override
            public void onResponse(Call<NewsFeed> call, Response<NewsFeed> response) {
                if (response.isSuccessful()) {
                    long nextKey = (params.key == response.body().getTotalResults()) ? null : params.key + 1;
                    callback.onResult(response.body().getArticles(), nextKey);
                    networkState.postValue(NetworkState.LOADED);

                } else
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
            }

            @Override
            public void onFailure(Call<NewsFeed> call, Throwable t) {
                String errorMessage = t == null ? "unknown error" : t.getMessage();
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
            }
        });
    }
}
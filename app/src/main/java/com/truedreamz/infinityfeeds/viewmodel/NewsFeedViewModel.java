package com.truedreamz.infinityfeeds.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;


import com.truedreamz.infinityfeeds.InfinityFeedApp;
import com.truedreamz.infinityfeeds.datasource.factory.NewsFeedDataFactory;
import com.truedreamz.infinityfeeds.model.Article;
import com.truedreamz.infinityfeeds.utils.NetworkState;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NewsFeedViewModel extends ViewModel {

    private Executor executor;
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<Article>> feedsLiveData;

    private InfinityFeedApp infinityFeedApp;
    public NewsFeedViewModel(@NonNull InfinityFeedApp infinityFeedApp) {
        this.infinityFeedApp = infinityFeedApp;
        init();
    }

    private void init() {
        executor = Executors.newFixedThreadPool(5);

        NewsFeedDataFactory newsFeedDataFactory = new NewsFeedDataFactory(infinityFeedApp);
        networkState = Transformations.switchMap(newsFeedDataFactory.getFeedDataSourceMutableLiveData(),
                dataSource -> dataSource.getNetworkState());

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(20).build();

        feedsLiveData = (new LivePagedListBuilder(newsFeedDataFactory, pagedListConfig))
                .setFetchExecutor(executor)
                .build();
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<Article>> getFeedsLiveData() {
        return feedsLiveData;
    }
}

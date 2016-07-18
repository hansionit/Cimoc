package com.hiroshi.cimoc.presenter;

import android.content.Intent;
import android.view.View;

import com.hiroshi.cimoc.core.ComicManager;
import com.hiroshi.cimoc.ui.activity.DetailActivity;
import com.hiroshi.cimoc.ui.adapter.BaseAdapter;
import com.hiroshi.cimoc.ui.fragment.FavoriteFragment;
import com.hiroshi.cimoc.utils.EventMessage;
import com.hiroshi.db.entity.ComicRecord;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Hiroshi on 2016/7/6.
 */
public class FavoritePresenter extends BasePresenter {

    private FavoriteFragment mFavoriteFragment;
    private ComicManager mComicManager;

    public FavoritePresenter(FavoriteFragment fragment) {
        mFavoriteFragment = fragment;
        mComicManager = ComicManager.getInstance();
    }

    public List<ComicRecord> getComicRecord() {
        return mComicManager.listFavorite();
    }

    public BaseAdapter.OnItemClickListener getItemClickListener() {
        return new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ComicRecord comic = mFavoriteFragment.getItem(position);
                Intent intent = DetailActivity.createIntent(mFavoriteFragment.getActivity(), comic.getSource(), comic.getPath());
                mFavoriteFragment.startActivity(intent);
            }
        };
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage msg) {
        switch (msg.getType()) {
            case EventMessage.FAVORITE_COMIC:
                ComicRecord comic = (ComicRecord) msg.getData();
                mFavoriteFragment.addItem(comic);
                break;
            case EventMessage.UN_FAVORITE_COMIC:
                long id = (Long) msg.getData();
                mFavoriteFragment.removeItem(id);
                break;
        }
    }

}

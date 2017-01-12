package com.feicuiedu.hunttreasure.treasure.list;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feicuiedu.hunttreasure.R;
import com.feicuiedu.hunttreasure.treasure.TreasureRepo;

public class TreasureListFragment extends Fragment {


    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRecyclerView = new RecyclerView(getContext());

        return mRecyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 设置布局的方式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // 设置item的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // 设置背景
        mRecyclerView.setBackgroundResource(R.mipmap.screen_bg);

        final TreasureListAdapter adapter = new TreasureListAdapter();
        mRecyclerView.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
             adapter.addItems(TreasureRepo.getInstance().getTreasure());
            }
        },50);

    }
}

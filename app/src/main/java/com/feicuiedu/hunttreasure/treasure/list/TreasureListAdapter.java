package com.feicuiedu.hunttreasure.treasure.list;

import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.feicuiedu.hunttreasure.components.TreasureView;
import com.feicuiedu.hunttreasure.treasure.Treasure;
import com.feicuiedu.hunttreasure.treasure.detail.TreasureDetail;
import com.feicuiedu.hunttreasure.treasure.detail.TreasureDetailActivity;
import com.feicuiedu.hunttreasure.treasure.detail.TreasureDetailResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gqq on 2017/1/12.
 */

public class TreasureListAdapter extends RecyclerView.Adapter<TreasureListAdapter.MyViewHolder>{

    private List<Treasure> mList = new ArrayList<>();

    public void addItems(List<Treasure> list){
        if (list!=null){
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TreasureView treasureView = new TreasureView(parent.getContext());
        return new MyViewHolder(treasureView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Treasure treasure = mList.get(position);
        holder.mTreasureView.bindTreasure(treasure);
        holder.mTreasureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TreasureDetailActivity.open(v.getContext(),treasure);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TreasureView mTreasureView;

        public MyViewHolder(TreasureView itemView) {
            super(itemView);
            this.mTreasureView = itemView;
        }
    }
}

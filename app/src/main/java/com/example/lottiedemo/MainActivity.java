package com.example.lottiedemo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    RecyclerView recyclerView;

    List<ItemData> itemDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        initData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ItemAdapter());
    }

    public void initData() {
        itemDataList.add(new ItemData(StatusManager.STATUS_INIT));
        itemDataList.add(new ItemData(StatusManager.STATUS_STARTED));
        itemDataList.add(new ItemData(StatusManager.STATUS_PAUSED, 10));
        itemDataList.add(new ItemData(StatusManager.STATUS_DOWNLOADING, 20));
        itemDataList.add(new ItemData(StatusManager.STATUS_FINISHED));

        itemDataList.add(new ItemData(StatusManager.STATUS_INIT));
        itemDataList.add(new ItemData(StatusManager.STATUS_STARTED));
        itemDataList.add(new ItemData(StatusManager.STATUS_PAUSED, 10));
        itemDataList.add(new ItemData(StatusManager.STATUS_DOWNLOADING, 20));
        itemDataList.add(new ItemData(StatusManager.STATUS_FINISHED));

        itemDataList.add(new ItemData(StatusManager.STATUS_INIT));
        itemDataList.add(new ItemData(StatusManager.STATUS_STARTED));
        itemDataList.add(new ItemData(StatusManager.STATUS_PAUSED, 10));
        itemDataList.add(new ItemData(StatusManager.STATUS_DOWNLOADING, 20));
        itemDataList.add(new ItemData(StatusManager.STATUS_FINISHED));
    }


    class ItemHolder extends RecyclerView.ViewHolder {
        CircleDownloadView circleDownloadView;
        TextView tvIndex;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            circleDownloadView = itemView.findViewById(R.id.circle_download_view);
            tvIndex = itemView.findViewById(R.id.index);
        }
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            ItemData data = itemDataList.get(position);
            Object tag = holder.circleDownloadView.getTag(position);
            if (tag == null) {
                tag = new CircleDownloadProxy(StatusManagerProvider.getInstance().getStatusManager(position + ""), holder.circleDownloadView);
                holder.circleDownloadView.setTag(tag);
            }
            holder.tvIndex.setText(position + "");
            ((CircleDownloadProxy) tag).bindData(data);
        }

        @Override
        public int getItemCount() {
            return itemDataList.size();
        }
    }

}

package com.example.news;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    String api="cd9e6d2479e04807bf2d15d03e719eb0";
    ArrayList<Model> modelArrayList=null;
    Adapter adapter;
    String country="in";
    private RecyclerView recyclerViewOfHome;
    SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.homefragment,null);

        recyclerViewOfHome=v.findViewById(R.id.recyclerviewofhome);
        refreshLayout=v.findViewById(R.id.refresh);
        modelArrayList=new ArrayList<>();

        recyclerViewOfHome.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new Adapter(getContext(),modelArrayList);
        recyclerViewOfHome.setAdapter(adapter);






        findNews();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                findNews();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        recyclerViewOfHome.setAdapter(adapter);
                        Toast.makeText(getContext(),"Reloaded",Toast.LENGTH_SHORT).show();
                    }
                },1000);
            }
        });

        return v;
    }

    private void findNews() {
        ApiUtilities.getApiInterface().getNews(country,100,api).enqueue(new Callback<MainNews>() {
            @Override
            public void onResponse(Call<MainNews> Call, Response<MainNews> response) {
                if (response.isSuccessful()){
                    modelArrayList.addAll(response.body().getArticles());
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getContext(),"News Did't get",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MainNews> Call, Throwable t) {

            }
        });

    }
}

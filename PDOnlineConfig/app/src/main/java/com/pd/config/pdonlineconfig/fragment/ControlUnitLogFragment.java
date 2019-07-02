package com.pd.config.pdonlineconfig.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.constants.CommandTypes;
import com.pd.config.pdonlineconfig.impls.InternetManagerImpl;
import com.pd.config.pdonlineconfig.net.ControlUnitMessager;

public class ControlUnitLogFragment extends Fragment {
    private ListView listView;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.log_fragment, null);

        initComponent(view);

        return view;
    }

    private void initComponent(View view) {
        listView = view.findViewById(R.id.logInfo);
        refreshLayout = view.findViewById(R.id.swipeLayout);
        refreshLayout.setOnRefreshListener(() -> {
            ControlUnitMessager messager = new ControlUnitMessager(CacheData.ip, CacheData.port, CommandTypes.GET_LOG);
            messager.setManager(new InternetManagerImpl());
            messager.start();
            //写好设置监听线程
        });
    }

}

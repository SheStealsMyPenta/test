package com.pd.config.pdonlineconfig.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.pd.config.pdonlineconfig.R;

public class TabFragment extends Fragment {
    private TextView textView;
    private LinearLayout linearLayout ;
    public static TabFragment newInstance(int index){
        Bundle bundle = new Bundle();
        bundle.putInt("index", 'A' + index);
        TabFragment fragment = new TabFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment, null);
        textView = view.findViewById(R.id.text);
        linearLayout = view.findViewById(R.id.container);

        if(getActivity()!=null){
            Toast.makeText(getActivity(), view.getMeasuredHeight()+"",Toast.LENGTH_LONG).show();
        }

        textView.setText(String.valueOf((char) getArguments().getInt("index")));
        return view;
    }

}

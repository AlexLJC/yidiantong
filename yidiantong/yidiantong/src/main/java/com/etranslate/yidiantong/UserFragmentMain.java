package com.etranslate.yidiantong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Alex on 2018/1/29.
 */

public class UserFragmentMain  extends Fragment {
    private View view;
    Button start;
    private EditText demand;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.translator_main, container,false);
        start=(Button) view.findViewById(R.id.start_searching_T);
        demand=(EditText)view.findViewById(R.id.demand);
        start.setOnClickListener(new View.OnClickListener(){
            //开始搜索
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.setClass(getContext(),SearchActivity.class);
                if(demand.getText().toString()!=null) {
                    Data.demand = demand.getText().toString();
                }
                else
                {
                    Data.demand="需求不清";
                }
                startActivity(intent);
            }

        });
        return view;

    }
}

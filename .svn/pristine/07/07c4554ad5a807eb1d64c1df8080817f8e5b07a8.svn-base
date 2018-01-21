package com.jiahehongye.robred.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.jiahehongye.robred.MainActivity;
import com.jiahehongye.robred.R;

/**
 * Created by huangjunhui on 2017/1/12.11:00
 */
public class SplashFragment extends Fragment implements View.OnClickListener {


    private  int index=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, null);
        ImageView imgView = (ImageView) view.findViewById(R.id.img);
        Button comeBtn = (Button) view.findViewById(R.id.come);

        imgView.setOnClickListener(this);
        //获取传到Fragment中的参数
         index = getArguments().getInt("INDEX");
        switch (index) {
            case 0:
                imgView.setBackgroundResource(R.drawable.guid1);
                comeBtn.setVisibility(View.GONE);
                break;
            case 1:
                imgView.setBackgroundResource(R.drawable.guid2);
                comeBtn.setVisibility(View.GONE);
                break;
            case 2:
                imgView.setBackgroundResource(R.drawable.guid3);
                comeBtn.setVisibility(View.GONE);
                break;
            case 3:
                imgView.setBackgroundResource(R.drawable.guid4);
                comeBtn.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        return view;
    }


    @Override
    public void onClick(View v) {
        if(index==3){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

    }
}

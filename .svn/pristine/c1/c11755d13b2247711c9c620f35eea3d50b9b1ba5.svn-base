package com.jiahehongye.robred.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.DFTTTitleResult;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;
import com.songheng.newsapisdk.sdk.apiservice.DFTTNewsApiService;
import com.songheng.newsapisdk.sdk.apiservice.listener.DfttApiServiceCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangeHobby extends BaseActivity {

    private String hobby;
    private TextView ok;
    private EditText edit;
    private ImageView back;
    private ArrayList<DFTTTitleResult.DdBean> dateTabList = new ArrayList<>();
    private GridView chose_hobby;
    private HobbyGridAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_change_hobby);
        chose_hobby = (GridView) findViewById(R.id.chose_hobby);

        adapter = new HobbyGridAdapter();
        chose_hobby.setAdapter(adapter);


        DFTTNewsApiService.getNewsColumnTag(new DfttApiServiceCallBack() {
            @Override
            public void onSuccess(String s) {
                StringBuilder sb = new StringBuilder();
                StringBuilder append = sb.append("{\"dd\":").append(s).append("}");

                initDate(append);
//                LogUtil.LogShitou("heheda:",s.toString());
//                textview.setText(s.toString());
            }

            @Override
            public void onError(String s, String s1, Response response, Exception e) {

            }
        });




        ok = (TextView) findViewById(R.id.changexingqu_ok);
        back = (ImageView) findViewById(R.id.changexingqu_back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata();
            }
        });
    }

    private void initDate(StringBuilder append) {
        DFTTTitleResult dfttTitleResult = new Gson().fromJson(append.toString(), DFTTTitleResult.class);
        dateTabList = new ArrayList<>();
        dateTabList.clear();
        ArrayList<DFTTTitleResult.DdBean> dd = (ArrayList) dfttTitleResult.getDd();

        for (int i = 0; i < dd.size(); i++) {
            if (dd.get(i).getIsup().equals("1")&&!dd.get(i).getName().equals("头条")) {
                dateTabList.add(dd.get(i));
            }
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            hobby = bundle.getString("hobby");

            String[] chrstr = hobby.split(",");
            for (int i = 0; i < chrstr.length; i++) {
                for (int j = 0; j < dateTabList.size(); j++) {
                    if (chrstr[i].equals(dateTabList.get(j).getName())) {
                        dateTabList.get(j).setSelect(true);
                    }
                }
            }
        }


        adapter.notifyDataSetChanged();


    }

    /**
     *
     */
    private PersistentCookieStore persistentCookieStore;
    private Call call;

    private void getdata() {

        int sizes = 0;

        StringBuffer hobby = new StringBuffer();

        for (int i = 0; i < dateTabList.size(); i++) {
            if (dateTabList.get(i).isSelect() == true) {
                hobby.append(dateTabList.get(i).getName()).append(",");
                sizes = sizes+1;
            }
        }
        if (sizes<4){
            Toast.makeText(ChangeHobby.this, "最少选择4个兴趣爱好", Toast.LENGTH_SHORT).show();
            return;
        }

        final String aaa = hobby.substring(0, hobby.length() - 1);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(ChangeHobby.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();


        RequestBody formBody = new FormBody.Builder()
                .add("hobbies", aaa).build();

        Request request = new Request.Builder()
                .url(Constant.CHANGE_MEMBER_INFO)

                .post(formBody)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ChangeHobby.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChangeHobby.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject object = new JSONObject(result);
                    String s = object.getString("result");
                    if (s.equals("success")) {
                        SPUtils.put(UIUtils.getContext(), Constant.USER_HOBBY, aaa);
                        ChangeHobby.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChangeHobby.this, "修改成功", Toast.LENGTH_SHORT).show();
                                hideSoftKeyboard();
                                finish();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    private class HobbyGridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dateTabList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.text_hobby, null);
                holder = new ViewHolder();
                holder.src = (TextView) convertView.findViewById(R.id.text_src);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.src.setText(dateTabList.get(position).getName());

            if (dateTabList.get(position).isSelect() == false) {
                holder.src.setBackgroundResource(R.drawable.edit_fdsfasdfas);
                holder.src.setTextColor(getResources().getColor(R.color.black));
            } else {
                holder.src.setBackgroundResource(R.drawable.hobby_chose);
                holder.src.setTextColor(getResources().getColor(R.color.white));
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dateTabList.get(position).isSelect() == false) {
                        dateTabList.get(position).setSelect(true);
                    } else {
                        dateTabList.get(position).setSelect(false);
                    }

                    notifyDataSetChanged();
                }
            });

            return convertView;
        }

        class ViewHolder {
            TextView src;
        }
    }
}

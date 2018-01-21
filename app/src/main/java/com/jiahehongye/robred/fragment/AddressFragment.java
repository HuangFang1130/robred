package com.jiahehongye.robred.fragment;

import android.view.View;

import com.jiahehongye.robred.BaseFragment;

/**
 * Created by Administrator on 2016/11/30.
 * <p/>
 * 通讯录
 */
public class AddressFragment extends BaseFragment implements View.OnClickListener {
    @Override
    public void onClick(View v) {

    }
//
//    private static final int REQUEST_SUCCESS = 3;
//    private SwipeRefreshLayout mAddressSwiperefresh;
//    private RecyclerView mAddressRecycle;
//    private LinearLayoutManager linearLayoutManager;
//    private AddressRecycleAdapter addressRecycleAdapter;
//    private String currentType = "0";
//    private String pageSize = 20 + "";
//    private String pageNumber = 1 + "";
//
//
//    private android.os.Handler handler = new android.os.Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case REQUEST_SUCCESS:
//                    String result = (String) msg.obj;
//                    AddressResult addressResult = new Gson().fromJson(result, AddressResult.class);
//                    if (addressResult.getResult().equals("success")) {
//                        showListDate(result);
//                    } else {
//                        Toast.makeText(getActivity(), "解析错误", Toast.LENGTH_SHORT).show();
//                    }
//                    if (mAddressSwiperefresh != null) {
//                        mAddressSwiperefresh.setRefreshing(false);
//                    }
//                    LogUtil.LogShitou("result: " + result.toString());
//                    break;
//            }
//        }
//    };
//    private List<AddressResult.DataBean> data;
//    private List<AddressResult.DataBean> fatherArrayList;
//    private TextView mAddressTvScreen;
//    private PopupWindow popupWindow;
//
//    private void showListDate(String result) {
//        AddressResult addressResult = new Gson().fromJson(result, AddressResult.class);
//        data = addressResult.getData();
//        int size = fatherArrayList.size();
//
//        fatherArrayList.addAll(data);
//        addressRecycleAdapter.notifyDataSetChanged();
//
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mMainUi.applyKitKatTranslucency();
//        mMainUi.mTintManager.setStatusBarTintResource(R.color.home_state_color);
//        View view = inflater.from(mMainUi).inflate(R.layout.activity_address, null);
//        mAddressSwiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.address_swiperefresh);
//        mAddressRecycle = (RecyclerView) view.findViewById(R.id.address_recycleview);
//        mAddressTvScreen = (TextView) view.findViewById(R.id.address_tv_screen);
//
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        fatherArrayList = new ArrayList<>();
//        currentType = "0";
//        pageNumber = "1";
//        requestDate(currentType, pageNumber);
//        mAddressSwiperefresh.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
//                R.color.holo_orange_light, R.color.holo_red_light);
//        mAddressSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                pageNumber = "1";
//                fatherArrayList.clear();
//                addressRecycleAdapter.notifyDataSetChanged();
//                requestDate(currentType, pageNumber);
//
//            }
//        });
//
//        linearLayoutManager = new LinearLayoutManager(getActivity());
//        mAddressRecycle.setLayoutManager(linearLayoutManager);
//        mAddressRecycle.setItemAnimator(new DefaultItemAnimator());
//        mAddressTvScreen.setOnClickListener(this);
//
//        //滑动的监听
//        mAddressRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
//                    if (linearLayoutManager.getItemCount() <= 1) {
//                        return;
//                    }
//                    if (lastVisiblePosition >= linearLayoutManager.getItemCount() - 1) {
//                        if (addressRecycleAdapter != null) {
//                            addressRecycleAdapter.showFootView();
//                        }
//                        int i = Integer.parseInt(pageNumber);
//                        i++;
//                        pageNumber = i + "";
//                        requestDate(currentType, pageNumber);
//                    }
//                }
//            }
//        });
//
////        addressRecycleAdapter = new AddressRecycleAdapter(getActivity(), fatherArrayList);
////        mAddressRecycle.setAdapter(addressRecycleAdapter);
//
//        addressRecycleAdapter.setOnItemClickListener(new MyItemClickListener() {
//            @Override
//            public void onItemClick(View view, int postion) {
//                Intent intent = new Intent(getActivity(), ContanctDetailActivity.class);
//                String mobile = fatherArrayList.get(postion).getMobile();
//                intent.putExtra("mobile", mobile);
//                startActivity(intent);
//            }
//        });
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//    }
//
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            mMainUi.applyKitKatTranslucency();
//            mMainUi.mTintManager.setStatusBarTintResource(R.color.home_state_color);
//        }
//    }
//
//    public void requestDate(String type, String pageNumber) {
//
//        String latitude = (String) SPUtils.get(UIUtils.getContext(), Constant.LATITUDE, "0.0");//纬度
//        String longitude = (String) SPUtils.get(UIUtils.getContext(), Constant.LONGITUDE, "0.0");//经度
//
//        OkHttpClient client = new OkHttpClient.Builder()
//                .cookieJar(new CookieJarImpl(new PersistentCookieStore(getActivity())))
//                .build();
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("type", type);
//            jsonObject.put("latitude", latitude + "");
//            jsonObject.put("longitude", longitude + "");
//            jsonObject.put("pageSize", pageSize);
//            jsonObject.put("pageNumber", pageNumber);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
//        Request request = new Request.Builder()
//                .url(Constant.ADDRESS_LIST)
//                .post(body)
//                .build();
//
//
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                if (mAddressSwiperefresh != null) {
//                    mAddressSwiperefresh.setRefreshing(false);
//                }
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String result = response.body().string();
//                Message msg = handler.obtainMessage();
//                msg.what = REQUEST_SUCCESS;
//                msg.obj = result;
//                handler.sendMessage(msg);
//            }
//        });
//
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//            case R.id.address_tv_screen://弹出popwindow
//
//                new ActionSheetDialog(getActivity()).builder().setCancelable(false).setCanceledOnTouchOutside(true)
//                        .addSheetItem("全部", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
//                            @Override
//                            public void onClick(int which) {
//                                fatherArrayList.clear();
//                                addressRecycleAdapter.notifyDataSetChanged();
//                                currentType = "0";
//                                pageNumber = "1";
//                                requestDate(currentType, pageNumber);
//                            }
//                        }).addSheetItem("男", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
//                    @Override
//                    public void onClick(int which) {
//
//                        fatherArrayList.clear();
//                        addressRecycleAdapter.notifyDataSetChanged();
//                        currentType = "1";
//                        pageNumber = "1";
//                        requestDate(currentType, pageNumber);
//                    }
//                }).addSheetItem("女", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
//                    @Override
//                    public void onClick(int which) {
//
//                        fatherArrayList.clear();
//                        addressRecycleAdapter.notifyDataSetChanged();
//                        currentType = "2";
//                        pageNumber = "1";
//                        requestDate(currentType, pageNumber);
//                    }
//                }).show();
//
//                break;
//
//        }
//    }
}

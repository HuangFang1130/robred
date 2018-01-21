package com.jiahehongye.robred.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.GrabRedActivity;
import com.jiahehongye.robred.activity.PayRedActivity;
import com.jiahehongye.robred.activity.SnatchOrderActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, "wx8f543a00c79d8221");
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {

//		Log.e(TAG, "onPayFinish, errCode = " + resp.errCode);
//
//		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle("1");
//			builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
//			builder.show();
//		}

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(("0").equals(Integer.toString(resp.errCode))){
				if (PayRedActivity.WHRER_TO_GO.equals("3")){
					finish();
				}else if (PayRedActivity.WHRER_TO_GO.equals("2")){
					Intent intent = new Intent(WXPayEntryActivity.this, GrabRedActivity.class);
					startActivity(intent);
					finish();
				}else if (PayRedActivity.WHRER_TO_GO.equals("1")){
					Intent intent = new Intent(WXPayEntryActivity.this, SnatchOrderActivity.class);
					startActivity(intent);
					finish();
				}
			}else if(("-1").equals(Integer.toString(resp.errCode))){
				//签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
				Toast.makeText(WXPayEntryActivity.this, "签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。", Toast.LENGTH_SHORT).show();
				finish();
			}else if(("-2").equals(Integer.toString(resp.errCode))){
				finish();
			}
		}
	}
}
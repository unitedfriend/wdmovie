package com.bw.movie.receiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

public class MessageReceiver extends XGPushBaseReceiver {

	@Override
	public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {
		Log.i("gxy","onRegisterResult");
	}

	@Override
	public void onUnregisterResult(Context context, int i) {
		Log.i("gxy",1+"onUnregisterResult");
	}

	@Override
	public void onSetTagResult(Context context, int i, String s) {
		Log.i("gxy",1+"onSetTagResult");
	}

	@Override
	public void onDeleteTagResult(Context context, int i, String s) {
		Log.i("gxy",1+"onDeleteTagResult");
	}

	@Override
	public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
		Log.i("gxy",1+"onTextMessage");
	}

	@Override
	public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
		Log.i("gxy",1+"onNotifactionClickedResult");
	}

	@Override
	public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
		Log.i("gxy",1+"onNotifactionShowedResult");
	}
}

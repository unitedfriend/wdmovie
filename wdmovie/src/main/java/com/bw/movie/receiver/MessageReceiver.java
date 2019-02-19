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

	}

	@Override
	public void onUnregisterResult(Context context, int i) {

	}

	@Override
	public void onSetTagResult(Context context, int i, String s) {

	}

	@Override
	public void onDeleteTagResult(Context context, int i, String s) {

	}

	@Override
	public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {

	}

	@Override
	public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

	}

	@Override
	public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

	}
}

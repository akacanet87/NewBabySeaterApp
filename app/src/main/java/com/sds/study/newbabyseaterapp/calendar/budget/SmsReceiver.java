package com.sds.study.newbabyseaterapp.calendar.budget;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.sds.study.newbabyseaterapp.calendar.CalendarActivity;

public class SmsReceiver extends BroadcastReceiver{

    String TAG;

    public SmsReceiver() {
        TAG=this.getClass().getName()+"/Canet";
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals( "android.provider.Telephony.SMS_RECEIVED" )) {

            StringBuilder sms = new StringBuilder();    // SMS문자를 저장할 곳
            Bundle bundle = intent.getExtras();         // Bundle객체에 문자를 받아온다

            if (bundle != null) {

                abortBroadcast();

                Intent startIntent = new Intent(context, CalendarActivity.class);
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(startIntent);

                // 번들에 포함된 문자 데이터를 객체 배열로 받아온다
                Object[] pdusObj = (Object[]) bundle.get("pdus");

                // SMS를 받아올 SmsMessage 배열을 만든다
                SmsMessage[] messages = new SmsMessage[pdusObj.length];

                for (int i = 0; i < pdusObj.length; i++) {

                    // SmsMessage의 static메서드인 createFromPdu로 pdusObj의
                    // 데이터를 message에 담는다
                    // 이 때 pdusObj는 byte배열로 형변환을 해줘야 함
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                }

                // SmsMessage배열에 담긴 데이터를 append메서드로 sms에 저장
                for (SmsMessage smsMessage : messages) {

                    // getMessageBody메서드는 문자 본문을 받아오는 메서드
                    sms.append(smsMessage.getMessageBody());

                }

                msgCut(sms.toString(), context);

            }
        }
    }

    public void msgCut(String sms,Context context){

        String[] msgarr = sms.split("\n");

        for(int i=0 ; i<msgarr.length ; i++){

            Log.d(TAG, i+"번째 라인 : "+msgarr[i]);

        }

    }

}

package com.repitch.trashsensor.thingspeak;

import android.app.Activity;
import android.util.Log;

import com.repitch.trashsensor.R;
import com.repitch.trashsensor.model.TrashReport;
import com.repitch.trashsensor.thingspeak.data.Channel;
import com.repitch.trashsensor.utils.ThingSpeakUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;

/**
 * Created by repitch on 15.01.17.
 */
public class TSController {

    private static final String TAG = TSController.class.getName();
    private Activity activity;
    private MqttAndroidClient client;

    public interface TSListener {
        void onError(Throwable e);

        void onConnected(boolean success);
    }

    public TSController(Activity activity) {
        this.activity = activity;
        initClient();
    }

    private void initClient() {
        String clientId = "Android";
        client = new MqttAndroidClient(activity.getApplicationContext(), activity.getString(R.string.broker_address),
                clientId);
    }

    public void publishTrashReport(final Channel channel, final TrashReport report) {
        if (client.isConnected()) {
            onConnected(true);
            finallyPublish(channel.getId(), report);
        } else {
            connectToServerAndThen(() -> {
                finallyPublish(channel.getId(), report);
                return null;
            });
        }
    }

    private void finallyPublish(int channelId, TrashReport report) {
        try {
            String payload = report.generateMessage();
            byte[] encodedPayload = new byte[0];
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setQos(0);
            message.setRetained(false);
            client.publish(ThingSpeakUtils.getTopic(activity, channelId), message);
        } catch (UnsupportedEncodingException | MqttException e) {
            Log.e(TAG, e.getMessage(), e);
            if (activity instanceof TSListener) {
                ((TSListener) activity).onError(e);
            }
        }
    }

    private void connectToServerAndThen(final Callable<Void> callable) {
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    onConnected(true);
                    try {
                        callable.call();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                        if (activity instanceof TSListener) {
                            ((TSListener) activity).onError(e);
                        }
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    onConnected(false);
                    if (activity instanceof TSListener) {
                        ((TSListener) activity).onError(exception);
                    }
                }
            });
        } catch (MqttException e) {
            Log.e(TAG, e.getMessage(), e);
            if (activity instanceof TSListener) {
                ((TSListener) activity).onError(e);
            }
        }
    }

    private void onConnected(boolean success) {
        if (activity instanceof TSListener) {
            ((TSListener) activity).onConnected(success);
        }
        Log.d(TAG, "Connection success: " + success);
    }


}

package com.repitch.trashsensor;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private static final String BROKER_ADDRESS = "tcp://mqtt.thingspeak.com:1883";
    private static final String CHANNEL_ID = "213466";
    private static final String API_KEY = "D6OIEIT5E9EZ3WJU";
    //  topic = "channels/" + channelID + "/publish/" + apiKey
    private static final String TOPIC = "channels/" + CHANNEL_ID + "/publish/" + API_KEY;

    private TextView txtConnectionResult;
    private EditText editVolume;
    private TextView txtVolumeError;
    private EditText editTemperature;
    private TextView txtTemperatureError;
    private Button btnSendData;

    private MqttAndroidClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initClient();
    }

    private void initViews() {
        txtConnectionResult = (TextView) findViewById(R.id.txt_connection_result);
        editVolume = (EditText) findViewById(R.id.edit_volume);
        txtVolumeError = (TextView) findViewById(R.id.txt_volume_error);
        editTemperature = (EditText) findViewById(R.id.edit_temperature);
        txtTemperatureError = (TextView) findViewById(R.id.txt_temperature_error);

        btnSendData = (Button) findViewById(R.id.btn_send_data);
        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()) {
                    String volume = editVolume.getText().toString();
                    String temperature = editTemperature.getText().toString();
                    TrashReport report = new TrashReport();
                    report.setField(TrashReport.FIELD_VOLUME, volume);
                    report.setField(TrashReport.FIELD_TEMPERATURE, temperature);
                    publish(report);
                }
            }
        });
    }

    private boolean validateData() {
        String volume = editVolume.getText().toString();
        boolean volumeValid;
        if (volume.isEmpty()) {
            volumeValid = false;
        } else {
            int volumeInt = Integer.parseInt(volume);
            volumeValid = volumeInt >= 0 && volumeInt <= 100;
        }
        txtVolumeError.setVisibility(volumeValid ? View.GONE : View.VISIBLE);

        String temperature = editTemperature.getText().toString();
        boolean temperatureValid;
        if (temperature.isEmpty()) {
            temperatureValid = false;
        } else {
            int temperatureInt = Integer.parseInt(temperature);
            temperatureValid = temperatureInt >= -50 && temperatureInt <= 100;
        }
        txtTemperatureError.setVisibility(temperatureValid ? View.GONE : View.VISIBLE);
        return volumeValid && temperatureValid;
    }

    private void publish(final TrashReport report) {
        txtConnectionResult.setText(R.string.connection_progress);
        txtConnectionResult.setTextColor(ContextCompat.getColor(this, R.color.color_text));
        if (client.isConnected()) {
            onConnected(true);
            finallyPublish(report);
        } else {
            connectToServerAndThen(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    finallyPublish(report);
                    return null;
                }
            });
        }
    }

    private void finallyPublish(TrashReport report) {
        try {
            String payload = report.generateMessage();
            byte[] encodedPayload = new byte[0];
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setQos(0);
            message.setRetained(false);
            client.publish(TOPIC, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
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
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    onConnected(false);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void messagePublished(boolean success) {
        if (success) {
            Toast.makeText(this, "SUCCESS message published!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error while publishing message!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initClient() {
        String clientId = "Android";
        client = new MqttAndroidClient(this.getApplicationContext(), BROKER_ADDRESS,
                clientId);
    }

    private void onConnected(boolean success) {
        if (success) {
            txtConnectionResult.setText(R.string.connection_success);
            txtConnectionResult.setTextColor(ContextCompat.getColor(this, R.color.success));
        } else {
            txtConnectionResult.setText(R.string.connection_error);
            txtConnectionResult.setTextColor(ContextCompat.getColor(this, R.color.error));
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_SHORT).show();
        }

    }

}

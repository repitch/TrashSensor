package com.repitch.trashsensor.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.repitch.trashsensor.R;
import com.repitch.trashsensor.model.TrashReport;
import com.repitch.trashsensor.thingspeak.TSController;
import com.repitch.trashsensor.thingspeak.data.Channel;

public class ChannelActivity extends AppCompatActivity implements TSController.TSListener {

    private static final String TAG = ChannelActivity.class.getName();
    private static final String EXTRA_CHANNEL = "channel";

    private TextView txtConnectionResult;
    private EditText editVolume;
    private TextView txtVolumeError;
    private EditText editTemperature;
    private TextView txtTemperatureError;
    private Button btnSendData;

    private Channel channel;
    private TSController tsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        obtainIntentData();
        initViews();
        initToolbar();
        tsController = new TSController(this);
    }

    private void initToolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Датчик #" + channel.getId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void obtainIntentData() {
        channel = (Channel) getIntent().getSerializableExtra(EXTRA_CHANNEL);
    }

    private void initViews() {
        txtConnectionResult = (TextView) findViewById(R.id.txt_connection_result);
        editVolume = (EditText) findViewById(R.id.edit_volume);
        txtVolumeError = (TextView) findViewById(R.id.txt_volume_error);
        editTemperature = (EditText) findViewById(R.id.edit_temperature);
        txtTemperatureError = (TextView) findViewById(R.id.txt_temperature_error);

        btnSendData = (Button) findViewById(R.id.btn_send_data);
        btnSendData.setOnClickListener(v -> {
            if (validateData()) {
                String volume = editVolume.getText().toString();
                String temperature = editTemperature.getText().toString();
                TrashReport report = new TrashReport();
                report.setField(TrashReport.FIELD_VOLUME, volume);
                report.setField(TrashReport.FIELD_TEMPERATURE, temperature);
                tsController.publishTrashReport(channel, report);
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

    private void messagePublished(boolean success) {
        if (success) {
            Toast.makeText(this, "SUCCESS message published!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error while publishing message!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(Throwable e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(boolean success) {
        if (success) {
            txtConnectionResult.setText(R.string.connection_success);
            txtConnectionResult.setTextColor(ContextCompat.getColor(this, R.color.success));
        } else {
            txtConnectionResult.setText(R.string.connection_error);
            txtConnectionResult.setTextColor(ContextCompat.getColor(this, R.color.error));
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_SHORT).show();
        }

    }

    public static Intent createIntent(@NonNull Context context, @NonNull Channel channel) {
        Intent intent = new Intent(context, ChannelActivity.class);
        intent.putExtra(EXTRA_CHANNEL, channel);
        return intent;
    }
}

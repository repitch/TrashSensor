package com.repitch.trashsensor.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.repitch.trashsensor.R;
import com.repitch.trashsensor.model.ChannelRepository;
import com.repitch.trashsensor.thingspeak.RetrofitManager;
import com.repitch.trashsensor.thingspeak.TSController;
import com.repitch.trashsensor.thingspeak.data.Channel;
import com.repitch.trashsensor.thingspeak.data.ChannelFeed;
import com.repitch.trashsensor.ui.adapter.ChannelAdapter;
import com.repitch.trashsensor.utils.CollectionUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by repitch on 15.01.17.
 */
public class ChannelListActivity extends AppCompatActivity implements ChannelAdapter.ChannelListener {

    private static final String TAG = ChannelListActivity.class.getName();

    private View sensorsPlaceholder;
    private RecyclerView rvSensors;
    private ChannelAdapter channelAdapter;
    private FloatingActionButton fabAddSensor;
    private Button btnAddSensor;

    private List<Channel> channels;
    private TSController tsController;
    private ChannelRepository channelRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_list);
        initViews();
        tsController = new TSController(this);
        channelRepository = new ChannelRepository();
        loadChannels();
    }

    private void loadChannels() {
        channels = channelRepository.getChannels();
        channelAdapter.updateData(channels);
        CollectionUtils.switchVisibility(rvSensors, sensorsPlaceholder, channels);
    }

    private void initViews() {
        sensorsPlaceholder = findViewById(R.id.sensors_placeholder);
        rvSensors = (RecyclerView) findViewById(R.id.rv_sensors);
        channelAdapter = new ChannelAdapter(this, null);
        rvSensors.setAdapter(channelAdapter);
        rvSensors.setLayoutManager(new LinearLayoutManager(this));

        btnAddSensor = (Button) findViewById(R.id.button_add_sensor);
        btnAddSensor.setOnClickListener(v -> showAddSensorDialog());
        fabAddSensor = (FloatingActionButton) findViewById(R.id.fab_add_sensor);
        fabAddSensor.setOnClickListener(v -> showAddSensorDialog());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.loading);
        progressDialog.setCancelable(false);
    }

    private void showAddSensorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Добавление сенсора");
        builder.setMessage("Введите id сенсора");
        final EditText editSensorId = new EditText(this);
        editSensorId.setHint("213466");
        editSensorId.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(editSensorId);

        builder.setPositiveButton("найти", (dialog, which) -> addSensor(editSensorId.getText().toString()));
        builder.setNegativeButton("отмена", null);
        builder.show();
    }

    private ProgressDialog progressDialog;

    private void showProgress(boolean show) {
        if (show) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    private void addSensor(String sensorId) {
        showProgress(true);
        int sensorIdInt = Integer.parseInt(sensorId);
        RetrofitManager.getInstance().getChannelFeed(sensorIdInt, new Callback<ChannelFeed>() {
            @Override
            public void onResponse(Call<ChannelFeed> call, Response<ChannelFeed> response) {
                showProgress(false);
                ChannelFeed feed = response.body();
                if (feed == null) {
                    showDialogChannelNotFound();
                } else {
                    Channel channel = feed.getChannel();
                    channelRepository.persistChannel(channel);
                    loadChannels();
                }
            }

            @Override
            public void onFailure(Call<ChannelFeed> call, Throwable t) {
                showProgress(false);
                Log.e(TAG, "failure");
            }
        });

    }

    private void showDialogChannelNotFound() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ошибка");
        builder.setMessage("Данный сенсор отсутствует на сервере thingspeak.com");
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    @Override
    public void onChannelClicked(Channel channel) {
        startActivity(ChannelActivity.createIntent(this, channel));
    }
}

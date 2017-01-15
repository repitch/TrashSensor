package com.repitch.trashsensor.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.repitch.trashsensor.R;
import com.repitch.trashsensor.thingspeak.data.Channel;

import java.util.List;

/**
 * Created by repitch on 15.01.17.
 */
public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ViewHolder> {

    private List<Channel> channels;
    private ChannelListener channelListener;

    public ChannelAdapter(@NonNull ChannelListener channelListener,
                          @Nullable List<Channel> channels) {
        this.channelListener = channelListener;
        this.channels = channels;
    }

    public void updateData(List<Channel> channels) {
        this.channels = channels;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sensor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Channel channel = channels.get(position);
        holder.itemView.setOnClickListener(v -> channelListener.onChannelClicked(channel));
        holder.txtSensorId.setText(Integer.toString(channel.getId()));
        holder.txtSensorName.setText(channel.getName());
        holder.txtSensorDesc.setText(channel.getDescription());
    }

    @Override
    public int getItemCount() {
        return channels == null ? 0 : channels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtSensorId;
        private TextView txtSensorName;
        private TextView txtSensorDesc;

        public ViewHolder(View itemView) {
            super(itemView);
            txtSensorId = (TextView) itemView.findViewById(R.id.txt_sensor_id);
            txtSensorName = (TextView) itemView.findViewById(R.id.txt_sensor_name);
            txtSensorDesc = (TextView) itemView.findViewById(R.id.txt_sensor_desc);
        }
    }

    public interface ChannelListener {
        void onChannelClicked(Channel channel);
    }
}

/*******************************************************************************
 * Copyright (c) 2013 Nordic Semiconductor. All Rights Reserved.
 * <p>
 * The information contained herein is property of Nordic Semiconductor ASA. Terms and conditions of usage are described in detail in NORDIC SEMICONDUCTOR STANDARD SOFTWARE LICENSE AGREEMENT.
 * Licensees are granted free, non-transferable use of the information. NO WARRANTY of ANY KIND is provided. This heading must NOT be removed from the file.
 ******************************************************************************/
package test.bpl.com.bplscreens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * DeviceListAdapter class is list adapter for showing scanned Devices name, address and RSSI image based on RSSI values.
 */
public class DeviceListAdapter extends BaseAdapter {
    private static final int NO_RSSI = -1000;

    private final List<ExtendedBluetoothDevice> listValues;
    private final Context context;

    public DeviceListAdapter(Context context, List<ExtendedBluetoothDevice> listValues) {
        this.context = context;
        this.listValues = listValues;
    }

    @Override
    public int getCount() {
        return listValues.size();
    }

    @Override
    public Object getItem(int position) {
        return listValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.device_list_row, null);
            holder.name =  view.findViewById(R.id.name);
            holder.address = view.findViewById(R.id.address);

            view.setTag(holder);
        }
        holder = (ViewHolder) view.getTag();
        final ExtendedBluetoothDevice device = (ExtendedBluetoothDevice) getItem(position);
        final String name = device.name;
        holder.name.setText(name != null ? name : "Device Not Available");
        holder.address.setText(device.device.getAddress());
        if (device.rssi != NO_RSSI) {
            final int rssiPercent = (int) (100.0f * (127.0f + device.rssi) / (127.0f + 20.0f));

        }
        return view;
    }

    private class ViewHolder {
        private TextView name;
        private TextView address;

    }
}

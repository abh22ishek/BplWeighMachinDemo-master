package biolight;

import android.annotation.*;
import android.app.*;
import android.view.*;
import android.widget.*;

import com.example.bluetoothlibrary.entity.*;

import java.util.*;

import test.bpl.com.bplscreens.*;

public class BioLightAdapter extends BaseAdapter {

    private ArrayList<Peripheral> mLeDevices;
    private LayoutInflater mInflator;
    private Activity mContext;


    public BioLightAdapter(Activity c ,ArrayList<Peripheral> showDevice) {
        super();
        mContext = c;
        mLeDevices = showDevice;
        mInflator = mContext.getLayoutInflater();
    }
    public Peripheral getDevice(int position)   {
        return mLeDevices.get(position);
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.biolight_list_device, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress = view.findViewById(R.id.device_address);
            viewHolder.deviceName = view.findViewById(R.id.device_name);
            viewHolder.type = view.findViewById(R.id.type);
            viewHolder.sn = view.findViewById(R.id.sn);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Peripheral device = mLeDevices.get(i);
        final String deviceName = device.getPreipheralMAC();

        if (deviceName != null && deviceName.length() > 0)
            viewHolder.deviceName.setText(deviceName);
        else
            viewHolder.deviceName.setText(R.string.unknown_device);

       // viewHolder.deviceAddress.setText(device.getBluetooth());
      //  viewHolder.type.setText(device.getModel());

        viewHolder.deviceAddress.setText(R.string.bpl_ip);
        viewHolder.type.setText(R.string.bt);
        viewHolder.sn.setText(device.getPreipheralSN());
        return view;
    }



    class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
        TextView type;
        TextView sn;
    }
}

package aicare.net.cn.iweightlibrary.wby;

import android.content.Intent;
import android.os.IBinder;

import java.util.List;

import aicare.net.cn.iweightlibrary.bleprofile.BleManager;
import aicare.net.cn.iweightlibrary.bleprofile.BleProfileService;
import aicare.net.cn.iweightlibrary.entity.BodyFatData;
import aicare.net.cn.iweightlibrary.entity.User;
import aicare.net.cn.iweightlibrary.entity.WeightData;
import aicare.net.cn.iweightlibrary.utils.AicareBleConfig;

public class WBYService extends BleProfileService implements WBYManagerCallbacks {
    private WBYManager mManager;
    public boolean mBinded;

    public final static int BLE_VERSION = 0;
    public final static int MCU_DATE = 1;
    public final static int MCU_TIME = 2;
    public final static int USER_ID = 3;
    public final static int ADC = 4;

    private final LocalBinder mBinder = new WBYBinder();
    private OnDeviceDataChangeListener listener;

    public void setOnDeviceDataChangeListener(OnDeviceDataChangeListener listener) {
        this.listener = listener;
    }

    public interface OnDeviceDataChangeListener {
        void onGetWeightData(WeightData weightData);
        void onGetSettingStatus(AicareBleConfig.SettingStatus status);
        void onGetResult(int index, String result);
        void onGetFatData(boolean isHistory, BodyFatData bodyFatData);
    }

    @Override
    public void getWeightData(WeightData weightData) {
        if (listener != null) {
            listener.onGetWeightData(weightData);
        }
    }

    @Override
    public void getSettingStatus(AicareBleConfig.SettingStatus status) {
        if (listener != null) {
            listener.onGetSettingStatus(status);
        }
    }

    @Override
    public void getResult(int index, String result) {
        if (listener != null) {
            listener.onGetResult(index, result);
        }
    }

    @Override
    public void getFatData(boolean isHistory, BodyFatData bodyFatData) {
        if (listener != null) {
            listener.onGetFatData(isHistory, bodyFatData);
        }
    }

    public class WBYBinder extends LocalBinder {

        /**
         * 获取历史记录
         */
        public void syncHistory(){
            mManager.sendCmd(AicareBleConfig.SYNC_HISTORY, AicareBleConfig.UNIT_KG);
        }

        /**
         * 同步当前用户
         * @param user
         */
        public void syncUser(User user) {
            if (user == null) {
                return;
            }
            mManager.syncUser(user);
        }

        /**
         * 同步用户列表
         * @param userList
         */
        public void syncUserList(List<User> userList) {
            mManager.syncUserList(userList);
        }

        /**
         * 同步当前单位
         * @param unit {@link AicareBleConfig#UNIT_KG}
         *             {@link AicareBleConfig#UNIT_LB}
         *             {@link AicareBleConfig#UNIT_ST}
         *             {@link AicareBleConfig#UNIT_JIN}
         */
        public void syncUnit(byte unit) {
            mManager.sendCmd(AicareBleConfig.SYNC_UNIT, unit);
        }

        /**
         * 同步时间
         */
        public void syncDate() {
            mManager.syncDate();
        }

        /**
         * 查询蓝牙版本信息
         */
        public void queryBleVersion() {
            mManager.sendCmd(AicareBleConfig.GET_BLE_VERSION, AicareBleConfig.UNIT_KG);
        }

        /**
         * 更新用户信息
         * @param user
         */
        public void updateUser(User user) {
            if (user == null) {
                return;
            }
            mManager.updateUser(user);
        }

        public WBYService getService() {
            return WBYService.this;
        }
    }

    @Override
    protected LocalBinder getBinder() {
        return mBinder;
    }

    @Override
    protected BleManager<WBYManagerCallbacks> initializeManager() {
        return mManager = WBYManager.getWBYManager();
    }

    @Override
    public IBinder onBind(final Intent intent) {
        mBinded = true;
        return super.onBind(intent);
    }

    @Override
    public void onRebind(final Intent intent) {
        mBinded = true;
    }

    @Override
    public boolean onUnbind(final Intent intent) {
        mBinded = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listener = null;
        mManager = null;
        mBinded = false;
    }
}

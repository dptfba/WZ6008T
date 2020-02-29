package com.example.wuzhi.Esptouch;


import android.location.LocationManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

import androidx.annotation.NonNull;

/**
 * Helper for accessing features in {@link LocationManager}.
 * 用于访问{@link LocationManager}中的特性的助手。
 */
public final class LocationManagerCompat {

    /**
     * Returns the current enabled/disabled state of location.
     * 返回当前位置的启用/禁用状态。
     *
     * @return true if location is enabled and false if location is disabled.
     */
    public static boolean isLocationEnabled(@NonNull LocationManager locationManager) {
        if (VERSION.SDK_INT >= VERSION_CODES.P) {
            return locationManager.isLocationEnabled();
        } else {
            // NOTE: for KitKat and above, it's preferable to use the proper API at the time to get
            // the location mode, Secure.getInt(context, LOCATION_MODE, LOCATION_MODE_OFF). however,
            // this requires a context we don't have directly (we could either ask the client to
            // pass one in, or use reflection to get it from the location manager), and since KitKat
            // and above remained backwards compatible, we can fallback to pre-kitkat behavior.
            //注意:对于奇巧或以上的产品，最好在购买时使用适当的API
            //定位模式，安全。getInt(上下文、LOCATION_MODE LOCATION_MODE_OFF)。然而,
            //这需要我们没有直接的上下文(我们可以要求客户这样做)
            //传一个进来，或者使用反射从位置管理器中获取它)
            //和以上保持向后兼容，我们可以退回到kitkat之前的行为。

            return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
    }

    private LocationManagerCompat() {}
}

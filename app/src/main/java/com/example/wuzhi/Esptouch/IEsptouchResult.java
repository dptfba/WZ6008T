package com.example.wuzhi.Esptouch;

import java.net.InetAddress;

public interface IEsptouchResult {

    /**
     * check whether the esptouch task is executed suc
     *      检查esptouch任务是否被suc执行
     * @return whether the esptouch task is executed suc
     *         esptouch任务是否被suc执行
     */
    boolean isSuc();

    /**
     * get the device's bssid  获取设备的bssid
     *
     * @return the device's bssid
     */
    String getBssid();

    /**
     * check whether the esptouch task is cancelled by user
     * 检查esptouch任务是否被用户取消
     *
     * @return whether the esptouch task is cancelled by user
     */
    boolean isCancelled();

    /**
     * get the ip address of the device
     * 获取设备的ip地址
     *
     * @return the ip device of the device
     */
    InetAddress getInetAddress();
}


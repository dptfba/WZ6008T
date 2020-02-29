package com.example.wuzhi.Esptouch;

public interface IEsptouchTaskParameter {

    /**
     * get interval millisecond for guide code(the time between each guide code sending)
     * 获取指导代码的间隔毫秒(每个指导代码发送之间的时间)
     *
     * @return interval millisecond for guide code(the time between each guide code sending)
     *          指导代码的间隔毫秒(每个指导代码发送之间的时间)
     */
    long getIntervalGuideCodeMillisecond();

    /**
     * get interval millisecond for data code(the time between each data code sending)
     * 获取数据代码的间隔毫秒(每个数据代码发送之间的时间)
     *
     * @return interval millisecond for data code(the time between each data code sending)
     */
    long getIntervalDataCodeMillisecond();

    /**
     * get timeout millisecond for guide code(the time how much the guide code sending)
     * 获取指导代码的超时毫秒(指导代码发送的时间)
     *
     * @return timeout millisecond for guide code(the time how much the guide code sending)
     */
    long getTimeoutGuideCodeMillisecond();

    /**
     * get timeout millisecond for data code(the time how much the data code sending)
     * 获取数据代码的超时毫秒(数据代码发送的时间)
     *
     * @return timeout millisecond for data code(the time how much the data code sending)
     */
    long getTimeoutDataCodeMillisecond();

    /**
     * get timeout millisecond for total code(guide code and data code altogether)
     * 获取总代码的超时毫秒(包括指导代码和数据代码)
     *
     * @return timeout millisecond for total code(guide code and data code altogether)
     */
    long getTimeoutTotalCodeMillisecond();

    /**
     * get total repeat time for executing esptouch task
     * 获取执行esptouch任务的总重复时间
     *
     * @return total repeat time for executing esptouch task
     */
    int getTotalRepeatTime();

    /**
     * the length of the Esptouch result 1st byte is the total length of ssid and
     * password, the other 6 bytes are the device's bssid
     *
     * 1字节的Esptouch结果的长度是ssid和的总长度
     * 密码，另外6个字节是设备的bssid
     */

    /**
     * get esptouchResult length of one
     * 得到esptouchResult长度为1
     *
     * @return length of one
     */
    int getEsptouchResultOneLen();

    /**
     * get esptouchResult length of mac
     * 得到mac的esptouchResult长度
     *
     * @return length of mac
     */
    int getEsptouchResultMacLen();

    /**
     * get esptouchResult length of ip
     * 获取esptouchResult的ip长
     *
     * @return length of ip
     */
    int getEsptouchResultIpLen();

    /**
     * get esptouchResult total length
     * 获取esptouchResult总长度
     *
     * @return total length
     */
    int getEsptouchResultTotalLen();

    /**
     * get port for listening(used by server)
     * 获取监听端口(供服务器使用)
     *
     * @return port for listening(used by server)
     */
    int getPortListening();

    /**
     * get target hostname
     * 得到目标主机名
     *
     * @return target hostame(used by client)
     */
    String getTargetHostname();

    /**
     * get target port
     * 得到目标端口
     *
     * @return target port(used by client)
     */
    int getTargetPort();

    /**
     * get millisecond for waiting udp receiving(receiving without sending)
     * 获得毫秒等待udp接收(接收不发送)
     *
     * @return millisecond for waiting udp receiving(receiving without sending)
     */
    int getWaitUdpReceivingMillisecond();

    /**
     * get millisecond for waiting udp sending(sending including receiving)
     * 获得等待udp发送的毫秒数(发送包括接收)
     *
     * @return millisecond for waiting udep sending(sending including receiving)
     */
    int getWaitUdpSendingMillisecond();

    /**
     * get millisecond for waiting udp sending and receiving
     * 获得毫秒等待udp发送和接收
     *
     * @return millisecond for waiting udp sending and receiving
     */
    int getWaitUdpTotalMillisecond();

    /**
     * set the millisecond for waiting udp sending and receiving
     * 设置等待udp发送和接收的毫秒数
     *
     * @param waitUdpTotalMillisecond the millisecond for waiting udp sending and receiving
     */
    void setWaitUdpTotalMillisecond(int waitUdpTotalMillisecond);

    /**
     * get the threshold for how many correct broadcast should be received
     * 获取应该接收多少正确广播的阈值
     *
     * @return the threshold for how many correct broadcast should be received
     */
    int getThresholdSucBroadcastCount();

    /**
     * get the count of expect task results
     * 获取预期任务结果的计数
     *
     * @return the count of expect task results
     */
    int getExpectTaskResultCount();

    /**
     * set the count of expect task results
     * 设置预期任务结果的计数
     *
     * @param expectTaskResultCount the count of expect task results
     */
    void setExpectTaskResultCount(int expectTaskResultCount);

    /**
     * Set broadcast or multicast
     * 设置广播或多播
     *
     * @param broadcast true is broadcast, false is multicast
     */
    void setBroadcast(boolean broadcast);
}
package com.example.wuzhi.Esptouch;

import java.util.List;

public interface IEsptouchTask {
    String ESPTOUCH_VERSION = "v0.3.7.2";

    /**
     * set the esptouch listener, when one device is connected to the Ap, it will be called back
     * 设置esptouch监听器，当一个设备连接到Ap时，它将被回调
     *
     * @param esptouchListener when one device is connected to the Ap, it will be called back
     */
    void setEsptouchListener(IEsptouchListener esptouchListener);

    /**
     * Interrupt the Esptouch Task when User tap back or close the Application.
     * 当用户回拨或关闭应用程序时，中断Esptouch任务。
     */
    void interrupt();

    /**
     * Note: !!!Don't call the task at UI Main Thread or RuntimeException will
     * be thrown Execute the Esptouch Task and return the result
     * 注意:! ! !不要在UI主线程中调用该任务，否则会出现RuntimeException
     * 执行Esptouch任务并返回结果
     * <p>
     * Smart Config v2.4 support the API
     *Smart Config v2.4支持该API
     * @return the IEsptouchResult
     */
    IEsptouchResult executeForResult() throws RuntimeException;

    /**
     * Note: !!!Don't call the task at UI Main Thread or RuntimeException will
     * be thrown Execute the Esptouch Task and return the result
     * 注意:! ! !不要在UI主线程中调用该任务，否则会出现RuntimeException
     * 执行Esptouch任务并返回结果
     * <p>
     * Smart Config v2.4 support the API
     * <p>
     * It will be blocked until the client receive result count >= expectTaskResultCount.
     * If it fail, it will return one fail result will be returned in the list.
     * If it is cancelled while executing,
     * if it has received some results, all of them will be returned in the list.
     * if it hasn't received any results, one cancel result will be returned in the list.
     *
     * 如果它失败了，它将返回一个失败的结果将在列表中返回。
     * 如果它在执行时被取消，
     * 如果它已经收到了一些结果，那么所有的结果都将返回到列表中。
     * 如果没有收到任何结果，将在列表中返回一个cancel结果。
     *
     * @param expectTaskResultCount the expect result count(if expectTaskResultCount <= 0,
     *                              expectTaskResultCount = Integer.MAX_VALUE)
     *                              expect结果计数(如果expectTaskResultCount <= 0，
     *                              expectTaskResultCount =整数. max_value)
     * @return the list of IEsptouchResult
     */
    List<IEsptouchResult> executeForResults(int expectTaskResultCount) throws RuntimeException;

    /**
     * check whether the task is cancelled by user
     *检查任务是否被用户取消
     * @return whether the task is cancelled by user
     */
    boolean isCancelled();

    /**
     * Set broadcast or multicast when post configure info
     *发布配置信息时设置广播或多播
     * @param broadcast true is broadcast, false is multicast
     */
    void setPackageBroadcast(boolean broadcast);
}


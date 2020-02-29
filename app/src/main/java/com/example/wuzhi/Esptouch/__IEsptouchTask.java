package com.example.wuzhi.Esptouch;

import java.util.List;

/**
 * IEsptouchTask defined the task of esptouch should offer. INTERVAL here means
 * the milliseconds of interval of the step. REPEAT here means the repeat times
 * of the step.
 *
 * EsptouchTask定义了esptouch应该提供的任务。时间间隔是指
 * 步距的毫秒数。这里的REPEAT表示重复的次数
 * 的一步。
 *
 * @author afunx
 */
public interface __IEsptouchTask {

    /**
     * Turn on or off the log.
     * 打开或关闭log。
     */
    static final boolean DEBUG = true;

    /**
     * set the esptouch listener, when one device is connected to the Ap, it will be called back
     * 设置esptouch监听器，当一个设备连接到Ap时，它将被回调
     *
     * @param esptouchListener when one device is connected to the Ap, it will be called back
     *                         当一个设备连接到Ap时，它将被回调
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
     *
     * @return the IEsptouchResult
     * @throws RuntimeException
     */
    IEsptouchResult executeForResult() throws RuntimeException;

    /**
     * Note: !!!Don't call the task at UI Main Thread or RuntimeException will
     * be thrown Execute the Esptouch Task and return the result
     * 注意:! ! !不要在UI主线程中调用该任务，否则会出现RuntimeException
     * 执行Esptouch任务并返回结果
     *
     * @param expectTaskResultCount the expect result count(if expectTaskResultCount <= 0,
     *                              expectTaskResultCount = Integer.MAX_VALUE)
     * @return the list of IEsptouchResult
     * @throws RuntimeException
     */
    List<IEsptouchResult> executeForResults(int expectTaskResultCount) throws RuntimeException;

    boolean isCancelled();
}

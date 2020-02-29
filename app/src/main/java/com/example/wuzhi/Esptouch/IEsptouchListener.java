package com.example.wuzhi.Esptouch;

public interface IEsptouchListener {
    /**
     * when new esptouch result is added, the listener will call
     * onEsptouchResultAdded callback
     *当新的esptouch结果被添加，监听器将调用
     * onEsptouchResultAdded回调
     * @param result the Esptouch result
     */
    void onEsptouchResultAdded(IEsptouchResult result);
}

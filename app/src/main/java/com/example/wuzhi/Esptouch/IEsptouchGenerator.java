package com.example.wuzhi.Esptouch;

public interface IEsptouchGenerator {
    /**
     * Get guide code by the format of byte[][]
     *按字节[][]的格式得到指导码
     * @return guide code by the format of byte[][]
     */
    byte[][] getGCBytes2();

    /**
     * Get data code by the format of byte[][]
     *按字节[][]格式获取数据代码
     * @return data code by the format of byte[][]
     */
    byte[][] getDCBytes2();
}

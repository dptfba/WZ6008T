package com.example.wuzhi.Esptouch;

/**
 * the class used to represent some code to be transformed by UDP socket should implement the interface
 *用来表示UDP套接字要转换的一些代码的类应该实现接口
 * @author afunx
 */
public interface ICodeData {
    /**
     * Get the byte[] to be transformed.
     *获取要转换的byte[]。
     * @return the byte[] to be transfromed
     */
    byte[] getBytes();

    /**
     * Get the char[](u8[]) to be transfromed.
     *获取要转换的char[](u8[])。
     * @return the char[](u8) to be transformed
     */
    char[] getU8s();
}

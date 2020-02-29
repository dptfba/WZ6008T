package com.example.wuzhi.Esptouch;


/**
 * An interface representing a data checksum.
 * 表示数据校验和的接口。
 *
 * @author      David Connelly
 */
public
interface Checksum {
    /**
     * Updates the current checksum with the specified byte.
     * 用指定的字节更新当前校验和。
     *
     * @param b the byte to update the checksum with
     */
    public void update(int b);

    /**
     * Updates the current checksum with the specified array of bytes.
     * 用指定的字节数组更新当前校验和。
     * @param b the byte array to update the checksum with
     * @param off the start offset of the data
     * @param len the number of bytes to use for the update
     */
    public void update(byte[] b, int off, int len);

    /**
     * Returns the current checksum value.
     * 返回当前校验和值。
     * @return the current checksum value
     */
    public long getValue();

    /**
     * Resets the checksum to its initial value.
     * 将校验和重置为其初始值。
     */
    public void reset();
}

package com.x.securityscanner.libparsesofile;

import android.support.annotation.NonNull;
import android.util.AndroidRuntimeException;

/**
 * Provide utility functions to get content of buffer 
 * @author ouhf1
 *
 */
public class SoBuffer {
	@NonNull /* package private */ final byte[] buf;

    public SoBuffer(@NonNull byte[] buf) {
        this.buf = buf;
    }
    
    /**
     * Read arbitrary bytes with offset and length
     * @param offset  offset to the beginning of buffer
     * @param len length
     * @return byte buffer with specific offset and length
     */
    public byte[] readAriBytes(int offset, int len) {
    	if(offset+len > this.buf.length) {
    		throw new AndroidRuntimeException("Encountered exceed the whole buffer capcity");
    	}
    	
    	byte[] result = new byte[len];   	
    	for(int i=0; i<len; i++) {
    		result[i] = this.buf[offset+i];
    	}
    	
        return result;
    }

    /**
     * Read 4 bytes unsigned integer with offset
     * @param offset offset to the beginning of buffer
     * @return value at specific offset
     */
    public int readSmallUint(int offset) {
        byte[] buf = this.buf;
        int result = (buf[offset] & 0xff) |
                ((buf[offset+1] & 0xff) << 8) |
                ((buf[offset+2] & 0xff) << 16) |
                ((buf[offset+3]) << 24);
        if (result < 0) {
            throw new AndroidRuntimeException("Encountered small uint that is out of range at offset " + Integer.toHexString(offset));
        }
        return result;
    }

    public int readOptionalUint(int offset) {
        byte[] buf = this.buf;
        int result = (buf[offset] & 0xff) |
                ((buf[offset+1] & 0xff) << 8) |
                ((buf[offset+2] & 0xff) << 16) |
                ((buf[offset+3]) << 24);
        if (result < -1) {
            throw new AndroidRuntimeException("Encountered optional uint that is out of range at offset " +Integer.toHexString(offset));
        }
        return result;
    }

    /**
     * Read 2 bytes unsigned short integer with offset
     * @param offset offset to the beginning of buffer
     * @return value at specific offset
     */
    public int readUshort(int offset) {
        byte[] buf = this.buf;
        return (buf[offset] & 0xff) |
                ((buf[offset+1] & 0xff) << 8);
    }

    /**
     * Read 1 unsigned byte with offset
     * @param offset offset to the beginning of buffer
     * @return  value with specific offset
     */
    public int readUbyte(int offset) {
        return buf[offset] & 0xff;
    }

    /**
     * Read 8 bytes long-integer with offset
     * @param offset offset to the beginning of buffer
     * @return value with specific offset
     */
    public long readLong(int offset) {
        byte[] buf = this.buf;
        return (buf[offset] & 0xff) |
                ((buf[offset+1] & 0xff) << 8) |
                ((buf[offset+2] & 0xff) << 16) |
                ((buf[offset+3] & 0xffL) << 24) |
                ((buf[offset+4] & 0xffL) << 32) |
                ((buf[offset+5] & 0xffL) << 40) |
                ((buf[offset+6] & 0xffL) << 48) |
                (((long)buf[offset+7]) << 56);
    }

    /**
     * Read 4 bytes integer with offset
     * @param offset offset to the beginning of buffer
     * @return value with specific offset
     */
    public int readInt(int offset) {
        byte[] buf = this.buf;
        return (buf[offset] & 0xff) |
                ((buf[offset+1] & 0xff) << 8) |
                ((buf[offset+2] & 0xff) << 16) |
                (buf[offset+3] << 24);
    }

    /**
     * Read 2 bytes short integer with offset
     * @param offset offset to the beginning of buffer
     * @return value with specific offset
     */
    public int readShort(int offset) {
        byte[] buf = this.buf;
        return (buf[offset] & 0xff) |
                (buf[offset+1] << 8);
    }

    /**
     * Read 1 byte with offset
     * @param offset offset to the beginning of buffer
     * @return value with specific offset
     */
    public int readByte(int offset) {
        return buf[offset];
    }
}
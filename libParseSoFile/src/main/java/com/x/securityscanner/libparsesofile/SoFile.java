package com.x.securityscanner.libparsesofile;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import android.support.annotation.NonNull;
import android.util.AndroidRuntimeException;

import com.google.common.io.ByteStreams;

/**
 * Utility class for storing the data of so file 
 * @author ouhf1
 *
 */
public class SoFile extends SoBuffer {

	public SoFile(byte[] buf) {
		super(buf);
	}

	/***
	 * Read file content into SoFile
	 * @param is InputStream of the file
	 * @return SoFile
	 * @throws IOException
	 */
	public static SoFile fromInputStream(@NonNull InputStream is)
			throws IOException {
		if (!is.markSupported()) {
			throw new IllegalArgumentException("InputStream must support mark");
		}
		is.mark(4);
		byte[] partialHeader = new byte[4];
		try {
			ByteStreams.readFully(is, partialHeader);
		} catch (EOFException ex) {
			throw new AndroidRuntimeException("File is too short");
		} finally {
			is.reset();
		}

		verifyMagic(partialHeader, 0);

		byte[] buf = ByteStreams.toByteArray(is);
		return new SoFile(buf);
	}
	
    private static void verifyMagic(@NonNull byte[] buf, int offset) {
        if (!ELFHeaderItem.verifyMagic(buf, offset)) {
            StringBuilder sb = new StringBuilder("Invalid magic value:");
            for (int i=0; i<4; i++) {
                sb.append(String.format(" %02x", buf[i]));
            }
            throw new AndroidRuntimeException(sb.toString());
        }
    }
}

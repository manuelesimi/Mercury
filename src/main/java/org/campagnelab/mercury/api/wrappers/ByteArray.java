package org.campagnelab.mercury.api.wrappers;

/**
 * A wrapper around a array of bytes.
 *
 * @author manuele
 */
public class ByteArray {

    private final byte[] array;

    public ByteArray(byte[] array) {
       this.array = array;
    }

    public byte[] getArray() {
        return array;
    }
}

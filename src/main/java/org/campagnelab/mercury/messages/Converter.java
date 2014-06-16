package org.campagnelab.mercury.messages;

import com.google.protobuf.GeneratedMessage;

import org.campagnelab.mercury.api.wrappers.ByteArray;

import java.io.IOException;
import java.lang.reflect.Method;


/**
 * Convert PB messages to/from byte arrays.
 *
 * @author manuele
 */
public class Converter {

    /**
     * Converts the message into an array of bytes.
     * @param message
     * @return
     * @throws IOException
     */
    public static ByteArray asByteArray(GeneratedMessage message) throws IOException {
        byte[] array = message.toByteArray();
        return new ByteArray(array);
    }

    /**
     * Creates and loads a PB message from the given array of bytes.
     * @param array
     * @param messageClass the concrete class of the PB message to load.
     * @param <MESSAGE> the class of the PB message to load.
     * @return
     * @throws Exception
     */
    public static <MESSAGE extends GeneratedMessage> MESSAGE asMessage(ByteArray array,  Class<MESSAGE> messageClass ) throws Exception {
        Method method = messageClass.getMethod("parseFrom", byte[].class);
        MESSAGE message = (MESSAGE) method.invoke(null, array.getArray());
        return message;
    }

}

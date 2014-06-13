package org.campagnelab.mercury.messages;

import com.google.protobuf.GeneratedMessage;

import org.campagnelab.mercury.api.ByteArray;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Convert PB messages to/from byte arrays.
 *
 * @author manuele
 */
public class Converter {

    public static ByteArray asByteArray(GeneratedMessage message) throws IOException {
        byte[] array = message.toByteArray();
        return new ByteArray(array);
    }

    public static <MESSAGE extends GeneratedMessage> MESSAGE asMessage(ByteArray buffer,  Class<MESSAGE> messageClass ) throws Exception {
        Method method = messageClass.getMethod("parseFrom", byte[].class);
        MESSAGE message = (MESSAGE) method.invoke(null, buffer.getArray());
        return message;
    }

}

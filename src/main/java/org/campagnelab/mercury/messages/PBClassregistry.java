package org.campagnelab.mercury.messages;

import com.google.protobuf.GeneratedMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mas2182 on 6/13/14.
 */
public class PBClassRegistry {

    private Map<Integer,Class<? extends GeneratedMessage>> registeredClasses
            = new HashMap<Integer, Class<? extends GeneratedMessage>>();

    private Map<Class<? extends GeneratedMessage>, Integer> registeredCodes
            = new HashMap<Class<? extends GeneratedMessage>, Integer>();


    public PBClassRegistry() {}

    public void registerPBClass(int code, Class<? extends GeneratedMessage> message) {
        this.registeredClasses.put(code,message);
        this.registeredCodes.put(message,code);
    }

    public int getCode(Class<? extends GeneratedMessage> message) {
       return this.registeredCodes.get(message);
    }

    public Class<? extends GeneratedMessage> getMessageClass(int code) {
        return this.registeredClasses.get(code);
    }

    public void clear() {
        this.registeredCodes.clear();
        this.registeredClasses.clear();
    }
}

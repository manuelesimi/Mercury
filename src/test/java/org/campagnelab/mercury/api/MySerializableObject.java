package org.campagnelab.mercury.api;

import java.io.Serializable;

/**
 * Created by mas2182 on 6/10/14.
 */
public class MySerializableObject implements Serializable {

    private static final long serialVersionUID = -7158088110191123270L;

    private String body;

    MySerializableObject(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }
}
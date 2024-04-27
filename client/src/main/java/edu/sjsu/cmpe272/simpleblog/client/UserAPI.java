package edu.sjsu.cmpe272.simpleblog.client;

import lombok.Getter;
import lombok.Setter;

public class UserAPI {

    @Getter
    @Setter
    private String user;
    @Getter
    @Setter
    private String publicKey;
    public UserAPI(String userid, String publicKey) {
        this.user = userid;
        this.publicKey = publicKey;
    }
}
package edu.sjsu.cmpe272.simpleblog.client;

import lombok.Getter;
import lombok.Setter;

public class UserService {

    public static String userid;
    public static String[] arr = new String[2];

    public UserService(String userid,String[] arr){
        this.userid = userid;
        this.arr = arr;
    }

    public static String getUserid() {
        return userid;
    }

    public static void setUserid(String userid) {
        UserService.userid = userid;
    }

    public static String[] getArr() {
        return arr;
    }

    public static void setArr(String[] arr) {
        UserService.arr = arr;
    }
}
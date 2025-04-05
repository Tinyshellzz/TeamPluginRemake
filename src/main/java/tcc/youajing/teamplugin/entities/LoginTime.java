//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin.entities;

import tcc.youajing.teamplugin.ObjectPool;

public class LoginTime {
    public int day_1;
    public int day_2;
    public int day_3;
    public int day_4;
    public int day_5;
    public int day_6;
    public int day_7;
    public int day_8;
    public int day_9;
    public int day_10;
    public int day_11;
    public int day_12;
    public int day_13;
    public int day_14;

    public LoginTime(int day_1, int day_2, int day_3, int day_4, int day_5, int day_6, int day_7, int day_8, int day_9, int day_10, int day_11, int day_12, int day_13, int day_14) {
        this.day_1 = day_1;
        this.day_2 = day_2;
        this.day_3 = day_3;
        this.day_4 = day_4;
        this.day_5 = day_5;
        this.day_6 = day_6;
        this.day_7 = day_7;
        this.day_8 = day_8;
        this.day_9 = day_9;
        this.day_10 = day_10;
        this.day_11 = day_11;
        this.day_12 = day_12;
        this.day_13 = day_13;
        this.day_14 = day_14;
    }

    public String toString() {
        return ObjectPool.gson.toJson(this);
    }
}

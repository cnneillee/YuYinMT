package com.uesct.neil.yuyinmt.bean;

import java.util.ArrayList;

/**
 * Created by Neil on 2016/2/25.
 */
public class VoiceBean {
    public ArrayList<WS> ws;

    public class WS {
        public ArrayList<CW> cw;

        public class CW {
            public String w;
        }
    }
}

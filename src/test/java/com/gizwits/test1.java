package com.gizwits;

import com.gizwits.util.TulingApiProcess;
import jodd.props.Props;

import java.io.File;
import java.io.IOException;

/**
 * Created by feel on 16/3/26.
 */
public class test1 {

    public static void main(String[] args) {


//        WxCryptUtil wxCryptUtil = new WxCryptUtil();
//        String decrypt = wxCryptUtil.decrypt("SD55TqCTd1vVPybHCn8UeEJNp+TLBHQP7NrqkLvqKoOuW0AD7BRs/oz6qRcL9O/q08Wq7UXqs6jeCq55LDMR/Q==");
//        System.out.println(decrypt);

        //  System.out.printf(TulingApiProcess.getTulingResult("明天要上班吗"));

        Props p = new Props();
        String path = test1.class.getResource("/example.props").getPath();

        System.out.println(path);
        try {
            p.load(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String story = p.getValue("story");

        System.out.println(story);


    }
}

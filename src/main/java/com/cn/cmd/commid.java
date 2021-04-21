package com.cn.cmd;

public class commid {
    //命令行参数
//    final static public String CREATECHAIN     = "createchain";
    final static public String GENERATEGENESIS = "generategensis";
    final static public String SENDTRASACTION  = "sendtransaction";
    final static public String GETBALANCE      = "getbalance"; //获取某个地址的余额
    final static public String GETLASTBLOCK    = "getlastblock";
    final static public String GETALLBLOCKS    = "getallblocks";
    final static public String GETBLOCKCOUNT   = "getblockcount";
    final static public String NEWADDRESS      = "newaddress";
    final static public String RESET           = "reset";
    final static public String HELP            = "help";
    final static public String[][] POST_COMMAD = new String[][]{
//        { "createchain","create new block"},
        {"generategensis","{\"address\":\"\"}"},
        {"sendtransaction","{\"from\":\"[\\\"\\\"]\",\"to\":\"[\\\"\\\"]\",\"value\":\"[]\"}"},
        {"getbalance","{\"address\":\"\"}"},
        {"getlastblock","get last block"},
        {"getallblocks","get all block"},
        {"getblockcount","get block count"},
        {"newaddress","new user addresss"},
        {"reset","clear blockchain"},
        {"help","help"},
    };

}

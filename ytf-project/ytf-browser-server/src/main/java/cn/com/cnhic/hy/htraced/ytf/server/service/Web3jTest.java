package cn.com.cnhic.hy.htraced.ytf.server.service;

import cn.com.cnhic.hy.htraced.ytf.server.service.EthService;

/**
 * @program: cn.hy.ytf
 * @description: Web3j练习测试类
 * @author: 王 恒
 * @create: 2019-10-12
 */
public class Web3jTest {
    public static void main(String[] args) throws Exception {
        EthService ethService=new EthService();
        //获取节点版本信息
        //System.out.println("节点版本信息："+ethService.getClientVersion());
        //获取秘钥对
//        System.out.println("私钥:"+ethService.getAccountTuple()[0]);
//        System.out.println("公钥："+ethService.getAccountTuple()[1]);
//        System.out.println("地址："+ethService.getAccountTuple()[2]);
//        //根据私钥获取公钥和地址
        //System.out.println("根据私钥获取地址"+ethService.importPrivateKey("569e312112460e773aa9f1e8b00c07e1addee0541b2502e5dc32b601106ab2f9")[0]);
        //System.out.println("根据私钥获取公钥："+ethService.importPrivateKey("569e312112460e773aa9f1e8b00c07e1addee0541b2502e5dc32b601106ab2f9")[1]);
        //使用密码创建钱包文件
        //System.out.println(ethService.newWalletFile("123456"));
        //System.out.println(ethService.newWalletFileByPrivateKey("123456","b7058ff9884533dff7feca7aafa7c8d8cbdaad574d24dcbf975584411658e678"));
        //加载钱包，返回凭证，获取秘钥对和地址
//        System.out.println("私钥: "+ethService.loadWalletFile("123456","E:\\Geth\\data\\keystore\\UTC--2019-10-12T02-52-45.867000000Z--aff8408eb8e45b4e69943371fc872be50e367d9d.json")[0]);
//        System.out.println("公钥："+ethService.loadWalletFile("123456","E:\\Geth\\data\\keystore\\UTC--2019-10-12T02-52-45.867000000Z--aff8408eb8e45b4e69943371fc872be50e367d9d.json")[1]);
//        System.out.println("地址："+ethService.loadWalletFile("123456","E:\\Geth\\data\\keystore\\UTC--2019-10-12T02-52-45.867000000Z--aff8408eb8e45b4e69943371fc872be50e367d9d.json")[2]);
        //获取节点管理账户列表
//        for (String s:ethService.getNodeAccounts()) {
//            System.out.println(s);
//        }
//        //获取账号余额
//        System.out.println(ethService.toEther(ethService.getBalance("0xf3711472849d8d87e6b953f464bb3bf9c4102ca5")));
//        System.out.println(ethService.toWei("5"));
        //发送普通交易
        //System.out.println(ethService.sendTransaction("0xe3FC325Fc90259fd804Eff1Cf67515D73545E394","0xb0a3B0DB6157C6e4505DE28F86465A641b5B1e8D","6","000000"));
        //发送普通交易并获取获取交易收据
        //ethService.transactionWithReceipt("0xe3FC325Fc90259fd804Eff1Cf67515D73545E394","0xb0a3B0DB6157C6e4505DE28F86465A641b5B1e8D","2","000000");
        //节点账户向钱包账户转账
       // ethService.transferToWallet("123456","E:\\Geth\\data\\keystore\\UTC--2019-10-12T02-52-45.867000000Z--aff8408eb8e45b4e69943371fc872be50e367d9d.json","000000");
        //发送裸交易
       // ethService.rawTransaction("123456","E:\\\\Geth\\\\data\\\\keystore\\\\UTC--2019-10-12T02-52-45.867000000Z--aff8408eb8e45b4e69943371fc872be50e367d9d.json","5","0x0aBEee2e1E20B4BfB731724E9574d68225543B4C");
        //使用交易管理器发送普通交易
        //ethService.transferMoney("0xe3FC325Fc90259fd804Eff1Cf67515D73545E394","0xAFf8408eB8e45B4E69943371fC872be50e367d9D","4","000000");
        //使用裸交易管理器发送裸交易
        //ethService.rawTransferMoney("123456","E:\\\\Geth\\\\data\\\\keystore\\\\UTC--2019-10-12T02-52-45.867000000Z--aff8408eb8e45b4e69943371fc872be50e367d9d.json","0x0aBEee2e1E20B4BfB731724E9574d68225543B4C","3");
        //System.out.println(ethService.toEther((4300000*22000000000l)+""));
//        String[] strings=ethService.loadWalletFile("000000","E:\\Geth\\data\\keystore\\UTC--2019-10-09T06-42-04.732587700Z--0abeee2e1e20b4bfb731724e9574d68225543b4c");
//        System.out.println("私钥："+strings[0]);
//        System.out.println("公钥："+strings[1]);
//        System.out.println("地址："+strings[2]);
       // System.out.println(Double.valueOf("75.99198892")-ethService.toEther("5000000000000000000").doubleValue());
//        System.out.println(ethService.getClientVersion());
//        String a=ethService.getClientVersion();
//        a=a.substring(a.indexOf("/")+1,a.indexOf("v")-1);
//        System.out.println(a);
        // System.out.println("0xf3711472849d8d87e6b953f464bb3bf9c4102ca5".toCharArray().length);
//        System.out.println(HexUtils.toHexString("20191022135130:出厂".getBytes("utf-8")));
//        String s=new String(HexUtils.fromHexString("32303139313032323133353133303ae587bae58e82"));
//        System.out.println(s);
//        System.out.println(s.substring(0,s.indexOf(":")));
//        System.out.println(s.substring(s.indexOf(":")+1));


    }
}

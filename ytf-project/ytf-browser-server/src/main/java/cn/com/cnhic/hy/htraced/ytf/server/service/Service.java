package cn.com.cnhic.hy.htraced.ytf.server.service;

import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.http.HttpService;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@org.springframework.stereotype.Service
public class Service {
    @Autowired
     Web3j web3j;
    @Autowired
    Admin admin =Admin.build(new HttpService());


    /**
     * 生成秘钥公钥，钱包地址 方法1（不生成json文件）
     * @param seed
     * @return
     */
    public static String createAccount(String seed) {
        try {
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            BigInteger privateKeyInDec = ecKeyPair.getPrivateKey();
            BigInteger publicKeyInDec= ecKeyPair.getPublicKey();
            String privateKey = privateKeyInDec.toString(16);
            String publicKey=publicKeyInDec.toString(16);
            WalletFile aWallet = Wallet.createLight(seed, ecKeyPair);
            String address = aWallet.getAddress();
            if (address.startsWith("0x")) {
                address = address.substring(2).toLowerCase();
            } else {
                address = address.toLowerCase();
            }
            address = "0x" + address;
            System.out.println("地址：" + address);
            System.out.println("秘钥：" + privateKey);
            System.out.println("公钥："+publicKey);
            return privateKey;
        } catch (InvalidAlgorithmParameterException |
                CipherException | NoSuchProviderException | NoSuchAlgorithmException e) {
            System.out.println(e.getCause().toString());
            return null;
        }

    }

    /**
     * 生成秘钥公钥，钱包地址 方法2(生成json文件)
     * @param pwd
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws InvalidAlgorithmParameterException
     * @throws CipherException
     * @throws IOException
     */
    public static String creatAccount(String pwd) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CipherException, IOException {
        // 钱包存放路径
        String walletFilePath = "F:\\key";
        //生成钱包，对应目录下会创建对应的私钥文件。
        String walletFileName=WalletUtils.generateNewWalletFile(pwd,new File(walletFilePath),false);
        // 加载指定位置的钱包
        Credentials credentials=WalletUtils.loadCredentials(pwd,walletFilePath+"/"+walletFileName);
        String address=credentials.getAddress();
        System.out.println("地址：" + address);
        System.out.println("秘钥：" + credentials.getEcKeyPair().getPrivateKey());
        System.out.println("公钥："+credentials.getEcKeyPair().getPublicKey());
        return address;
    }

    public static void main(String[] args) throws IOException {
        Service service=new Service();
//        String send= UUID.randomUUID().toString();
//       String privateKey=Service.createAccount(send);
//        String password="123456";
//        String[] keys={"privateKey","password"};
//        String[] strings={privateKey,password};
//        System.out.println(
//                Service.httpRequest("http://viewapi.huayuyun.cn/api/V1/Account/createAccount","POST",keys,strings));
        //System.out.println(Service.transaction("0x8bf31f48568ed875fc6b2343fef8536bfc842630","获取通证",1));
        //System.out.println(BigInteger.valueOf(21_000));
        PersonalUnlockAccount personalUnlockAccount=service.admin.personalUnlockAccount("0xe3FC325Fc90259fd804Eff1Cf67515D73545E394","000000").send();
        System.out.println(personalUnlockAccount.accountUnlocked());
        /*try {
            creatAccount("123456");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }*/
        //createAccount("123456");
        System.out.println(new String(HexUtils.fromHexString("e4b8ade59bbde79c9fe79a84e5a5bd")));
    }

    /**
     * 处理http请求  requestUrl为请求地址  requestMethod请求方式，值为"GET"或"POST"
     */
    public static String httpRequest(String requestUrl, String requestMethod, Object[] outputStrKey,Object[] outputStrValue){
        StringBuffer buffer=null;
        try{
            URL url=new URL(requestUrl);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod(requestMethod);
            conn.connect();
            //往服务器端写内容 也就是发起http请求需要带的参数
            if(null!=outputStrValue){
                OutputStream os=conn.getOutputStream();
                StringBuffer stringBuffer=new StringBuffer();
                System.out.println(outputStrKey.length);
                for (int i = 0; i < outputStrKey.length; i++) {
                    if (outputStrKey[i]!=null){
                        stringBuffer.append("&"+outputStrKey[i]+"="+outputStrValue[i]);
                    }else {
                        break;
                    }

                }
                System.out.println(stringBuffer.toString());
                os.write((stringBuffer.toString()).getBytes("utf-8"));
                os.close();
            }

            //读取服务器端返回的内容
            InputStream is=conn.getInputStream();
            InputStreamReader isr=new InputStreamReader(is,"utf-8");
            BufferedReader br=new BufferedReader(isr);
            buffer=new StringBuffer();
            String line=null;
            while((line=br.readLine())!=null){
                buffer.append(line);
            }
            is.close();
            isr.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 交易
     * @param address 收账人地址
     * @param title 备注
     * @return
     */
    public static String transaction(String address,String title,Integer value){
        String from="0xa43333c6135262e3bde87c7c8058361a4fd0f096";
        String password="484577";
        String to=address;
        String input=title;
        Object[] keys={"from","password","to","input","value"};
        Object[] values={from,password,to,input,value};
      String transaction=Service.httpRequest("http://viewapi.huayuyun.cn/api/V1/Order/addOrder","POST",keys,values);
        return transaction;
    }



}

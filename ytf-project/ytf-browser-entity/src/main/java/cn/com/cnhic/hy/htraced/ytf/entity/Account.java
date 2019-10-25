package cn.com.cnhic.hy.htraced.ytf.entity;

/**
 * @program: cn.hy.ytf.model
 * @description: 账户详情类
 * @author: 王恒
 * @create: 2019-10-10
 */
public class Account {
    private Integer id;
    private String address;
    private String balance;
    private Integer txNumber;
    private String pwd;
    private String privateKey;
    private String publicKey;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Integer getTxNumber() {
        return txNumber;
    }

    public void setTxNumber(Integer txNumber) {
        this.txNumber = txNumber;
    }
}

package cn.com.cnhic.hy.htraced.ytf.server.model;

/**
 * @program: cn.hy.ytf.model
 * @description: 区块信息类
 * @author: 王 恒
 * @create: 2019-10-15
 */
public class Block {
    private Integer id;
    private String number;
    private String hash;
    private String miner;
    private String gasLimit;
    private String gasUsed;
    private String txCount;
    private String size;
    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getMiner() {
        return miner;
    }

    public void setMiner(String miner) {
        this.miner = miner;
    }

    public String getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(String gasLimit) {
        this.gasLimit = gasLimit;
    }

    public String getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
    }

    public String getTxCount() {
        return txCount;
    }

    public void setTxCount(String txCount) {
        this.txCount = txCount;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}

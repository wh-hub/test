package cn.com.cnhic.hy.htraced.ytf.client.api;

import cn.com.cnhic.hy.htraced.ytf.entity.Account;
import cn.com.cnhic.hy.htraced.ytf.entity.Block;
import cn.com.cnhic.hy.htraced.ytf.entity.BlockchainTransaction;
import cn.com.cnhic.hy.htraced.ytf.entity.Node;
import cn.com.cnhic.hy.htraced.ytf.entity.Transaction;
import cn.com.cnhic.hy.htraced.ytf.entity.TransactionReceipt;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * <b>功能描述：</b>容错处理类<br>
 * <b>修订记录：</b><br>
 * <li>20191024&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
 */
@Component
public class FeignFallBack implements FeignService {


    @Override
    public String createPrivateKey() {
        return null;
    }

    @Override
    public String account(String pwd) {
        return null;
    }

    @Override
    public String accountImportPrivateKey(String pwd, String privateKey) {
        return null;
    }

    @Override
    public BlockchainTransaction execute(BlockchainTransaction blockchainTransaction) {
        return null;
    }

    @Override
    public Long height() {
        return null;
    }

    @Override
    public BigInteger nonce(String addr) {
        return null;
    }

    @Override
    public String balance(String addr) {
        return null;
    }

    @Override
    public Transaction getTransactionByHash(String hash) {
        return null;
    }

    @Override
    public List<Block> getBlocks() {
        return null;
    }

    @Override
    public Block getBlockByHash(String blockHash) {
        return null;
    }

    @Override
    public List<Transaction> getBlockTransactionByHash(String blockHash) {
        return null;
    }

    @Override
    public Block getInfoByNumber(Long number) {
        return null;
    }

    @Override
    public List<Transaction> getBlockTransactionByNumber(Long number) {
        return null;
    }

    @Override
    public List<Account> getAccounts() {
        return null;
    }

    @Override
    public Account getAccountInfo(String addr) {
        return null;
    }

    @Override
    public List<Transaction> getTransactionByAddr(String addr) {
        return null;
    }

    @Override
    public List<Transaction> getTransaction() {
        return null;
    }

    @Override
    public TransactionReceipt getReceipt(String hash) {
        return null;
    }

    @Override
    public Map initData() {
        return null;
    }

    @Override
    public List<Node> getNodeList(String pagenum, Integer type, String area) {
        return null;
    }

    @Override
    public List<String> getAreas() {
        return null;
    }

    @Override
    public List<String> getTypeList() {
        return null;
    }

    @Override
    public String search(String data) {
        return null;
    }
}
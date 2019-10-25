package cn.com.cnhic.hy.htraced.ytf.client.api;

import cn.com.cnhic.hy.htraced.ytf.entity.Account;
import cn.com.cnhic.hy.htraced.ytf.entity.Block;
import cn.com.cnhic.hy.htraced.ytf.entity.BlockchainTransaction;
import cn.com.cnhic.hy.htraced.ytf.entity.Node;
import cn.com.cnhic.hy.htraced.ytf.entity.Transaction;
import cn.com.cnhic.hy.htraced.ytf.entity.TransactionReceipt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@FeignClient(value = "service-test", fallback = FeignFallBack.class)
public interface FeignService {

    //服务中方法的映射路径

    @RequestMapping(path = "/api/ytf/Account/createPrivateKey")
    public String createPrivateKey();
    @RequestMapping(path = "/api/ytf/newAccount")
    public String account(String pwd);
    @RequestMapping(path = "/api/ytf/Account/createAccount")
    public String accountImportPrivateKey(String pwd,String privateKey);
    @RequestMapping(path = "/api/ytf//Order/addOrder")
    public BlockchainTransaction execute(BlockchainTransaction blockchainTransaction);
    @RequestMapping(path = "/api/ytf/height")
    public Long height();
    @RequestMapping("/api/ytf/nonce")
    public BigInteger nonce(String addr);
    @RequestMapping("/api/ytf/balance")
    public String balance(String addr);
    @RequestMapping("/api/ytf/Order/getInfo")
    public Transaction getTransactionByHash(String hash);
    @RequestMapping(path = "/api/ytf/Block/getList")
    public List<Block> getBlocks();
    @RequestMapping(path = "/api/ytf/Block/getInfoByHash")
    public Block getBlockByHash(String blockHash);
    @RequestMapping(path = "/api/ytf/Block/getBlockTransactionByHash")
    public List<Transaction> getBlockTransactionByHash(String blockHash);
    @RequestMapping(path = "/api/ytf/Block/getInfoByNumber")
    public Block getInfoByNumber(Long number);
    @RequestMapping(path = "/api/ytf/Block/getBlockTransactionByNumber")
    public List<Transaction> getBlockTransactionByNumber(Long number);
    @RequestMapping("/api/ytf/Account/getList")
    public List<Account> getAccounts();
    @RequestMapping("/api/ytf/Account/getInfo")
    public Account getAccountInfo(String addr);
    @RequestMapping("/api/ytf/Account/getOrderList")
    public List<Transaction> getTransactionByAddr(String addr);
    @RequestMapping("/api/ytf/Order/getList")
    public List<Transaction> getTransaction();
    @RequestMapping("/api/ytf/Order/getReceipt")
    public TransactionReceipt getReceipt(String hash);
    @RequestMapping("/api/ytf/Index/initData")
    public Map initData();
    @RequestMapping("/api/ytf/Peer/getPeerList")
    public List<Node> getNodeList(String pagenum, Integer type, String area);
    @RequestMapping("/api/ytf/Peer/getAreaList")
    public List<String> getAreas();
    @RequestMapping("/api/ytf/Peer/getTypeList")
    public List<String> getTypeList();
    @RequestMapping("/api/ytf/Index/search")
    public String search(String data);


}
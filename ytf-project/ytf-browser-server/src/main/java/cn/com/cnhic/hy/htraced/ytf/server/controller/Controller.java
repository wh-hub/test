package cn.com.cnhic.hy.htraced.ytf.server.controller;


import cn.com.cnhic.hy.htraced.ytf.server.model.Account;
import cn.com.cnhic.hy.htraced.ytf.server.model.Block;
import cn.com.cnhic.hy.htraced.ytf.server.model.BlockchainTransaction;
import cn.com.cnhic.hy.htraced.ytf.server.model.Node;
import cn.com.cnhic.hy.htraced.ytf.server.model.Transaction;
import cn.com.cnhic.hy.htraced.ytf.server.model.TransactionReceipt;
import cn.com.cnhic.hy.htraced.ytf.server.service.BlockChainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.core.methods.response.EthBlock;


import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/${spring.cloud.config.profile}")
public class Controller {

    @Autowired
    private BlockChainService blockChainService;

    /**
     * <b>功能描述：</b>生成私钥<br>
     * <b>修订记录：</b><br>
     * <li>20191014&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @RequestMapping("/Account/createPrivateKey")
    public String createPrivateKey(){
        try {
            return blockChainService.createPrivateKey();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 仅使用密码创建账户
     * @param pwd
     * @return
     */
    @RequestMapping("/newAccount")
    public String account(String pwd){
        try {
            return blockChainService.newAccount(pwd);
        } catch (IOException | CipherException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * <b>功能描述：</b>使用密码和导入私钥创建账户<br>
     * <b>修订记录：</b><br>
     * <li>20191014&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @RequestMapping("/Account/createAccount")
    public String accountImportPrivateKey(String pwd,String privateKey){
        try {
            return blockChainService.newAccountImportPrivateKey(pwd,privateKey);
        } catch (CipherException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 普通交易
     * @param blockchainTransaction
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/Order/addOrder",method = RequestMethod.POST)
    public BlockchainTransaction execute(BlockchainTransaction blockchainTransaction) {
        System.out.println(blockchainTransaction.getFromAddr()+".."+blockchainTransaction.getToAddr()+".."+blockchainTransaction.getValue()+".."+blockchainTransaction.getPassword());
        try {
            if (blockChainService.unlock(blockchainTransaction.getFromAddr(),blockchainTransaction.getPassword())){
                    return blockChainService.process(blockchainTransaction);
            }else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 获取当前区块高度
     * @return
     * @throws IOException
     */
    @RequestMapping("/height")
    public Long height() throws IOException {
        return blockChainService.getHeight();
    }

    /**
     * 获取账户的Nonce
     * @param addr
     * @return
     */
    @RequestMapping("/nonce")
    public BigInteger nonce(String addr){
        try {
            return blockChainService.getNonce(addr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 查询账户中余额
     * @param addr
     * @return
     */
    @RequestMapping("/balance")
    public String balance(String addr){
        try {
            return blockChainService.getBalance(addr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取交易详情
     * @param hash
     * @return
     */
    @RequestMapping("/Order/getInfo")
    public Transaction getTransactionByHash(String hash){
        try {
            return blockChainService.getTransaction(hash);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * <b>功能描述：</b>根据当前区块高度获取高度信息<br>
     * <b>修订记录：</b><br>
     * <li>20191010&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @RequestMapping("/getBlockInfo")
    public EthBlock getBlockInfo(){
        try {
            return blockChainService.getBlockList();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * <b>功能描述：</b>获取区块列表<br>
     * <b>修订记录：</b><br>
     * <li>20191017&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @RequestMapping("/Block/getList")
    public List<Block> getBlocks(){
        try {
            return blockChainService.getBlocks();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * <b>功能描述：</b>根据区块HASH查询区块信息<br>
     * <b>修订记录：</b><br>
     * <li>20191010&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @RequestMapping("/Block/getInfoByHash")
    public Block getBlockByHash(String blockHash){
        try {
            return blockChainService.getBlockByHash(blockHash);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * <b>功能描述：</b>根据区块HASH查询区块中的交易列表<br>
     * <b>修订记录：</b><br>
     * <li>20191010&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @RequestMapping("/Block/getBlockTransactionByHash")
    public List<Transaction> getBlockTransactionByHash(String blockHash){
        try {
            return blockChainService.getBlockTransactionByHash(blockHash);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * <b>功能描述：</b>根据区块Number查询区块信息<br>
     * <b>修订记录：</b><br>
     * <li>20191010&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @RequestMapping("/Block/getInfoByNumber")
    public Block getInfoByNumber(Long number){
        try {
            return blockChainService.getInfoByNumber(number);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * <b>功能描述：</b>根据区块Number查询区块中的交易列表<br>
     * <b>修订记录：</b><br>
     * <li>20191010&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @RequestMapping("/Block/getBlockTransactionByNumber")
    public List<Transaction> getBlockTransactionByNumber(Long number){
        try {
            return blockChainService.getBlockTransactionByNumber(number);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * <b>功能描述：</b>获取账号地址列表<br>
     * <b>修订记录：</b><br>
     * <li>20191010&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @RequestMapping("/Account/getList")
    public List<Account> getAccounts(){
        try {
            return blockChainService.getAccounts();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * <b>功能描述：</b>根据账号地址获取账号信息<br>
     * <b>修订记录：</b><br>
     * <li>20191010&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @RequestMapping("/Account/getInfo")
    public Account getAccountInfo(String addr){
        try {
            return blockChainService.getAccountInfo(addr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * <b>功能描述：</b>根据账号地址获取账号交易列表<br>
     * <b>修订记录：</b><br>
     * <li>20191017&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @RequestMapping("/Account/getOrderList")
    public List<Transaction> getTransactionByAddr(String addr){
        try {
            return blockChainService.getTransactionByAddr(addr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * <b>功能描述：</b>获取交易列表<br>
     * <b>修订记录：</b><br>
     * <li>20191010&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @RequestMapping("/Order/getList")
    public List<Transaction> getTransaction(){
        try {
            return blockChainService.getTransactionList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * <b>功能描述：</b>获取交易收据<br>
     * <b>修订记录：</b><br>
     * <li>20191011&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @RequestMapping("/Order/getReceipt")
    public TransactionReceipt getReceipt(String hash){
        try {
            return blockChainService.getTransactionReceipt(hash);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * <b>功能描述：</b>初始化首页数据<br>
     * <b>修订记录：</b><br>
     * <li>20191016&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @RequestMapping("/Index/initData")
    public Map initData(){
        try {
            return blockChainService.initData();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * <b>功能描述：</b>获取节点列表<br>
     * <b>修订记录：</b><br>
     * <li>20191016&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @RequestMapping("/Peer/getPeerList")
    public List<Node> getNodeList(String pagenum, Integer type, String area){
        try {
            return blockChainService.getNodeList(Integer.valueOf(pagenum),type,area);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * <b>功能描述：</b>获取节点地区列表<br>
     * <b>修订记录：</b><br>
     * <li>20191017&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @RequestMapping("/Peer/getAreaList")
    public List<String> getAreas() throws Exception {
        return blockChainService.getAreaList();
    }
    /**
     * <b>功能描述：</b>获取节点类型列表<br>
     * <b>修订记录：</b><br>
     * <li>20191017&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @RequestMapping("/Peer/getTypeList")
    public List<String> getTypeList(){
        List<String> types=new ArrayList<>();
        types.add("普通节点");
        types.add("一般节点");
        types.add("超级节点");
        return types;
    }
    /**
     * <b>功能描述：</b>判断参数类型<br>
     * <b>修订记录：</b><br>
     * <li>20191016&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @RequestMapping("/Index/search")
    public String search(String data){
        try {
            return blockChainService.search(data);
        } catch (Exception e) {
            e.printStackTrace();
            return "异常错误";
        }
    }
}

package cn.com.cnhic.hy.htraced.ytf.server.service;


import cn.com.cnhic.hy.htraced.ytf.server.dao.AccountMapper;
import cn.com.cnhic.hy.htraced.ytf.server.dao.BlockMapper;
import cn.com.cnhic.hy.htraced.ytf.server.dao.NodeMapper;
import cn.com.cnhic.hy.htraced.ytf.server.dao.TransactionMapper;
import cn.com.cnhic.hy.htraced.ytf.server.model.Account;
import cn.com.cnhic.hy.htraced.ytf.server.model.Block;
import cn.com.cnhic.hy.htraced.ytf.server.model.BlockchainTransaction;
import cn.com.cnhic.hy.htraced.ytf.server.model.Node;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.tx.gas.DefaultGasProvider;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class BlockChainService {
    @Resource
    AccountMapper accountMapper;
    @Resource
   BlockMapper blockMapper;
    @Resource
    NodeMapper nodeMapper;
    @Resource
    TransactionMapper transactionMapper;
    @Resource
    EthService ethService;

    @Resource
    Web3j web3j;
    @Resource
    Admin admin;


    /**
     * <b>功能描述：</b>生成私钥<br>
     * <b>修订记录：</b><br>
     * <li>20191014&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public String createPrivateKey() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        ECKeyPair ecKeyPair= Keys.createEcKeyPair();
        return ecKeyPair.getPrivateKey().toString(16);
    }
    /**
     * 创建账户
     * @param pwd
     * @return
     */
    public String newAccount(String pwd) throws IOException, CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        //创建账户
        Request<?, NewAccountIdentifier> request=admin.personalNewAccount(pwd);
        String accountId=request.send().getAccountId();
        return "0x"+accountId;

    }
    /**
     * <b>功能描述：</b>导入私钥创建钱包账户<br>
     * <b>修订记录：</b><br>
     * <li>20191014&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public String newAccountImportPrivateKey(String pwd,String privateKey) throws Exception {
        //指定钱包文件所在地址
        File file=new File("E:\\Geth\\data\\keystore");
        BigInteger bigInteger=new BigInteger(privateKey,16);
        ECKeyPair ecKeyPair=ECKeyPair.create(bigInteger);
        System.out.println("账户地址 =>"+Keys.getAddress(ecKeyPair));
        String walletFile = WalletUtils.generateWalletFile(pwd, ecKeyPair, file, true);
        System.out.println("钱包文件 =>"+file+walletFile);
        //保存账户信息到数据库
        Account account=new Account();
        account.setAddress(Keys.getAddress(ecKeyPair));
        account.setBalance("0");
        account.setPrivateKey(privateKey);
        account.setPublicKey(ecKeyPair.getPublicKey().toString(16));
        account.setPwd(pwd);
        account.setTxNumber(0);
        accountMapper.insertAccount(account);
        return "0x"+Keys.getAddress(ecKeyPair);
    }

    /**
     * 解锁账户
     * @param formAddr
     * @param pwd
     * @return
     * @throws IOException
     */
    public Boolean unlock(String formAddr,String pwd) throws IOException {
        PersonalUnlockAccount personalUnlockAccount=admin.personalUnlockAccount(formAddr,pwd).send();
        if (personalUnlockAccount.accountUnlocked()){
            return true;
        }
        return false;
    }
    /**
     * 普通交易
     * @param trx
     * @return
     * @throws IOException
     */
    public BlockchainTransaction process(BlockchainTransaction trx) throws Exception {
        //获取当前区块当前账户的交易数量
        EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(trx.getFromAddr(), DefaultBlockParameterName.LATEST).send();
        //将交易备注转为16进制
        String a= HexUtils.toHexString(trx.getInput().getBytes("utf-8"));
        BigInteger gasLimit=DefaultGasProvider.GAS_LIMIT;
        BigInteger gasPrice =DefaultGasProvider.GAS_PRICE;
        //创建交易
        org.web3j.protocol.core.methods.request.Transaction transaction =
                org.web3j.protocol.core.methods.request.Transaction.createFunctionCallTransaction(trx.getFromAddr(), transactionCount.getTransactionCount(), gasPrice,
                        gasLimit, trx.getToAddr(),BigInteger.valueOf(trx.getValue()),a);
        EthSendTransaction response = web3j.ethSendTransaction(transaction).send();
        if (response.getError() != null) {
            trx.setAccepted(false);
            System.err.println(response.getError().getMessage());
            return trx;
        }
        trx.setAccepted(true);
        String txHash = response.getTransactionHash();
        System.out.println("Tx hash: {}"+ txHash);
        trx.setId(txHash);
        EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(txHash).send();
        if (receipt.getTransactionReceipt().isPresent()) {
            System.out.println("Tx receipt: {}"+receipt.getTransactionReceipt().get().getCumulativeGasUsed().intValue());
        }
        return trx;
    }
    /**
     * <b>功能描述：</b>裸交易<br>
     * <b>修订记录：</b><br>
     * <li>20191014&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */


    /**
     * 获取当前高度
     * @return
     * @throws IOException
     */
    public Long getHeight() throws IOException {
        EthBlockNumber ethBlockNumber=web3j.ethBlockNumber().send();
        Long height=ethBlockNumber.getBlockNumber().longValue();
        return height;
    }

    /**
     * 获取账户的Nonce
     * @param addr
     * @return
     * @throws IOException
     */
    public BigInteger getNonce(String addr) throws IOException {
        EthGetTransactionCount getNonce=web3j.ethGetTransactionCount(addr,DefaultBlockParameterName.PENDING).send();
        return getNonce.getTransactionCount();
    }

    /**
     * 查询余额
     * @param addr
     * @return
     * @throws IOException
     */
    public String getBalance(String addr) throws IOException {
        //获取指定钱包的以太币余额
        BigInteger integer=web3j.ethGetBalance(addr,DefaultBlockParameterName.LATEST).send().getBalance();
        //eth默认会部18个0这里处理比较随意
        String decimal = toDecimal(18,integer);
        System.out.println(decimal);
        return decimal;
    }

    /**
     * 根据交易hash获取交易信息
     * @param txHash
     * @return
     * @throws IOException
     */
    public cn.com.cnhic.hy.htraced.ytf.server.model.Transaction getTransaction(String txHash) throws Exception {
        /*Request<?, EthTransaction> request=web3j.ethGetTransactionByHash(txHash);
       Optional<org.web3j.protocol.core.methods.response.Transaction> transaction= request.send().getTransaction();
        Transaction tra=new Transaction();
        BeanUtils.copyProperties(transaction,tra);
       return tra;*/
        return transactionMapper.getTransactionByHash(txHash);
    }
    /**
     * <b>功能描述：</b>根据当前区块高度获取高度信息<br>
     * <b>修订记录：</b><br>
     * <li>20191010&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public EthBlock getBlockList() throws IOException {
        DefaultBlockParameter defaultBlockParameter=new DefaultBlockParameterNumber(getHeight());
        Request<?, EthBlock> request=web3j.ethGetBlockByNumber(defaultBlockParameter,true);
        EthBlock ethBlock=request.send();
        return ethBlock;
    }
    /**
     * <b>功能描述：</b>获取区块列表<br>
     * <b>修订记录：</b><br>
     * <li>20191017&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public List<Block> getBlocks() throws Exception {
        return blockMapper.getBlockByMap(null);
    }
    /**
     * <b>功能描述：</b>根据区块HASH查询区块信息<br>
     * <b>修订记录：</b><br>
     * <li>20191010&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public Block getBlockByHash(String blockHash) throws Exception {
      /* Request<?,EthBlock> request=web3j.ethGetBlockByHash(blockHash,true);
       EthBlock ethBlock=request.send();
       return ethBlock;*/
        Map<String,Object> map=new HashMap<>();
        map.put("hash",blockHash);
        return blockMapper.getBlockByMap(map).get(0);
    }
    /**
     * <b>功能描述：</b>根据区块HASH查询区块中的交易列表<br>
     * <b>修订记录：</b><br>(数据库中查询)
     * <li>20191010&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public List<cn.com.cnhic.hy.htraced.ytf.server.model.Transaction> getBlockTransactionByHash(String blockHash) throws Exception {
       /* //根据当前区块hash获取交易数量
        Integer count=web3j.ethGetBlockTransactionCountByHash(blockHash).send().getTransactionCount().intValue();
        //根据当前区块hash和下标获取交易信息
        List<EthTransaction> ethTransactions=new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Request<?, EthTransaction> request=web3j.ethGetTransactionByBlockHashAndIndex(blockHash,BigInteger.valueOf(i));
            EthTransaction etTransaction=request.send();
            ethTransactions.add(etTransaction);
        }
        return ethTransactions;*/
       Map<String,Object> map=new HashMap<>();
        map.put("blockHash",blockHash);
        return transactionMapper.getTransactionByMap(map);
    }
    /**
     * <b>功能描述：</b>根据区块Number查询区块信息<br>
     * <b>修订记录：</b><br>
     * <li>20191010&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public Block getInfoByNumber(Long number) throws Exception {
       /* DefaultBlockParameter defaultBlockParameter=new DefaultBlockParameterNumber(number);
        Request<?, EthBlock> request=web3j.ethGetBlockByNumber(defaultBlockParameter,true);
        EthBlock ethBlock=request.send();
        return ethBlock;*/
       Map<String,Object> map=new HashMap<>();
       map.put("number",number);
       return blockMapper.getBlockByMap(map).get(0);
    }
    /**
     * <b>功能描述：</b>根据区块Number查询区块中的交易列表<br>
     * <b>修订记录：</b><br>（数据库中查询）
     * <li>20191010&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public List<cn.com.cnhic.hy.htraced.ytf.server.model.Transaction> getBlockTransactionByNumber(Long number) throws Exception {
        /*DefaultBlockParameter defaultBlockParameter=new DefaultBlockParameterNumber(number);
        //获取当前高度下交易数量
        Request<?, EthGetBlockTransactionCountByNumber> request=web3j.ethGetBlockTransactionCountByNumber(defaultBlockParameter);
        EthGetBlockTransactionCountByNumber ethGetBlockTransactionCountByNumber=request.send();
        Integer count=ethGetBlockTransactionCountByNumber.getTransactionCount().intValue();
        //根据当前高度和下标获取交易
        List<EthTransaction> transactions=new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Request<?, EthTransaction> request1=web3j.ethGetTransactionByBlockNumberAndIndex(defaultBlockParameter,BigInteger.valueOf(i));
           EthTransaction ethTransaction=request1.send();
           transactions.add(ethTransaction);
        }
        return transactions;*/
        Map<String,Object> map=new HashMap<>();
        map.put("blockNumber",number);
        return transactionMapper.getTransactionByMap(map);
    }
    /**
     * <b>功能描述：</b>获取账号地址列表<br>
     * <b>修订记录：</b><br>
     * <li>20191010&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public List<Account> getAccounts() throws Exception {

        List<Account> accounts1=accountMapper.getAccountByMap(null);

        return accounts1;
    }
    /**
     * <b>功能描述：</b>根据账号地址获取账号信息<br>
     * <b>修订记录：</b><br>
     * <li>20191010&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public Account getAccountInfo(String addr) throws IOException {
        Account account =new Account();
        account.setAddress(addr);
        //获取余额
        String balance=getBalance(addr);
        //获取交易数量
       Integer count=web3j.ethGetTransactionCount(addr,DefaultBlockParameterName.LATEST).send().getTransactionCount().intValue();
       account.setBalance(balance);
       account.setTxNumber(count);
       return account;
    }
    /**
     * <b>功能描述：</b>获取交易列表<br>
     * <b>修订记录：</b><br> 备注：所有交易信息可在完成交易时保存在mysql中。
     * <li>20191010&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public List<cn.com.cnhic.hy.htraced.ytf.server.model.Transaction> getTransactionList() throws Exception {

        return transactionMapper.getTransactionByMap(null);
    }
    /**
     * <b>功能描述：</b>根据账号地址获取账号交易记录<br>
     * <b>修订记录：</b>在数据库中根据账户地址查询交易记录<br>
     * <li>20191011&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public List<cn.com.cnhic.hy.htraced.ytf.server.model.Transaction> getTransactionByAddr(String addr) throws Exception {
        return transactionMapper.getTransactionByAddr(addr);
    }
    /**
     * <b>功能描述：</b>获取交易收据<br>
     * <b>修订记录：</b><br>
     * <li>20191011&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public cn.com.cnhic.hy.htraced.ytf.server.model.TransactionReceipt getTransactionReceipt(String hash) throws IOException {
        Optional<TransactionReceipt> transactionReceipt=web3j.ethGetTransactionReceipt(hash).send().getTransactionReceipt();
        cn.com.cnhic.hy.htraced.ytf.server.model.TransactionReceipt receipt=new cn.com.cnhic.hy.htraced.ytf.server.model.TransactionReceipt();
        BeanUtils.copyProperties(transactionReceipt,receipt);
        return receipt;
    }
    /**
     * <b>功能描述：</b>初始化首页数据<br>
     * <b>修订记录：</b><br>
     * <li>20191016&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public Map initData() throws Exception {
        Map<String,Object> map=new HashMap<>();
        //获取节点总数
        Integer nodeCount=nodeMapper.getNodeCountByMap(null);
        //获取当前区块高度
        Long height = getHeight();
        //获取交易总数
        Integer txCount=transactionMapper.getTransactionCountByMap(null);
        //获取账户数
        Integer accountCount=accountMapper.getAccountCountByMap(null);
        //获取流通量
        String sum=transactionMapper.getSumValue();
        map.put("节点总数",nodeCount);
        map.put("当前区块高度",height);
        map.put("交易总数",txCount);
        map.put("账户数",accountCount);
        map.put("流通量",sum);
        return map;
    }
    /**
     * <b>功能描述：</b>获取节点列表<br>
     * <b>修订记录：</b><br>
     * <li>20191016&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public List<Node> getNodeList(Integer pagenum, Integer type, String area) throws Exception {
        Map<String ,Object> map=new HashMap<>();
        map.put("type",type);
        map.put("area",area);
        map.put("beginPos",pagenum-1);
        map.put("pageSize",10);
        return nodeMapper.getNodeByMap(map);
    }
    /**
     * <b>功能描述：</b>判断参数类型<br>
     * <b>修订记录：</b><br>
     * <li>20191016&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public String search(String string) throws Exception {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        Block block=blockMapper.getBlockByHash(string);
        cn.com.cnhic.hy.htraced.ytf.server.model.Transaction transaction=transactionMapper.getTransactionByHash(string);
        if (pattern.matcher(string).matches()){
            return "区块号";
        }else if (string.toCharArray().length==42){
            return "地址";
        }else if (block!=null){
            return "区块hash";
        }else if (transaction!=null){
            return "交易hash";
        }
        return "未找到相关参数";
    }
    /**
     * <b>功能描述：</b>获取节点地区列表<br>
     * <b>修订记录：</b><br>
     * <li>20191017&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public List<String> getAreaList() throws Exception {
        return nodeMapper.getAreas();
    }


    /**
     * 转换成符合 decimal 的数值
     * @param decimal
     * @param
     * @return
     */
    public static String toDecimal(int decimal,BigInteger integer){
//		String substring = str.substring(str.length() - decimal);
        StringBuffer sbf = new StringBuffer("1");
        for (int i = 0; i < decimal; i++) {
            sbf.append("0");
        }
        String balance = new BigDecimal(integer).divide(new BigDecimal(sbf.toString()), 18, BigDecimal.ROUND_DOWN).toPlainString();
        return balance;
    }
}

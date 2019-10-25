package cn.com.cnhic.hy.htraced.ytf.server.service;



import cn.com.cnhic.hy.htraced.ytf.server.dao.AccountMapper;
import cn.com.cnhic.hy.htraced.ytf.server.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.Optional;

/**
 * <b>功能：</b>web3j练习类<br>
 * <b>Copyright ZJHY</b>
 * <ul>
 * <li>版本&nbsp;&nbsp;&nbsp;&nbsp;修改日期&nbsp;&nbsp;&nbsp;&nbsp;部　　门&nbsp;&nbsp;&nbsp;&nbsp;作　者&nbsp;&nbsp;&nbsp;&nbsp;变更内容</li>
 * <hr>
 * <li>v1.0&nbsp;&nbsp;&nbsp;&nbsp;20191012&nbsp;&nbsp;技术中心&nbsp;&nbsp;&nbsp;&nbsp;王恒&nbsp;&nbsp;&nbsp;&nbsp;创建类</li>
 * </ul>
 */
@Service
public class EthService {
    @Resource
    Web3j web3j ;
    @Resource
    Admin admin ;
    @Resource
    AccountMapper accountMapper;

    /**
     * <b>功能描述：</b>获取节点版本信息<br>
     * <b>修订记录：</b><br>
     * <li>20191012&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public String getClientVersion() throws IOException {
        String clientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
        //return clientVersion.substring(clientVersion.indexOf("/")+1,clientVersion.indexOf("v")-1);
        return clientVersion;
    }

/******************************账户管理*******************************/

    /**
     * <b>功能描述：</b>创建秘钥对<br>
     * <b>修订记录：</b><br>
     * <li>20191012&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public String[] getAccountTuple() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        String[] strings = {ecKeyPair.getPrivateKey().toString(16), ecKeyPair.getPublicKey().toString(16), Keys.getAddress(ecKeyPair)};
        return strings;
    }

    /**
     * <b>功能描述：</b>根据私钥获取公钥和地址<br>
     * <b>修订记录：</b><br>
     * <li>20191012&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public String[] importPrivateKey(String privateKey) {
        BigInteger bigInteger = new BigInteger(privateKey, 16);
        //根据私钥创建秘钥对 对象
        ECKeyPair ecKeyPair = ECKeyPair.create(bigInteger);
        String addr = Keys.getAddress(ecKeyPair);
        String publicKey = ecKeyPair.getPublicKey().toString(16);
        return new String[]{addr, publicKey};
    }

    /**
     * <b>功能描述：</b>使用密码创建钱包文件<br>
     * <b>修订记录：</b><br>
     * <li>20191012&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public String newWalletFile(String pwd) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CipherException, IOException {
        File dir = new File("E:\\Geth\\data\\keystore");
        String walletFileName = WalletUtils.generateNewWalletFile(pwd, dir, true);
        return walletFileName;
    }

    /**
     * <b>功能描述：</b>根据密码和私钥生成账户<br>
     * <b>修订记录：</b><br>
     * <li>20191012&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public String newWalletFileByPrivateKey(String pwd, String privateKey) throws CipherException, IOException {
        File dir = new File("E:\\Geth\\data\\keystore");
        BigInteger bigInteger = new BigInteger(privateKey, 16);
        ECKeyPair ecKeyPair = ECKeyPair.create(bigInteger);
        System.out.println("账户地址：" + Keys.getAddress(ecKeyPair));
        String walletFile = WalletUtils.generateWalletFile(pwd, ecKeyPair, dir, true);
        return walletFile;
    }

    /**
     * <b>功能描述：</b>加载钱包，返回凭证，获取秘钥对和地址<br>
     * <b>修订记录：</b><br>
     * <li>20191012&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public String[] loadWalletFile(String pwd, String src) throws IOException, CipherException {
        Credentials credentials = WalletUtils.loadCredentials(pwd, src);
        ECKeyPair ecKeyPair = credentials.getEcKeyPair();
        return new String[]{ecKeyPair.getPrivateKey().toString(16), ecKeyPair.getPublicKey().toString(16), Keys.getAddress(ecKeyPair)};
    }

    /**
     * <b>功能描述：</b>获取节点管理账户列表<br>
     * <b>修订记录：</b><br>
     * <li>20191012&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public List<String> getNodeAccounts() throws IOException {
        List<String> accounts = web3j.ethAccounts().send().getAccounts();
        return accounts;
    }

    /**
     * <b>功能描述：</b>根据账户地址查看最新余额<br>
     * <b>修订记录：</b><br>
     * <li>20191012&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public String getBalance(String addr) throws IOException {
        return web3j.ethGetBalance(addr, DefaultBlockParameterName.LATEST).send().getBalance().toString();
    }

    /**
     * <b>功能描述：</b>根据账户地址查看交易数<br>
     * <b>修订记录：</b><br>
     * <li>20191015&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public Integer getTxCount(String addr) throws IOException {
        return web3j.ethGetTransactionCount(addr, DefaultBlockParameterName.LATEST).send().getTransactionCount().intValue();
    }

    /**
     * <b>功能描述：</b>Wei转为Ether<br>
     * <b>修订记录：</b><br>
     * <li>20191012&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public BigDecimal toEther(String value) {
        return Convert.fromWei(value, Convert.Unit.ETHER);
    }

    /**
     * <b>功能描述：</b>Ether转为Wei<br>
     * <b>修订记录：</b><br>
     * <li>20191012&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public BigInteger toWei(String value) {
        return new BigInteger(String.valueOf(Convert.toWei(value, Convert.Unit.ETHER)));
    }

    /**
     * <b>功能描述：</b>普通交易<br>
     * <b>修订记录：</b><br>
     * <li>20191012&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public String sendTransaction(String fromAddr, String toAddr, String value, String pwd) throws IOException {
        BigInteger gasPrice = null;
        BigInteger gasLimit = null;
        BigInteger nonce = null;
        String data = null;
        //创建交易
        Transaction transaction = new Transaction(fromAddr, nonce, gasPrice, gasLimit, toAddr, toWei(value), data);
        //解锁账户
        if (admin.personalUnlockAccount(fromAddr, pwd).send().accountUnlocked()) {
            System.out.println(web3j.ethSendTransaction(transaction).send().getTransactionHash());
            return web3j.ethSendTransaction(transaction).send().getTransactionHash();
        }
        return null;
    }

    /**
     * <b>功能描述：</b>等待交易收据<br>
     * <b>修订记录：</b><br>
     * <li>20191012&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    private TransactionReceipt waitForTransaction(String txHash, Long timeout) throws IOException, InterruptedException {
        System.out.println("等待交易收据中");
        Long t0 = System.currentTimeMillis();
        Optional<TransactionReceipt> receipt = null;
        while (true) {
            receipt = web3j.ethGetTransactionReceipt(txHash).send().getTransactionReceipt();
            if (receipt.isPresent()) {
                System.out.println("获取到交易收据");
                return receipt.get();
            }
            Long t1 = System.currentTimeMillis();
            if ((t1 - t0) > timeout) {
                System.out.println("超时");
                return null;
            }
            Thread.sleep(100);
        }
    }

    /**
     * <b>功能描述：</b>普通交易带交易收据<br>
     * <b>修订记录：</b><br>
     * <li>20191012&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public void transactionWithReceipt(String fromAddr, String toAddr, String value, String pwd) throws IOException, InterruptedException {
        //发送交易
        String txHash = sendTransaction(fromAddr, toAddr, value, pwd);
        TransactionReceipt receipt = waitForTransaction(txHash, 2 * 1000l);
        System.out.println("tx receipt =>");
        System.out.println("tx hash: " + receipt.getTransactionHash());
        System.out.println("tx index: " + receipt.getTransactionIndex());
        System.out.println("block hash: " + receipt.getBlockHash());
        System.out.println("block number: " + receipt.getBlockNumber());
        System.out.println("cumulativeGasUsed: " + receipt.getCumulativeGasUsed());
        System.out.println("gas used: " + receipt.getGasUsed());
        System.out.println("contractAddress: " + receipt.getContractAddress());
        System.out.println("root: " + receipt.getRoot());
        System.out.println("status: " + receipt.getStatus());
        System.out.println("from: " + receipt.getFrom());
        System.out.println("to: " + receipt.getTo());
        System.out.println("logs: " + receipt.getLogs());
        System.out.println("logsBloom: " + receipt.getLogsBloom());
    }

    /**
     * <b>功能描述：</b>节点账户向钱包账户转账<br>
     * <b>修订记录：</b><br>
     * <li>20191014&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public void transferToWallet(String toPwd, String src, String fromPwd) throws IOException, CipherException, InterruptedException {
        //读取keystore,获取凭证
        Credentials credentials = WalletUtils.loadCredentials(toPwd, src);
        //获取节点账户
        List<String> accounts = web3j.ethAccounts().send().getAccounts();
        //获取转账,收账地址
        String from = accounts.get(1);
        String to = credentials.getAddress();
        String value = "10";
        //发起普通交易并获取交易收据
        transactionWithReceipt(from, to, value, fromPwd);
    }

    /**
     * <b>功能描述：</b>发起裸交易<br>
     * <b>修订记录：</b><br>
     * <li>20191014&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public void rawTransaction(String pwd, String src, String value, String toAddr) throws IOException, CipherException, InterruptedException {
        //加载钱包文件，获取凭证
        Credentials credentials = WalletUtils.loadCredentials(pwd, src);
        BigInteger gasPrice = DefaultGasProvider.GAS_PRICE;
        BigInteger gasLimit = DefaultGasProvider.GAS_LIMIT;
        BigInteger va = Convert.toWei(value, Convert.Unit.ETHER).toBigInteger();
        String data = "7878";
        BigInteger nonce = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount();
        //创建裸交易
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, toAddr, va, data);
        //使用发送凭证签名
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        //将签名转为HexString(是将字节数组转换为带0x前缀的16进制字符串)
        String hexValue = Numeric.toHexString(signMessage);
        //发送裸交易
        String transactionHash = web3j.ethSendRawTransaction(hexValue).send().getTransactionHash();
        System.out.println("裸交易hash：" + transactionHash);
        //获取裸交易收据
        TransactionReceipt receipt = waitForTransaction(transactionHash, 2 * 1000l);
        System.out.println("tx receipt =>" + receipt);
    }

    /**
     * <b>功能描述：</b>受控交易和交易管理器 1. 普通交易<br>
     * <b>修订记录：</b><br>
     * <li>20191014&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public void transferMoney(String fromAddr, String toAddr, String value, String pwd) throws Exception {
        //解锁发送账户
        if (admin.personalUnlockAccount(fromAddr, pwd).send().accountUnlocked()) {
            //创建交易管理器，使用其构造函数传入发送方
            //ClientTransactionManager clientTransactionManager=new ClientTransactionManager(web3j,fromAddr);
            //调节交易管理器检查周期
            int attempts = 10;//一周期检查10次
            int sleepDuration = 1000;//1秒为一周期
            ClientTransactionManager clientTransactionManager = new ClientTransactionManager(web3j, fromAddr, attempts, sleepDuration);
            //创建受控交易
            Transfer transfer = new Transfer(web3j, clientTransactionManager);
            //使用sendFunds()提交交易
            TransactionReceipt receipt = transfer.sendFunds(toAddr, BigDecimal.valueOf(Long.valueOf(value)), Convert.Unit.ETHER).send();
            System.out.println("交易收据 =>" + receipt);
        }
    }

    /**
     * <b>功能描述：</b>裸交易管理器<br>
     * <b>修订记录：</b><br>
     * <li>20191022&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public void rawTransferMoney(String pwd, String src, String toAddr, String value) throws Exception {
        //创建裸交易管理器，使用构造函数传入发送方凭证
        Credentials credentials = WalletUtils.loadCredentials(pwd, src);
        RawTransactionManager rawTransactionManager = new RawTransactionManager(web3j, credentials);
        //创建受控交易
        Transfer transfer = new Transfer(web3j, rawTransactionManager);
        //使用sendFunds()提交交易
        TransactionReceipt receipt = transfer.sendFunds(toAddr, BigDecimal.valueOf(Long.valueOf(value)), Convert.Unit.ETHER).send();
        System.out.println("交易收据 :" + receipt);
    }


    /**
     * <b>功能描述：</b>第一次连接节点时，给账户表新增账户<br>
     * <b>修订记录：</b><br>
     * <li>20191022&nbsp;&nbsp;|&nbsp;&nbsp;王恒&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public void addAccount() throws Exception {
        List<String> accounts = web3j.ethAccounts().send().getAccounts();
        for (String s : accounts) {
            Account account = new Account();
            EthGetBalance balance = web3j.ethGetBalance(s, DefaultBlockParameterName.LATEST).send();
            int transactionCount = web3j.ethGetTransactionCount(s, DefaultBlockParameterName.LATEST).send().getTransactionCount().intValue();
            account.setAddress(s);
            account.setBalance(toEther(balance.getBalance().toString()).toString());
            account.setTxNumber(transactionCount);
            accountMapper.insertAccount(account);
        }
    }

}

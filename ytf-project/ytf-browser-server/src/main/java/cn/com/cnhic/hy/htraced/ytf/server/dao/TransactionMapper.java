package cn.com.cnhic.hy.htraced.ytf.server.dao;



import cn.com.cnhic.hy.htraced.ytf.server.model.Transaction;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @program: cn.hy.ytf.dao
 * @description: 交易信息操作接口
 * @author: 王 恒
 * @create: 2019-10-15
 */
public interface TransactionMapper {
    public Transaction getTransactionById(Integer id) throws Exception;
    public List<Transaction> getTransactionByMap(Map<String, Object> param) throws Exception;
    public Integer getTransactionCountByMap(Map<String, Object> param) throws Exception;
    public Integer insertTransaction(Transaction transaction) throws Exception;
    public Integer updateTransaction(Transaction transaction) throws Exception;
    public Integer deleteTransactionById(@Param(value = "id") Integer id) throws Exception;
    public String getSumValue() throws Exception;
    public Transaction getTransactionByHash(@Param(value = "hash")String hash) throws Exception;
    public List<Transaction> getTransactionByAddr(@Param(value = "address") String address) throws Exception;
}

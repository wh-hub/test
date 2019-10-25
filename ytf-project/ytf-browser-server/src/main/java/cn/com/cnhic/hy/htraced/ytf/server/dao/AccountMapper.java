package cn.com.cnhic.hy.htraced.ytf.server.dao;


import cn.com.cnhic.hy.htraced.ytf.server.model.Account;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

/**
 * @program: cn.hy.ytf
 * @description: 账户操作接口
 * @author: 王 恒
 * @create: 2019-10-15
 */
public interface AccountMapper {
    public Account getAccountById(Integer id) throws Exception;
    public List<Account> getAccountByMap(Map<String, Object> param) throws Exception;
    public Integer getAccountCountByMap(Map<String, Object> param) throws Exception;
    public Integer insertAccount(Account account) throws Exception;
    public Integer updateAccount(Account account) throws Exception;
    public Integer deleteAccountById(@Param(value = "id") Integer id) throws Exception;
    public Account getAccountByAddress(@Param(value = "address")String address)throws Exception;
}

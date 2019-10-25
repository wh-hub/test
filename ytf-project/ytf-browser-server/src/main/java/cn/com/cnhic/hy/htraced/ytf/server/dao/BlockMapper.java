package cn.com.cnhic.hy.htraced.ytf.server.dao;



import cn.com.cnhic.hy.htraced.ytf.server.model.Block;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @program: cn.hy.ytf.dao
 * @description: 区块信息操作接口
 * @author: 王 恒
 * @create: 2019-10-15
 */
public interface BlockMapper {
    public Block getBlockById(Integer id) throws Exception;
    public List<Block> getBlockByMap(Map<String, Object> param) throws Exception;
    public Integer getBlockCountByMap(Map<String, Object> param) throws Exception;
    public Integer insertBlock(Block block) throws Exception;
    public Integer updateBlock(Block block) throws Exception;
    public Integer deleteBlockById(@Param(value = "id") Integer id) throws Exception;
    public Block getBlockByHash(@Param(value = "hash") String hash)throws Exception;
}

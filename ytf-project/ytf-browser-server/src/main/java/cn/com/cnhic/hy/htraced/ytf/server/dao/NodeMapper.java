package cn.com.cnhic.hy.htraced.ytf.server.dao;



import cn.com.cnhic.hy.htraced.ytf.server.model.Node;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @program: cn.hy.ytf.dao
 * @description: 节点信息操作接口
 * @author: 王 恒
 * @create: 2019-10-15
 */
public interface NodeMapper {
    public Node getNodeById(Integer id) throws Exception;
    public List<Node> getNodeByMap(Map<String, Object> param) throws Exception;
    public Integer getNodeCountByMap(Map<String, Object> param) throws Exception;
    public Integer insertNode(Node node) throws Exception;
    public Integer updateNode(Node node) throws Exception;
    public Integer deleteNodeById(@Param(value = "id") Integer id) throws Exception;
    public Node getNodeByAddr(@Param(value = "address") String address) throws Exception;
    public List<String> getAreas() throws Exception;
}

package cn.com.cnhic.hy.htraced.ytf.server.dao;



import cn.com.cnhic.hy.htraced.ytf.server.model.Source;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @program: cn.hy.ytf
 * @description: 账户操作接口
 * @author: 王 恒
 * @create: 2019-10-15
 */
public interface SourceMapper {
    public Source getSourceById(Integer id) throws Exception;
    public List<Source> getSourceByMap(Map<String, Object> param) throws Exception;
    public Integer getSourceCountByMap(Map<String, Object> param) throws Exception;
    public Integer insertSource(Source source) throws Exception;
    public Integer deleteSourceById(@Param(value = "id") Integer id) throws Exception;

}

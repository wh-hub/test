package cn.com.cnhic.hy.htraced.ytf.entity;

/**
 * @program: cn.hy.ytf.model
 * @description: 溯源信息
 * @author: 王 恒
 * @create: 2019-10-22
 */
public class Source {
    private Integer id;
    private String context;
    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

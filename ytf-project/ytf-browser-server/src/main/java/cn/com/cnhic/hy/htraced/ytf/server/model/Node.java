package cn.com.cnhic.hy.htraced.ytf.server.model;

/**
 * @program: cn.hy.ytf.model
 * @description: 节点信息类
 * @author: 王 恒
 * @create: 2019-10-15
 */
public class Node {
    private Integer id;
    private String name;
    private Integer type;
    private String address;
    private String area;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}

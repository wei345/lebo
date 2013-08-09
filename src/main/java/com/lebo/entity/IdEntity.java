package com.lebo.entity;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 统一定义id的entity基类.
 * <p/>
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.
 *
 * @author liuwei
 */
@MappedSuperclass
public abstract class IdEntity implements Serializable {

    protected String id;
    public static final String ID_KEY = "id";

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

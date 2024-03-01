package com.roukaixin.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 统一结果返回
 *
 * @author 不北咪
 * @date 2023/3/16 20:30
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class R<T> {

    /**
     * 返回状态码
     */
    private Integer status;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;


    public static <T> R<T> ok(){
        R<T> t = new R<T>();
        t.setStatus(200);
        t.setMessage("操作成功");
        return t;
    }

    public static <T> R<T> ok(String message){
        R<T> t = new R<T>();
        t.setStatus(200);
        t.setMessage(message);
        return t;
    }

    public static <T> R<T> ok(T data){
        R<T> t = new R<>();
        t.setStatus(200);
        t.setMessage("操作成功");
        t.setData(data);
        return t;
    }

    public static <T> R<T> ok(String message,T data){
        R<T> t = new R<>();
        t.setStatus(200);
        t.setMessage(message);
        t.setData(data);
        return t;
    }
}

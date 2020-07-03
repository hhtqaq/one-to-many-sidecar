package com.ecjtu.hht.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hht
 * @date 2019/11/26 17:32
 */
@Data
public class ResponseVo<T> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;

    //构建其他状态的ResponseVo对象
    public static ResponseVo<Object> build(Integer status, String msg, Object data) {
        return new ResponseVo<>(status, msg, data);
    }

    public static ResponseVo ok(Object data) {
        return new ResponseVo(data);
    }

    public static ResponseVo<Object> error(Integer status, String msg, Object data) {
        return new ResponseVo<>(status, msg, data);
    }

    public static ResponseVo ok() {
        return new ResponseVo(null);
    }

    private ResponseVo(Object data) {
        this.status=200;
        this.msg="success";
        this.data=data;
    }

    public static ResponseVo build(Integer status, String msg) {
        return new ResponseVo(status, msg, null);
    }

    private ResponseVo(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseVo{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

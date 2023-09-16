package org.example.user.dto;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/9/16
 **/
public class ResultDTO<T> {
    private String code;
    private String msg;
    private T data;

    public static <T> ResultDTO<T> success(T data) {
        ResultDTO<T> resultDTO = new ResultDTO<>();
        resultDTO.code = "200";
        resultDTO.msg = "success";
        resultDTO.data = data;
        return resultDTO;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

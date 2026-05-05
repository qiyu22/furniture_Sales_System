package com.furniture.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一响应结果类
 */
public class Result {
    private boolean success;
    private String message;
    private Object data;

    public Result() {
    }

    public Result(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static Result success() {
        return new Result(true, "操作成功", null);
    }

    public static Result success(Object data) {
        return new Result(true, "操作成功", data);
    }

    public static Result success(String message, Object data) {
        return new Result(true, message, data);
    }

    public static Result error() {
        return new Result(false, "操作失败", null);
    }

    public static Result error(String message) {
        return new Result(false, message, null);
    }

    public static Result error(String message, Object data) {
        return new Result(false, message, data);
    }

    // 分页数据封装
    public static Result page(Object list, int total) {
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("total", total);
        return new Result(true, "操作成功", data);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
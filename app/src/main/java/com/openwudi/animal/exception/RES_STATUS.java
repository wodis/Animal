package com.openwudi.animal.exception;

/**
 * Created by diwu on 16/12/1.
 */

public enum RES_STATUS {
    FAILED(-1,"未知错误"),
    SUCCESS(0, "SUCCESS"),
    PBC_TIMEOUT(98, "征信超时，请重新登录"),
    PBC_SHOW_DIALOG(99, "征信服务器异常"),

    RESP_ERROR(40000, "呃! 服务器出小差了"),

    JSON_PARSE_ERROR(40001, "呃! 服务器出小差了"),

    NO_CONNECTION_ERROR(50002, "哎唷, 好像没有网络哦~"),
    NETWORK_ERROR(50003, "您的网络可能有问题哦! "),
    TIMEOUT_ERROR(50004, "抱歉, 请求超时啦! "),
    CLIENT_ERROR(50005, "抱歉, 请求地址错误! "),
    SERVER_ERROR(50006, "抱歉, 服务器错误请稍后再试! "),
    AUTH_FAILURE_ERROR(50007, "认证失败! "),
    VOLLEY_ERROR(50008, "您的网络可能有问题哦! "),

    FILE_DOWN_ERROR(60000, "哎唷, 文件下载失败了~"),
    FILE_SAVE_ERROR(60001, "哎唷, 文件保存失败了~"),
    ;

    public final int code;
    public final String msg;

    RES_STATUS(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static RES_STATUS findStatusByCode(int code) {
        for (RES_STATUS status : RES_STATUS.values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}

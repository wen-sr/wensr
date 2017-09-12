package com.mmall.common;

import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.Set;

public class Constant {
    public static final String CURRENT_USER     = "currentUser";
    public static final String USERNAME         = "username";
    public static final String EMAIL            = "email";

    public interface Role{
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1;//管理员
    }

    public interface ProductOrderBy {
        Set<String> PRICE_ORDER_BY = Sets.newHashSet("price_asc", "price_desc");
    }

    public enum  productStatus {
        ON_SELL(1, "在售"),
        OUT_SELL(0, "下架")
        ;

        private int code;
        private String msg;

        productStatus(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}

package com.doin.execution.constant.db;

public class DbConstant {

    private DbConstant() {
    }

    public static class DbCommon {
        public static final String CREATED_AT = "created_at";
        public static final String UPDATED_AT = "updated_at";

        DbCommon() {
        }
    }

    public static class DbOrder extends DbCommon {
        public static final String TABLE_NAME = "orders";
    }
}
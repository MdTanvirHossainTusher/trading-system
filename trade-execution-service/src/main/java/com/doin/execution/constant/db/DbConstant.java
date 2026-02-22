package com.doin.signal.constant.db;

public class DbConstant {

    private DbConstant() {
    }

    public static class DbCommon {
        public static final String CREATED_AT = "created_at";
        public static final String UPDATED_AT = "updated_at";
        public static final String CREATED_BY = "created_by";
        public static final String UPDATED_BY = "updated_by";

        DbCommon() {
        }
    }

    public static class DbUser extends DbCommon {
        public static final String TABLE_NAME = "users";
    }

    public static class DbBrokerAccount extends DbCommon {
        public static final String TABLE_NAME = "broker_accounts";
    }

    public static class DbActivityLog extends DbCommon {
        public static final String TABLE_NAME = "activity_logs";
    }
}
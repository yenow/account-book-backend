package com.ysy.accountbook.global.config.jpa;

import org.hibernate.dialect.MariaDB103Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StringType;

public class MairaDbDialectConfig extends MariaDB103Dialect {

    public MairaDbDialectConfig() {
        super();
        this.registerFunction("convert_keyword",
                              new StandardSQLFunction("convert_keyword",
                                                      new StringType()));
    }
}

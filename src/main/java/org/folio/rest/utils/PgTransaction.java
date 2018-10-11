package org.folio.rest.utils;

import io.vertx.reactivex.ext.sql.SQLConnection;

public class PgTransaction<T> {
    public T entity;
    public SQLConnection sqlConnection;

    public PgTransaction(T entity) {
        this.entity = entity;
    }
}

package org.folio.rest.utils;

import io.reactivex.Single;
import io.vertx.core.Vertx;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.reactivex.ext.sql.SQLConnection;
import io.vertx.reactivex.ext.sql.SQLClient;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.cql.CQLWrapper;
import org.folio.rest.persist.interfaces.Results;

/**
 * Dummy PostgresClient
 */
public class PostgresClient {

  private final Vertx vertx;
  private final String tenantId;
  private SQLClient client;

  private PostgresClient(Vertx vertx, String tenantId) {
    this.tenantId = tenantId;
    this.vertx = vertx;

  }
  public static PostgresClient getInstance(Vertx vertx, String tenantId) {
    return new PostgresClient(vertx, tenantId);
  }

  public <T> Single<Results<T>> save(String tableName, String id, T entity) {
    return Single.just(new Results<>());
  }

  public <T> Single<Results<T>> get(String tableName, Class<T> clazz, String[] fields, CQLWrapper cql, boolean returnCount, boolean setId) {
    return Single.just(new Results<>());
  }

  public <T> Single<UpdateResult> update(String tableName, T entity, Criterion criterion, boolean returnUpdatedIds) {
    return Single.just(new UpdateResult());
  }

  public <T> Single<Results<T>> get(String tableName, Class<T> clazz, Criterion criterion, boolean returnCount, boolean setId) {
    return Single.just(new Results<>());
  }

  public <T> Single<Results<T>> get(SQLConnection connection, String tableName, Class<T> clazz, Criterion criterion, boolean returnCount, boolean setId) {
    return Single.just(new Results<>());
  }

  public Single<UpdateResult> delete(SQLConnection sqlConnection, String tableName, Criterion criterion) {
    return Single.just(new UpdateResult());
  }

  public <T> Single<Results<T>> save(SQLConnection connection, String tableName, T entity) {
    return Single.just(new Results<>());
  }

  public Single<SQLConnection> startTx() {
    return this.client.rxGetConnection();
  }

  public void rollbackTx(SQLConnection connection) {
    connection.close();
  }

  public void endTx(SQLConnection connection) {
    connection.close();
  }

}

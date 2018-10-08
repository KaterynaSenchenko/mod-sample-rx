package org.folio.rest.utils;

import io.reactivex.Single;
import io.vertx.core.Vertx;
import io.vertx.ext.sql.UpdateResult;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.cql.CQLWrapper;
import org.folio.rest.persist.interfaces.Results;

public class PostgresClient {

  private final Vertx vertx;
  private final String tenantId;

  private PostgresClient(Vertx vertx, String tenantId) {
    this.tenantId = tenantId;
    this.vertx = vertx;

  }
  public static PostgresClient getInstance(Vertx vertx, String tenantId) {
    return new PostgresClient(vertx, tenantId);
  }

  public <T> Single<Results<T>> save(String tableName, String id, Object entity) {
    return Single.just(new Results<>());
  }

  public <T> Single<Results<T>> get(String tableName, Class<T> clazz, String[] fields, CQLWrapper cql, boolean returnCount, boolean setId) {
    return Single.just(new Results<>());
  }

  public Single<UpdateResult> update(String tableName, Object entity, Criterion criterion, boolean returnUpdatedIds) {
    return Single.just(new UpdateResult());
  }

  public <T> Single<Results<T>> get(String tableName, Class<T> clazz, Criterion criterion, boolean returnCount, boolean setId) {
    return Single.just(new Results<>());
  }
}

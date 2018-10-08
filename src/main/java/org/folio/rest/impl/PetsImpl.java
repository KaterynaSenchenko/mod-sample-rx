package org.folio.rest.impl;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.ext.sql.UpdateResult;
import org.folio.rest.jaxrs.model.Pet;
import org.folio.rest.jaxrs.model.PetsCollection;
import org.folio.rest.jaxrs.resource.Pets;
import org.folio.rest.persist.Criteria.Criteria;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.interfaces.Results;
import org.folio.rest.utils.PgQuery;
import org.folio.rest.utils.PostgresClient;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PetsImpl implements Pets {

  private static final String PETS_TABLE_NAME = "pets";
  private static final String[] ALL_FIELDS = {"*"};

  private final PostgresClient pgClient;

  public PetsImpl(Vertx vertx, String tenantId) {
    this.pgClient = PostgresClient.getInstance(vertx, tenantId);
  }

  @Override
  public void postPets(String lang, Pet entity, Map<String, String> okapiHeaders, SingleObserver<Response> observer, Context vertxContext) {
    try {
      vertxContext.runOnContext(v -> {
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        savePet(entity)
          .flatMap(this::constructPostResponse)
          .subscribe(observer);
      });
    } catch (Exception e) {
      observer.onSuccess(PostPetsResponse.respond500WithTextPlain(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }
  }

  @Override
  public void getPets(String query, int offset, int limit, String lang, Map<String, String> okapiHeaders, SingleObserver<Response> observer, Context vertxContext) {
    try {
      PgQuery.PgQueryBuilder queryBuilder = new PgQuery.PgQueryBuilder(ALL_FIELDS, PETS_TABLE_NAME).query(query).offset(offset).limit(limit);
      runGetQuery(queryBuilder)
        .flatMap(this::constructGetResponse)
        .subscribe(observer);
    } catch (Exception e) {
      observer.onSuccess(GetPetsResponse.respond500WithTextPlain(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }
  }

  @Override
  public void putPetsById(String id, String lang, Pet entity, Map<String, String> okapiHeaders, SingleObserver<Response> observer, Context vertxContext) {
    try {
      vertxContext.runOnContext(v -> {
        entity.setId(id);
        updatePet(entity)
          .flatMap(this::constructPutResponse)
          .subscribe(observer);
      });
    } catch (Exception e) {
      observer.onSuccess(PutPetsByIdResponse.respond500WithTextPlain(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }
  }

  @Override
  public void getPetsById(String id, String lang, Map<String, String> okapiHeaders, SingleObserver<Response> observer, Context vertxContext) {
    try {
      vertxContext.runOnContext(v -> {
        runGetPetById(id)
          .flatMap(this::constructGetByIdResponse)
          .subscribe(observer);
      });
    } catch (Exception e) {
      observer.onSuccess(GetPetsByIdResponse.respond500WithTextPlain(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }
  }

  @Override
  public void deletePetsById(String id, String lang, Map<String, String> okapiHeaders, SingleObserver<Response> observer, Context vertxContext) {

  }

  private Single<Results<Pet>> savePet(Pet pet) {
    try {
      return pgClient.save(PETS_TABLE_NAME, pet.getId(), pet);
    } catch (Exception e) {
      return Single.error(e);
    }
  }

  private Single<Response> constructPostResponse(Results<Pet> result) {
    if (!result.getResults().isEmpty()) {
      return Single.just(PostPetsResponse.respond201WithApplicationJson(result.getResults().get(0), PostPetsResponse.headersFor201()));
    }
    return Single.just(PostPetsResponse.respond500WithTextPlain(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase()));
  }

  private Single<Results<Pet>> runGetQuery(PgQuery.PgQueryBuilder queryBuilder) {
    try {
      PgQuery query = queryBuilder.build();
      return pgClient.get(query.getTable(), Pet.class, query.getFields(), query.getCql(), true, false);
    } catch (Exception e) {
      return Single.error(e);
    }
  }

  private Single<Response> constructGetResponse(Results<Pet> results) {
    List<Pet> petsList = results.getResults();
    int totalRecords = petsList.size();
    PetsCollection petsCollection = new PetsCollection();
    petsCollection.setPets(petsList);
    petsCollection.setTotalRecords(totalRecords);
    return Single.just(GetPetsResponse.respond200WithApplicationJson(petsCollection));
  }

  private Single<UpdateResult> updatePet(Pet pet) {
    try {
      Criteria idCrit = constructCriteria("'id'", pet.getId());
      return pgClient.update(PETS_TABLE_NAME, pet, new Criterion(idCrit), true);
    } catch (Exception e) {
      return Single.error(e);
    }
  }

  private Single<Response> constructPutResponse(UpdateResult result) {
    if (result.getUpdated() == 0) {
      return Single.just(PutPetsByIdResponse.respond404WithTextPlain(Response.Status.NOT_FOUND.getReasonPhrase()));
    }
    return Single.just(PutPetsByIdResponse.respond204());
  }

  private Single<Results<Pet>> runGetPetById(String id) {
    try {
      Criteria idCrit = constructCriteria("'id'", id);
      return pgClient.get(PETS_TABLE_NAME, Pet.class, new Criterion(idCrit), true, false);
    } catch (Exception e) {
      return Single.error(e);
    }
  }

  private Single<Response> constructGetByIdResponse(Results<Pet> results) {
    if (results.getResults().isEmpty()) {
      return Single.just(GetPetsByIdResponse.respond404WithTextPlain(Response.Status.NOT_FOUND.getReasonPhrase()));
    }
    return Single.just(GetPetsByIdResponse.respond200WithApplicationJson(results.getResults().get(0)));
  }

  /**
   * Builds criteria by which db result is filtered
   *
   * @param jsonbField - json key name
   * @param value      - value corresponding to the key
   * @return - Criteria object
   */
  private Criteria constructCriteria(String jsonbField, String value) {
    Criteria criteria = new Criteria();
    criteria.addField(jsonbField);
    criteria.setOperation("=");
    criteria.setValue(value);
    return criteria;
  }

}

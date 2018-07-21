package com.example.demo.da;

import com.example.demo.da.entity.Actor;
import com.example.demo.da.mappers.ActorMapper;
import java.util.List;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ActorRepository {

  private ActorDao actorDao;

  @Autowired
  public void setJdbi(Jdbi jdbi) {
    this.actorDao = jdbi.onDemand(ActorDao.class);
  }

  public void create(Actor actor) {
    actorDao.create(actor);
  }

  public List<Actor> readAll() {
    return actorDao.readAll();
  }

  public Actor read(long actorId) {
    return actorDao.read(actorId);
  }


  @RegisterRowMapper(ActorMapper.class)
  public  interface ActorDao {

    @SqlUpdate("INSERT INTO DEMO.ACTOR (USERNAME, RESOURCES_REQUESTED, RECORD_VERSION, CREATED_BY, CREATED_TIMESTAMP) values (:username, :resourcesRequested, :recordVersion, :createdBy, :createdTimestamp)")
    @GetGeneratedKeys
    long create(@BindBean Actor actor);

    @SqlQuery("SELECT * FROM DEMO.ACTOR WHERE ACTOR_ID = :actorId")
    Actor read(@Bind("actorId") Long actorId);

    @SqlQuery("SELECT * from DEMO.ACTOR")
    List<Actor> readAll();

  }

}

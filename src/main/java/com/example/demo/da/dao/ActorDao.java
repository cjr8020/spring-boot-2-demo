package com.example.demo.da.dao;

import com.example.demo.da.entity.Actor;
import com.example.demo.da.mappers.ActorMapper;
import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

@RegisterRowMapper(ActorMapper.class)
@UseStringTemplateSqlLocator
public interface ActorDao {

  @SqlUpdate
  @GetGeneratedKeys
  long create(@BindBean Actor actor);

  @SqlQuery
  Actor read(@Bind("actorId") Long actorId);

  @SqlQuery
  List<Actor> readAll();

}

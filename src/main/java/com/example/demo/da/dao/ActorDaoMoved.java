package com.example.demo.da.dao;

import com.example.demo.da.entity.Actor;
import com.example.demo.da.mappers.ActorMapper;
import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

//@UseStringTemplateSqlLocator
@RegisterRowMapper(ActorMapper.class)
public interface ActorDaoMoved {

  @SqlQuery
  Actor read(@Bind("actorId") Long actorId);

  @SqlQuery("SELECT * from DEMO.ACTOR")
  List<Actor> readAll();

}

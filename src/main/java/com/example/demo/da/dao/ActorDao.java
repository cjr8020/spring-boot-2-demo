package com.example.demo.da.dao;

import com.example.demo.da.entity.Actor;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface ActorDao {

  @SqlQuery
  Actor read(@Bind("actorId") Long actorId);

}

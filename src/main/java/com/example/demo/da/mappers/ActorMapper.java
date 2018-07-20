package com.example.demo.da.mappers;

import com.example.demo.da.entity.Actor;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

/**
 * Maps ResultSet to Entity.
 */
public class ActorMapper implements RowMapper<Actor> {

  @Override
  public Actor map(ResultSet rs, StatementContext ctx) throws SQLException {
    Actor actor = new Actor();

    actor.setId(rs.getLong("ACTOR_ID"));
    actor.setUsername(rs.getString("USERNAME"));
    actor.setResourcesRequested(rs.getInt("RESOURCES_REQUESTED"));

    actor.setRecordVersion(rs.getInt("RECORD_VERSION"));

    actor.setCreatedBy(rs.getString("CREATED_BY"));
    actor.setCreatedTimestamp(
        rs.getTimestamp("CREATED_TIMESTAMP") == null ? null
            : rs.getTimestamp("CREATED_TIMESTAMP").toLocalDateTime()
    );
    actor.setUpdatedBy(rs.getString("UPDATED_BY"));
    actor.setUpdatedTimestamp(
        rs.getTimestamp("UPDATED_TIMESTAMP") == null ? null
            : rs.getTimestamp("UPDATED_TIMESTAMP").toLocalDateTime()
    );

    return actor;
  }

}
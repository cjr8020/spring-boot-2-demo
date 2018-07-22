package com.example.demo.rest;

import com.example.demo.da.ActorRepository;
import com.example.demo.da.entity.Actor;
import com.example.demo.exceptions.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActorRestResourceController {

  private static Logger logger = LoggerFactory.getLogger(ActorRestResourceController.class);

  private ActorRepository actorRepository;

  @Autowired
  public void setActorRepository(ActorRepository actorRepository) {
    this.actorRepository = actorRepository;
  }

  @RequestMapping(
      value = "/actors",
      method = RequestMethod.GET
  )
  public List<Actor> getActors() {
    logger.info(" -------- getActors -------");
    return actorRepository.readAll();
  }


  @RequestMapping(
      value = "/actor/{id}",
      method = RequestMethod.GET
  )
  public Actor getActor(@PathVariable("id") long id) {
    logger.info(" -------- getActor(id) -------");
    Optional<Actor> actorById = actorRepository.read(id);
    if (actorById.isPresent()) {
      return actorById.get();
    } else {
      throw new ResourceNotFoundException();
    }
  }



  @RequestMapping(
      value = "/actor",
      method = RequestMethod.POST
  )
  public void create(@RequestBody Actor actor) {
    logger.info(" -------- create() -------");
    actor.setResourcesRequested(0);
    actor.setRecordVersion(0);
    actor.setCreatedBy("create-method");
    actor.setCreatedTimestamp(LocalDateTime.now());

    actorRepository.create(actor);
  }


}

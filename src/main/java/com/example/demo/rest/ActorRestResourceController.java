package com.example.demo.rest;

import com.example.demo.da.ActorRepository;
import com.example.demo.da.entity.Actor;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/actors")
public class ActorRestResourceController {

  private static Logger logger = LoggerFactory.getLogger(ActorRestResourceController.class);

  private ActorRepository actorRepository;

  @Autowired
  public void setActorRepository(ActorRepository actorRepository) {
    this.actorRepository = actorRepository;
  }

  @RequestMapping(
      method = RequestMethod.GET
  )
  public List<Actor> getActors() {
    logger.info(" -------- getActors -------");
    return actorRepository.readAll();
  }


  @RequestMapping(
      value = "/{id}",
      method = RequestMethod.GET
  )
  public Optional<Actor> getActor(@PathVariable("id") long id) {
    return Optional.of(actorRepository.read(id));
  }


}

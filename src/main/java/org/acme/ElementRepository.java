package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ElementRepository implements PanacheRepository<Element> {
    List<Element> findByLastName(String lastName) { return list("lastName", lastName);}
}

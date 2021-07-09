//./mvnw compile quarkus:dev -DdebugHost=0.0.0.0
//./mvnw compile quarkus:dev

package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path(GreetingResource.RESOURCE_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

    public static final String RESOURCE_PATH = ("/People");

    @Inject
    EntityManager em;

    public List<Element> list = new ArrayList<>();
    public UriInfo uriInfo;
    private ElementRepository elementRepository;

    public static <Element> Response create(List<Element> entity, PanacheRepository<Element> repository, UriInfo uriInfo) {
        return null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response post(@Valid List<Element> elems) {
        list = elems;
        for (Element el : list)
            el.persist();
        return create(elems, elementRepository, uriInfo);
    }

    @GET
    @Path("{id}/getOne")
    @Produces(MediaType.APPLICATION_JSON)
    public Element get(@PathParam Long id) {
        return em.find(Element.class, id);
    }

    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Element> getAll() {
        Query query = em.createQuery("SELECT c FROM Element c WHERE c.name LIKE '%'");
        return query.getResultList();
    }

    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Element> find(@Valid final Element elem) {
        final Query query = em.createQuery("SELECT c FROM Element c WHERE c.name LIKE :name OR c.ID = :id " +
                        "AND c.price = :price OR c.manufacturer LIKE :manufacturer ")
            .setParameter("name","%" + elem.getName() + "%")
            .setParameter("id", elem.getId())
            .setParameter("price", elem.getPrice())
            .setParameter("manufacturer", elem.getManufacturer());
        return query.getResultList();
    }

    @DELETE
    public void delete(String Id){

    }

}

//%dev.quarkus.datasource.url=jdbc:postgresql://localhost:5005/product
//%dev.quarkus.datasource.username=admin
//%dev.quarkus.datasource.password=admin

//docker run --name products -p 5433:5432 -e POSTGRES_DB=product -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -d postgres:alpine
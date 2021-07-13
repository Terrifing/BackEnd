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
    public Response post(@Valid Element elem) {
        list.add(elem);
        em.persist(elem);
        return null;
    }

    @POST
    @Path("/getPage")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Element> getPage(@Valid final Page page){
        Query query = em.createQuery("SELECT c FROM Element c WHERE c.name LIKE '%'");
        query.setFirstResult(page.getOffset() * page.getPageSize());
        query.setMaxResults(page.getPageSize());
        return query.getResultList();
    }

    @GET
    @Path("{id}/getOne")
    @Produces(MediaType.APPLICATION_JSON)
    public Element getOne(@PathParam Long id) {
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
        final Query query = em.createQuery("SELECT c FROM Element c WHERE c.name LIKE :name")
            .setParameter("name","%" + elem.getName() + "%");
        return query.getResultList();
    }

    @POST
    @Path("/clear")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void clear(@Valid Element elem){
        list.remove(elem);
        em.remove(elem);
    }

    @POST
    @Path("/DelOne")
    @Transactional
    public void DelOne(@Valid Long id){
        em.remove(getOne(id));
    }

    @POST
    @Path("/change")
    @Transactional
    public void change(@Valid Element elem){
        Long ln = Long.valueOf(elem.getId());
        Element el = getOne(ln);
        el.setName(elem.getName());
        el.setPrice(elem.getPrice());
        el.setManufacturer(elem.getManufacturer());
        em.persist(el);
    }

    @POST
    @Path("/IdBellow")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Element> Bellow(@Valid  Long id){
        final Query query = em.createQuery("SELECT c FROM Element c WHERE c.id > :Id ")
                .setParameter("Id", id);
        return query.getResultList();
    }

    @POST
    @Path("/IdLow")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Element> Low(@Valid  Long id){
        final Query query = em.createQuery("SELECT c FROM Element c WHERE c.id < :Id ")
                .setParameter("Id", id);
        return query.getResultList();
    }

    @POST
    @Path("/IdEqual")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Element> Equal(@Valid  Long id){
        final Query query = em.createQuery("SELECT c FROM Element c WHERE c.id = :Id ")
                .setParameter("Id", id);
        return query.getResultList();
    }

    @POST
    @Path("/PriceBellow")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Element> PriceBellow(@Valid  Integer price){
        final Query query = em.createQuery("SELECT c FROM Element c WHERE c.price > :Price ")
                .setParameter("Price", price);
        return query.getResultList();
    }

    @POST
    @Path("/PriceLow")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Element> PriceLow(@Valid  Integer price){
        final Query query = em.createQuery("SELECT c FROM Element c WHERE c.price < :Price ")
                .setParameter("Price", price);
        return query.getResultList();
    }

    @POST
    @Path("/PriceEqual")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Element> PriceEqual(@Valid  Integer price){
        final Query query = em.createQuery("SELECT c FROM Element c WHERE c.price = :Price ")
                .setParameter("Price", price);
        return query.getResultList();
    }

    @POST
    @Path("/NameFilter")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Element> NameFilter(@Valid String name){
        final Query query = em.createQuery("SELECT c FROM Element c WHERE c.name LIKE :Name ")
                .setParameter("Name", name);
        return query.getResultList();
    }

    @POST
    @Path("/ManufacturerFilter")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Element> ManufacturerFilter(@Valid String manufacturer){
        final Query query = em.createQuery("SELECT c FROM Element c WHERE c.manufacturer LIKE :Manufacturer ")
                .setParameter("Manufacturer", manufacturer);
        return query.getResultList();
    }

    @POST
    @Path("/{elem}/Try")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Element> Test(@Valid Element prod, @PathParam String elem){
        String jql = "SELECT c FROM Element c WHERE c.name LIKE :name AND c.manufacturer LIKE :manufacturer AND ";
        String[] sp = elem.split("!");
        if(sp[0].equals("Больше"))
            jql += "c.ID > :id AND";
        if(sp[0].equals("Меньше"))
            jql += "c.ID < :id AND";
        if(sp[0].equals("Равно"))
            jql += "c.ID = :id AND";
        if(sp[1].equals("В начале"))
            prod.setName(prod.getName() + "%");
        if(sp[1].equals("В конце"))
            prod.setName("%" + prod.getName());
        if(sp[1].equals("В любом месте"))
            prod.setName("%" + prod.getName() + "%");
        if(sp[2].equals("Больше"))
            jql += " c.price > :price";
        if(sp[2].equals("Меньше"))
            jql += " c.price < :price";
        if(sp[2].equals("Равно"))
            jql += " c.price = :price";
        if(sp[3].equals("В начале"))
            prod.setManufacturer(prod.getManufacturer() + "%");
        if(sp[3].equals("В конце"))
            prod.setManufacturer("%" + prod.getManufacturer());
        if(sp[3].equals("В любом месте"))
            prod.setManufacturer("%" + prod.getManufacturer() + "%");
        final Query query = em.createQuery(jql)
                .setParameter("id",prod.getId())
                .setParameter("name", prod.getName())
                .setParameter("price", prod.getPrice())
                .setParameter("manufacturer", prod.getManufacturer());
        return query.getResultList();
    }

}

//%dev.quarkus.datasource.url=jdbc:postgresql://localhost:5005/product
//%dev.quarkus.datasource.username=admin
//%dev.quarkus.datasource.password=admin

//docker run --name products -p 5433:5432 -e POSTGRES_DB=product -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -d postgres:alpine
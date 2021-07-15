//./mvnw compile quarkus:dev -DdebugHost=0.0.0.0
//./mvnw compile quarkus:dev

package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;


@Path(GreetingResource.RESOURCE_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

    public static final String RESOURCE_PATH = ("/People");

    @Inject
    EntityManager em;
    @Inject
    Session session;

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
    public List<Element> getPage(@Valid Page page){
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
    @Path("/getCount")
    @Produces(MediaType.APPLICATION_JSON)
    public Long getAll() {
        Query query = em.createQuery("SELECT COUNT(*) FROM Element");
        return (Long) query.getResultList().get(0);
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
    @Path("/{elem}/Try")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Element> Test(@Valid Element prod, @PathParam String elem){
        String[] sp = elem.split("!");
        Filter filter1 = null;
        Filter filter2 = null;
        Filter filter3 = null;
        Filter filter4 = null;

        if(sp[0].equals("Больше"))
            filter1 = session.enableFilter("Id_Better").setParameter("id", prod.getId());
        else if(sp[0].equals("Меньше"))
            filter1 = session.enableFilter("Id_Low").setParameter("id", prod.getId());
        else if(sp[0].equals("Равно"))
            filter1 = session.enableFilter("Id_Equal").setParameter("id", prod.getId());
        else {
            prod.setId(1);
            filter1 = session.enableFilter("Id_Other").setParameter("id", prod.getId());
        }

        if(sp[1].equals("В начале"))
            prod.setName(prod.getName() + "%");
        else if(sp[1].equals("В конце"))
            prod.setName("%" + prod.getName());
        else prod.setName("%" + prod.getName() + "%");
        filter2 = session.enableFilter("Name").setParameter("name", prod.getName());

        if(sp[2].equals("Больше"))
            filter3 = session.enableFilter("Price_Better").setParameter("price", prod.getPrice());
        else if(sp[2].equals("Меньше"))
            filter3 = session.enableFilter("Price_Low").setParameter("price", prod.getPrice());
        else if(sp[2].equals("Равно"))
            filter3 = session.enableFilter("Price_Equal").setParameter("price", prod.getPrice());
        else {
            prod.setId(1);
            filter3 = session.enableFilter("Price_Other").setParameter("price", prod.getPrice());
        }

        if(sp[3].equals("В начале"))
            prod.setManufacturer(prod.getManufacturer() + "%");
        else if(sp[3].equals("В конце"))
            prod.setManufacturer("%" + prod.getManufacturer());
        else prod.setManufacturer("%" + prod.getManufacturer() + "%");
        filter4 = session.enableFilter("Manufacturer").setParameter("manufacturer", prod.getManufacturer());

        Integer offset = Integer.parseInt(sp[sp.length-2]);
        Integer pageSize = Integer.parseInt(sp[sp.length-1]);
        /*final Query query = em.createQuery(jql)
                .setParameter("id",prod.getId())
                .setParameter("name", prod.getName())
                .setParameter("price", prod.getPrice())
                .setParameter("manufacturer", prod.getManufacturer());*/
        list = session.createQuery("from Element").list();//query.getResultList();
        List<Element> result = new ArrayList<>();
        for(int i = offset*pageSize; i < min(offset*pageSize+pageSize, list.size()); i++)
            result.add(list.get(i));
        result.add(new Element(list.size(), "",0,""));
        return result;
    }

    @POST
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Element> test(@Valid Element elem){
        Filter filter = session.enableFilter("Test");
        filter.setParameter("id", elem.getId());
        session.beginTransaction();
        List<Element> results = session.createQuery("from Element").list();
        //session.close();
        return results;
    }

}

//%dev.quarkus.datasource.url=jdbc:postgresql://localhost:5005/product
//%dev.quarkus.datasource.username=admin
//%dev.quarkus.datasource.password=admin

//docker run --name products -p 5433:5432 -e POSTGRES_DB=product -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -d postgres:alpine
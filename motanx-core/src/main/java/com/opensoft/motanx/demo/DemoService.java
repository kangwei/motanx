package com.opensoft.motanx.demo;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Created by kangwei on 2016/8/27.
 */
@Path("demo")
public interface DemoService {
    @GET
    @Path("/hello")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    String hello();

    @GET
    @Path("/hello/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    String hello(@PathParam("id") String id);

    String sayHello(String s);

    @GET
    @Path("/list")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    List<String> list();

    Map<String, String> map();

    @POST
    @Path("/pojo")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    DemoPojo pojo(@BeanParam DemoPojo pojo);

    @GET
    @Path("/showPojo")
    @Produces({MediaType.APPLICATION_JSON})
    DemoPojo showPojo();
}

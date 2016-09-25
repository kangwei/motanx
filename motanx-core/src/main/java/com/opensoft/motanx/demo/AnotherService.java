package com.opensoft.motanx.demo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by kangwei on 2016/9/20.
 */
@Path("other")
public interface AnotherService {
    @GET
    @Path("/hello")
    @Produces({MediaType.APPLICATION_JSON})
    String hello();
}

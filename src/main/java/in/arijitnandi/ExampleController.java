package in.arijitnandi;

import io.quarkus.runtime.Quarkus;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/examples")
public class ExampleController {

    @GET
    public String getNotAccessProtected() {
        return "this endpoint is not access protected!";
    }

    @GET
    @Path("/str")
    public String setAccessProtected(String body) {
        return "this endpoint is RBAC protected and permits both users and admins!";
    }

    @GET
    @Path("/{id}")
    public String patchAccessProtected(@PathParam("id") String id) {
        return "this endpoint is RBAC protected and permits no one!";
    }

    @GET
    @Path("/shutdown")
    public String shutdown() {
        Quarkus.asyncExit();
        return "this endpoint is RBAC protected and permits no one!";
    }
}
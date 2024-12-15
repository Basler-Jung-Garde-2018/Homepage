package ch.junggarde.api.adapter.in;


import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;


import io.quarkus.security.identity.SecurityIdentity;
import org.jboss.resteasy.annotations.cache.NoCache;

@Path("/api/users")
public class TestResource {
    @Inject
    SecurityIdentity identity;

    @GET
    @Path("/me")
    @NoCache

    public User me() {
        return new User(identity);
    }

    public static class User {

        private final String userName;

        User(SecurityIdentity identity) {
            this.userName = identity.getPrincipal().getName();
        }

        public String getUserName() {
            return userName;
        }
    }
}
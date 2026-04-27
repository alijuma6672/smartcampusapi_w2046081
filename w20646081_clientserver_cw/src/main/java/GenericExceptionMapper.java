import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
     
        exception.printStackTrace(); 
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR) 
                       .entity("error: An unexpected internal server error occurred. Please try again later.")
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }
}
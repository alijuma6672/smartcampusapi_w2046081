import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @POST
    public Response createSensor(Sensor newSensor) {
        // Validation for empty id
        if (newSensor.getId() == null || newSensor.getRoomId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("error: Sensor ID and Room ID are required")
                           .build();
        }

        if (!DataStore.rooms.containsKey(newSensor.getRoomId())) {
            throw new LinkedResourceNotFoundException("Linked Room ID does not exist");
        }

        // 3. Saving sensor and updating list
        DataStore.sensors.put(newSensor.getId(), newSensor);
        DataStore.rooms.get(newSensor.getRoomId()).getSensorIds().add(newSensor.getId());

        return Response.status(Response.Status.CREATED).entity(newSensor).build();
    }

    @GET
    public Response getSensors(@QueryParam("type") String type) {
        List<Sensor> resultList = new ArrayList<>();
        
        for (Sensor sensor : DataStore.sensors.values()) {
            if (type != null && !type.trim().isEmpty()) {
                if (sensor.getType().equalsIgnoreCase(type)) {
                    resultList.add(sensor);
                }
            } else {
                resultList.add(sensor);
            }
        }
        
        return Response.ok(resultList).build();
    }
    
    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        // Verifiying if parent sensor exists
        if (!DataStore.sensors.containsKey(sensorId)) {

            throw new WebApplicationException(
                Response.status(Response.Status.NOT_FOUND)
                        .entity("error: Parent Sensor not found")
                        .build()
            );
        }
        
        return new SensorReadingResource(sensorId);
    }
}

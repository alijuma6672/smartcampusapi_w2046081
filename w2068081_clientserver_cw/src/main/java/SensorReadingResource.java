import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private String sensorId;

    // Receives sensorId
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    
    @GET
    public Response getReadings() {
        // GET request for list of readings
        List<SensorReading> readings = DataStore.sensorReadings.getOrDefault(sensorId, new ArrayList<>());
        return Response.ok(readings).build();
    }


    @POST
    public Response addReading(SensorReading reading) {
        
        Sensor parentSensor = DataStore.sensors.get(sensorId);
        
        // Exception handling for maintenance
        if (parentSensor != null && "MAINTENANCE".equalsIgnoreCase(parentSensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is offline for maintenance and cannot accept readings.");
        }

        // Creating random id and timestamp
        if (reading.getId() == null) reading.setId(UUID.randomUUID().toString());
        if (reading.getTimestamp() == 0) reading.setTimestamp(System.currentTimeMillis());

        DataStore.sensorReadings.putIfAbsent(sensorId, new ArrayList<>());
        DataStore.sensorReadings.get(sensorId).add(reading);

        // Updating currentValue
        if (parentSensor != null) {
            parentSensor.setCurrentValue(reading.getValue());
        }

        return Response.status(Response.Status.CREATED).entity(reading).build();
    }
}
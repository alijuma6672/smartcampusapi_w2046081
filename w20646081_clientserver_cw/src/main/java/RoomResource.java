import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    // GET request to get a list of all rooms
    @GET
    public Response getAllRooms() {
        List<Room> roomList = new ArrayList<>(DataStore.rooms.values());
        return Response.ok(roomList).build();
    }

    // GET request to get data about a room
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\":\"Room not found\"}")
                           .build();
        }
        return Response.ok(room).build();
    }

    // POST request to create a room
    @POST
    public Response createRoom(Room newRoom) {
        if (newRoom.getId() == null || newRoom.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\":\"Room ID is required\"}")
                           .build();
        }
        
        // Exception handling for rooms with the same name
        if (DataStore.rooms.containsKey(newRoom.getId())) {
            return Response.status(Response.Status.CONFLICT)
                           .entity("{\"error\":\"Room with this ID already exists\"}")
                           .build();
        }

        DataStore.rooms.put(newRoom.getId(), newRoom);
        return Response.status(Response.Status.CREATED).entity(newRoom).build();
    }
    
    
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);

        
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\":\"Room not found\"}")
                           .build();
        }

        
        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            
            throw new RoomNotEmptyException("Cannot delete room. Active sensors are still assigned.");
        }

        
        DataStore.rooms.remove(roomId);
        
        
        return Response.noContent().build(); 
    }
}
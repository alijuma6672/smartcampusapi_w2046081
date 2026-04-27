import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

public class DataStore {
    // Creating ConcurrentHashMap for rooms
    public static final Map<String, Room> rooms = new ConcurrentHashMap<>();

    // Data for Postman testing
    static {
        rooms.put("main-library", new Room("main_library", "Main Library", 50));
        rooms.put("lab_001", new Room("lab_001", "Computer Lab", 30));
    }
    
    // Creating ConcurrentHashMap for sensors
    public static final Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    public static final Map<String, List<SensorReading>> sensorReadings = new ConcurrentHashMap<>();
}
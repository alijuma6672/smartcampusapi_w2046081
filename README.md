# Smart Campus API

API Design Overview:
   
This project is a RESTful web API built with JAX-RS and designed to manage a university's rooms and sensors. It includes a hashmap to hold the data, a Sub-Resource Locator for nesting, exception mappers to handle logic constraints, and a logging filter as well.


Launch Instructions:

1. Clone the repository.
2. Open the project in NetBeans.
3. Right click the project folder in the Projects window and select Clean and Build.
4. Make sure it's going to run on GlassFish.
5. Right click the project and select Run.
6. GlassFish will deploy the application. 
7. The API base path will be accessible at: http://localhost:8080/w2046081_clientserver_cw/api/v1.
8. Run Postmam to test.


Sample Curl Commands:
(apipath changed my id from w2046081 to w2068081)

1. Get API Discovery Metadata:**
   
curl -X GET http://localhost:8080/w2068081_clientserver_cw/api/v1/

2. Create a new room

curl -X POST http://localhost:8080/w2068081_clientserver_cw/api/v1/rooms 
-H "Content-Type: application/json" \
-d '{"id":"LIB-301", "name":"Library Quiet Study", "capacity":50}'

3. Get all rooms

curl -X GET http://localhost:8080/w2068081_clientserver_cw/api/v1/rooms

4. Register a new sensor to a room

curl -X POST http://localhost:8080/w2068081_clientserver_cw/api/v1/sensors 
-H "Content-Type: application/json" \
-d '{"id":"TEMP-001", "type":"Temperature", "status":"ACTIVE", "currentValue":22.5, "roomId":"LIB-301"}'

5. Get sensors filtered by type

curl -X GET "http://localhost:8080/w2068081_clientserver_cw/api/v1/sensors?type=Temperature"



Part 1 Question 1:

In JAX-RS, the default lifecycle for a Resource class is per-request. This means that it isn’t treated like a singleton and a new instance is instantiated for every incoming request. This means that data has to be stored in a database or static variable because otherwise, the data will be deleted with every new request. However, because every temporary instance is writing and reading to the same database, data loss and race conditions can be issues. That’s why a HashMap or database has to be used to prevent those issues.

Question 2: 

In RESTful design, HATEOAS provides hypermedia links directly inside response data. This means that client developers don’t have to be reliant on static documentation and allowed actions and updated links are provided with each response. This avoids issues like the app breaking because of URL changes or having to find and type every needed link yourself. 

Part 2 Question 1: 

Returning full room objects creates a heavy payload that can strain bandwidth, whereas just returning IDs keeps the response lightweight and fast. However, only returning IDs forces it to make separate network requests for every single room just to display the details. This introduces significant latency on the client side. Therefore, returning the full room objects is preferable because it saves the client application from unneeded latency.

Question 2:

In my implementation, the DELETE operation is idempotent. This means that executing the same request multiple times leaves the data in the exact same state as if it was a single request. Once the first request successfully removes a room, any additional requests will return a 404 error since the room is already deleted. 

Part 3 Question 1:

If a client sends data in a different format, JAX-RS issues an Unsupported Media Type error. As a result, this prevents exceptions from crashing the application. 

Question 2: 

The query parameter approach is better because path parameters are designed to pinpoint specific resources in a REST hierarchy, but query parameters are used to filter and sort existing collections. Forcing filters directly into the URL path treats them as separate sub-resources, which contradicts REST principles. 

Part 4 Question 1:

Defining every nested path into a single controller class would lead to a massive, unreadable file. As a result, one small update to a sub-resource could easily break core functionality. Delegating logic to separate classes is preferred because each nested path is handled by its own self-contained file.

Part 5 Question 1:

HTTP 422 is more semantically accurate because it communicates that while the endpoint is correct and the syntax is fine, the underlying logic or data payload is what contains errors. Using a 422 separates simple broken links from semantic data issues.

Question 2:

Exposing raw stack traces directly to users would greatly undermine security. Sensitive internal details would be broadcasted instead of safely hidden. Consequently, bad actors could easily leverage this exposed framework data to look up known vulnerabilities or map out your logic for precise SQL injections and attacks become a severe threat. 

Question 3:

Manually pasting logging statements into every API method clutters your core business logic and turns future updates into a massive, error-prone chore. A much smarter approach is using a JAX-RS filter, which acts as a centralized checkpoint to automatically intercept all network traffic. By handling logging entirely at the framework level, you strip all that repetitive boilerplate out of your endpoints. This ensures your resource classes stay incredibly clean and focused strictly on handling data, while your logging rules are easily maintained in one single file.



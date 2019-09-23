
# CollabCalc

Small school project about using Java's Reflection to send an object over a network (TCP) 
to a server that will execute a chosen method and reply with the result.

A use case would be if the client has a bad hardware and would need a more powerful server 
to run expensive computations.

## Implementation

### CollabRequest

Each object and methods calls are encapsulated in a Serializable CollabRequest object,
that contains the object with the name of the method to execute and its parameters.

It has a run() method that invoke the methods and encapsulate the result in a CollabResponse object.

### CollabResponse

Encapsulate the result of the CollabRequest execution. If there was an error 
during the execution it's included in CollabResponse.

## Client & Server

The project includes a basic server that waits for CollabRequest, execute them and send back
a CollabResponse.

There's a basic Client where the user can choose between different test methods from a basic 
Calc class to perform CollabRequest to the server.

There's also an AutoClient that automatically send some pre-determined CollabRequest to the server, 
meant to show different use cases. 
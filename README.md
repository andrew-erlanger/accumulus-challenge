# Accumulus Synergy Coding Challenge

By Andrew Erlanger, 2023

## Building and Running the Server

There are three ways to build and run the server: 
Docker, shell scripts, and IntelliJ. Docker is recommended
for maximum portability, but IntelliJ is likely the most
convenient method if it works on your local environment.
With all three options, the server defaults to using port 8080.

### Running with Docker

This project contains a `Dockerfile` at the project root directory.
To run the server with docker, navigate a shell to the project
root, run `docker build .`, and then `docker run X`, where `X` is
the docker image hash from the build command.

### Running with Shell Scripts

This project contains a `scripts` directory containing (very) minimal build and run
scripts. To run the server with these scripts, navigate a shell to the project
root (_not_ the `scripts` directory) and run `./scripts/build.sh`, and
`./scripts/run.sh`. This option may not be portable.

### Running with IntelliJ

For this option, simply load the project using IntelliJ, and then
use the standard IntelliJ "run button" to run the server.

## Using the Server

The server exposes a few endpoints with specific HTTP methods:

`GET /toppingCountMap` returns a JSON object containing a mapping from
pizza toppings to the number of unique users (identified by email) which
have submitted said topping.

`DELETE /toppingCountMap` deletes all data, effectively resetting the application
state. This is useful for testing purposes.

`POST /userToppingSubmission` takes a body of the form
```
{"email": "foo@com.com", "toppings": ["apple", "banana"]}
```
and adds this data to the application data store. If there is no
error, a `200 Ok` is returned.

## Testing the Server

For your convenience, the file `./scripts/test.sh` contains a brief test to
demonstrate the capabilities of the "application".
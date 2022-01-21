# LIng-2 DP1 Semester Project - No Time For Heroes

![Logo](/react/public/images/logo_ntfh.png)

## Getting started

First, some dependencies that have to be installed:

- Java JDK 11
- NodeJS 16.13 LTS

### Initializing the proyect

Before getting to the fun part, there is one little step we have to make.
Navigate to the /react folder in the project:

```
cd ./react/
```

After that, install the node modules necessary to run the application with:

```
npm install
```

## Running the application

Nearly there! As this application is split in two services (spring-boot for backend and database and react for frontend)
we need to initialize both services individually.

### Spring-Boot

From the source folder, navigate to the spring-boot directory:

```
cd ./spring-boot/
```

Now we execute the service with the appropiate maven command:

```
mvn spring-boot:run
```

From here on out, the spring-boot proyect will be located at localhost:8080, with the console at localhost:8080/h2-console.
To access the console follow this image:
![console-tutorial](https://i.imgur.com/rQuYCm4.png)

### React.js

Now, for the visual part! Similar to spring-boot, we navigate to the react directory:

```
cd ./react/
```

Now we execute the service with the appropiate nodejs command:

```
npm run build
```

After that, we will se a message like this one:
```
The build folder is ready to be deployed.
You may serve it with a static server:

  npm install -g serve
  serve -s build
```

Following these steps, we will see the last message, telling us that everything was OK:

```
   ┌──────────────────────────────────────────────────┐
   │                                                  │
   │   Serving!                                       │
   │                                                  │
   │   - Local:            http://localhost:3000      │
   │   - On Your Network:  http://192.168.1.48:3000   │
   │                                                  │
   │   Copied local address to clipboard!             │
   │                                                  │
   └──────────────────────────────────────────────────┘
```

The application is now ready to run. Enjoy!

## Tools used

To assist and streamline the development process, we've decided to use these tools:

- [Postman](https://www.postman.com/): A simple to use collaborative API REST request sender
- [Github Actions](https://github.com/features/actions): Compiles projects from a github repository to ensure no compiling errors get through a pull request

## Other work!

Link to tutorial and full game: https://youtu.be/T72qEQxI5pA

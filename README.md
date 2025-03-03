# Ericsson MVC Task

This project is a simple MVC application. The application demonstrates basic CRUD functionality exposed through a REST API and comes with a simple frontend for testing.

## Overview

- **Backend:** Spring MVC
- **API Documentation:** Swagger UI
- **Frontend:** Simple web interface for testing endpoints
- **Containerization:** Docker

## API Documentation

You can view the API documentation at:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Running the Application

To start the application, navigate to the project root and run:

docker compose up --build


This command will build and start the application, which will listen on port `8080`.

## Accessing the Application

Once running, open your browser and navigate to:

[http://localhost:8080](http://localhost:8080)

You will see the simple frontend used for testing the API.

## Technologies Used

- **Spring MVC:** For building the RESTful web service.
- **Swagger UI:** For API documentation and testing.
- **Docker:** For containerizing the application.
- **JS, HTML, CSS:** For application frontend.
- **Other:** JPA, PostgreSQL etc.

## Summary

This project was created as a simple demonstration for Ericsson Digital Society, showcasing a Spring MVC application with a simple web frontend. 
The application is containerized with Docker for easy deployment. Simply run `docker compose up --build` in the project directory, and access the frontend or API documentation via your browser.


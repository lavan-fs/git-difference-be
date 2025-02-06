# Git Difference Backend

## Overview

This project is a backend service for analyzing and displaying differences between Git commits. It is built using Java and Spring Boot, providing RESTful APIs to fetch commit details and differences from GitHub repositories.

## Why Java Spring Boot?

Java Spring Boot is chosen for this project due to its robust ecosystem, ease of creating stand-alone applications, and extensive support for building RESTful services. Spring Boot simplifies the development process by providing pre-configured templates and reducing boilerplate code, making it an ideal choice for building scalable and maintainable backend services.

## Installation

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher


### Steps

1. Clone the repository:
    ```sh
    git clone https://github.com/lavan-fs/git-difference-be.git
    cd git-difference-be
    ```

2. Export Environments variables and  Build the project using Maven:
    ```sh
   export FE_URL="http://fe_url"
    mvn clean install
    ```

3. Run the application:
    ```sh
    mvn spring-boot:run
    ```

## Running the Application

Once the application is running, it will be available at `http://localhost:8080`. You can use tools like Postman or cURL to interact with the provided endpoints.

## Functionalities and Features

### Endpoints

1. **Get Commit Difference**
    - **URL:** `/repositories/{owner}/{repository}/commits/{oid}/diff`
    - **Method:** `GET`
    - **Description:** Fetches the difference (diff) details for a specific commit.
    - **Response:** A list of file differences, including change kind, file details, and hunks.

2. **Get Commit Details**
    - **URL:** `/repositories/{owner}/{repository}/commits/{oid}/details`
    - **Method:** `GET`
    - **Description:** Fetches detailed information about a specific commit.
    - **Response:** A map containing commit message, author details, commit date, and parent commit information.

### Features

- **Fetch Commit Details:** Retrieve detailed information about a specific commit, including author, message, and date.
- **Fetch Commit Differences:** Analyze and display the differences between files in a specific commit.
- **Filter Deleted Files:** Automatically filter out deleted files from the diff results.
- **Parse Patch Data:** Efficiently parse patch data to extract hunks and line changes.

## Configuration

The application can be configured using the `src/main/resources/application.properties` file. Key configurations include:

- `spring.application.name`: The name of the application.
- `springdoc.version`: The version of the OpenAPI documentation.
- `server.forward-headers-strategy`: Strategy for forwarding headers.


# Wizard World Potion Crafter

## How to Run the Project

To run this project, you need to have **Docker** installed on your system.

Use the following command to build the Java application's Docker image, start the Redis service, and then run the Java console application:

```bash
docker compose run --rm java-app
```

### Important Notes:

- **Redis Data Persistence:** The `redis_data` volume defined in `docker-compose.yaml` ensures that your cached data in Redis persists even if you stop and restart the `redis` service. If you want to clear the Redis cache, you'll need to remove this volume.

## Usage

Once the application starts, you will be presented with a menu:

```bash
Welcome to the Wizard World Potion Crafter!

--- Menu ---
1. List All Ingredients
2. List All Elixirs
3. Find Craftable Elixirs by Ingredients
4. Exit
Enter your choice:
```

Follow the prompts:

- **Option 1 & 2:** Fetch and display all ingredients or elixirs. The first time these are called, data will be fetched from the API and cached in Redis. Subsequent calls will retrieve from the cache.

- **Option 3: Find Creatable Elixirs:** Based on the ingredients you entered, the application will determine and list all potions you can craft. It will validate each ingredient you entered by checking against the API (and caching individual ingredient lookups in Redis for efficiency). Then, for each ingredient, it will fetch from the API (and cache it) all the potions that require that ingredient. Finally, for each potion list of ingredients, it will compare with the provided set of ingredients.

- **Option 5: Exit:** Closes the application.

## Why Docker?

This project has been **dockerized** to provide a seamless and consistent experience for anyone wishing to run it.

- **No Java Version Hassle:** You don't need to have a specific Java Development Kit (JDK) version installed on your local machine. Docker containers encapsulate the entire runtime environment, ensuring the application runs with its intended JDK version.

- **Easy Setup:** With Docker installed, you can get the entire application stack (Java app and Redis cache) up and running with a single command, avoiding complex local environment configurations.

- **Dependency Management:** All project dependencies (Maven, Redis, RESTEasy, Jackson) are managed within the Docker environment.

## Key Features

- **Elixir & Ingredient Data Fetching:** Retrieves data on elixirs and ingredients from the Wizard World API.

- **Redis Caching:** Implements a Redis caching layer to store fetched data, significantly speeding up subsequent requests and reducing load on the external API.

- **Ingredient Validation & Caching:** When you enter ingredients, the application fetches and caches individual ingredient details by name, optimizing lookups for future crafting attempts.

- **Potion Crafting:** Determines which potions can be crafted based on your available ingredients.

## Technologies Used

- **Java 21:** Core programming language.

- **Maven:** Build automation and dependency management.

- **Docker & Docker Compose:** Containerization for simpler service orchestration.

- **Redis:** Caching.

- **RESTEasy:** Java API for RESTful Web Services, used for interacting with the Wizard World API.

- **Jackson:** High-performance JSON processor for data serialization and deserialization.

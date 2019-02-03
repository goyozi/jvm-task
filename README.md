### Setup

The project is written in Kotlin and uses Gradle (wrapper provided) for build and dependency management.

Java 8+ is required to build the project.

### Running the project

To build and run the project you can execute:

```
./run.sh
```

The command builds the project using the provided Gradle wrapper script and runs the application using `docker-compose`.

### Testing the project

Tests can be executed by running:

```
./gradlew test
```

### Implementation notes

Similarly to the Python task, I opted for the simplest thing that does the job, given the provided requirements.
In a real-world scenario the code might be a bit different but the _how_ is very context-dependent.
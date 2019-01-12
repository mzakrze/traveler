# traveler

Projekt zaliczeniowy na przedmiot SPDB w semestrze 18Z na wydziale EiTI Politechniki Warszawskiej.

### Prerequisite
- Java 8
- npm(5.0.0), node(8.0.0)
- Gradle

### Install
- make sure to put your Google API KEY:
```bash
PROJECT_ROOT/src/main/resources/secrets$ ls
google_api_key  google_api_key.gpg
```
- run:
```
PROJECT_ROOT/frontend $ npm install
```

### Run frontend
```
PROJECT_ROOT/frontend $ make start
```

### Run backend
- import project to your IDE(e.g. IntelliJ IDEA)
- run from IDE

### Or run both(not in development mode):
- run:
```
PROJECT_ROOT$ make local_deploy
```
- go to localhost:8080

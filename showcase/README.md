# PrimeFaces Extensions Showcase

The showcase demonstrates every component of PrimeFaces Extensions. Its layout, menu, theming and example source viewer are based on the
[PrimeFaces showcase](https://github.com/primefaces/primefaces/tree/master/primefaces-showcase).

## Run

Install the core module once (and again after any change to `core`), then start the showcase from this directory:

```
mvn -q -pl core install -DskipTests     # from the repository root
cd showcase
mvn -Ddev
```

Then open your web browser to http://localhost:8080/showcase/ (the context path is `/showcase`).

## Test

```
mvn -q -pl showcase test                         # unit tests (menu, page conventions, example coverage)
bash showcase/src/test/scripts/crawl.sh          # with the server running: requests every page, expects "OK N/N"
```

## Docker (TomEE, ROOT context)

```
mvn -q -pl showcase package -DskipTests
docker compose -f showcase/docker-compose.yml up --build
```

The container serves the showcase at http://localhost:8080/ (ROOT context); crawl it with `crawl.sh http://localhost:8080`.

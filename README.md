# ICE Home Assignment

A music metadata service

# How to run

* Make sure `docker`, `docker-compose` and `java 17` are installed
* Run `sh run.sh`

# Assumptions and ideas
* The solution is essentially a Spring Boot microservice using Postgres as DB and running everything in docker
  * The service is running on port 8080
* Artists table is seeded with 5 artist on startup
  * I added additional Get endpoint to get artist list, see API section
* Artist of the day algorithm is `index =  day_since_epoch % number_of_artists`. This way we ensure that 
  distribution is fair.
  

# API

Get artists:
```
curl --location --request GET 'localhost:8080/artists'
```

Get artist tracks:
```
curl --location --request GET 'localhost:8080/artists/{artistId}/tracks'
```

Get artist of the day:
```
curl --location --request GET 'localhost:8080/artists/oftheday'
```

Add track to the artist:
```
curl --location --request POST 'localhost:8080/artists/{artistId}/tracks' \
--header 'Content-Type: application/json' \
--data-raw '{
    "title": "track02",
    "genre": "rap",
    "length": 10
}'
```

Add alias to the artist:
```
curl --location --request POST 'localhost:8080/artists/{artistId}/aliases' \
--header 'Content-Type: application/json' \
--data-raw '{
    "alias": "SOmeCoolAlias4"
}'
```

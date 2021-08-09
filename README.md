[![Unit tests and packaging](https://github.com/lionel-nj/city-backend/actions/workflows/test_pack.yml/badge.svg?event=release)](https://github.com/lionel-nj/city-backend/actions/workflows/test_pack.yml)

# Canadian city backend autocompletion API

## 🎉 Features

This is a simple API to search through city's data provided by [geonames.org](geonames.org).

Design a REST API endpoint that provides auto-complete suggestions for Canadian cities 🇨🇦
- The endpoint is exposed at `/suggestions`
- The partial (or complete) search term is passed as a querystring parameter q
- The caller's location can optionally be supplied via querystring parameters
latitude and longitude to help improve relative scores
- The endpoint returns a JSON response with an array of scored suggested
matches
  - The suggestions are sorted by descending score. The scoring is established using the [Jaro-Wrinkler distance](https://en.wikipedia.org/wiki/Jaro%E2%80%93Winkler_distance)
  - Each suggestion has a score between 0 and 1 (inclusive) indicating
confidence in the suggestion (1 is most confident)
  - Each suggestion has a name which can be used to disambiguate between
similarly named locations
  - Each suggestion has a latitude and longitude

## ⚙️ How to start the service

### Using a local `.jar` file
1. Download the latest application's `jar` file at [available here](https://github.com/MobilityData/gtfs-validator/releases)
1. Run the following command line:
```
java -jar city-backend-v1.0.0jar
```

### Using a Docker image

1. Pull the application's Docker image 
```
docker pull lionelnj/city-backend:latest
```

2. Run the container 

```
docker run lionelnj/city-backend:latest -p <port>
```
where <port> shall be replaced by the port you would like to use (e.g. 8080 ).
  
## 🧠 How to use the service

**The services is exposed at `/suggestions/`.**

**Query parameters**

| query parameter 	| description                    	|
|-----------------	|--------------------------------	|
| q               	| city name to match             	|
| latitude        	| user's latitude                	|
| longitude       	| user's longitude               	|
| perPage         	| number of suggestions per page 	|
| page            	| page for pagination            	|

Sample example of an API request:
```
http://localhost:8080/suggestions/?q=montreal&page=2
```
  
```
curl http://localhost:8080/suggestions/?q=montreal&page=2
```
 
  
 ## 💪🏾 Areas of improvement
  - Using another algorithm to establish the similarity between two `String`s would allow be beneficial.
  - Computing the exact distsance between the user and a given city would allow greater precision. We could eventually add a query parameter specifying the maximum distance to search cities.
  - The definition of "large" cities could be worked on: if large means population, this parameter could also be taken into account while editing suggestions. The same rationale  applies to when "large" refers to the size of the city.
  
## Per per user API?
  - Use `OpenID` (relies on OAuth 2.0) to authenticate users
  - Deploy application at large scale (using Kubernetes for example)
  - The management of user's plan can be done separately
  
 📚 docs: https://developers.google.com/identity/protocols/oauth2/openid-connect

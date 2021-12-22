# TomTom Scenery Backend

The purpose of the TomTom Scenery Backend application is to return specific information about a Point Of Interest (POI) to the frontend. To get the specific POI information the backend uses the information it gets from the GET-Request. The GET-Request contains 3 pieces of information:

- Chosen category
- Latitude
- Longitude

Based on that information the TomTom Scenery Backend application will build a URL that will retrieve all the POI data from the TomTom API, then filters out the specific information and sends that information as an object to the frontend.

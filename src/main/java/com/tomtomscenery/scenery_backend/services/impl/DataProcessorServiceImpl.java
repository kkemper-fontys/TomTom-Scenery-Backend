package com.tomtomscenery.scenery_backend.services.impl;

import com.tomtomscenery.scenery_backend.model.Impl.PoiImpl;
import com.tomtomscenery.scenery_backend.services.I_DataProcessorService;
import com.tomtomscenery.scenery_backend.services.I_UrlBuildService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Service
public class DataProcessorServiceImpl implements I_DataProcessorService
{
     private int counter;
     private final I_UrlBuildService urlBuildService;

     public DataProcessorServiceImpl(I_UrlBuildService urlBuildService) {
          this.urlBuildService = urlBuildService;
     }

     public int getCounter() {
          return counter;
     }

     public void setCounter(int counter) {
          this.counter = counter;
     }


     @Override
     public PoiImpl getPois() {
          try {
               URL url = getUrl();
               URLConnection urlConnection = url.openConnection();

               if (((HttpURLConnection) urlConnection).getResponseCode() == 200) {

                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReaderObject = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilderObject = new StringBuilder();

                    read(bufferedReaderObject, stringBuilderObject);
                    return getSpecificInformation(stringBuilderObject);
               }
          } catch (IOException ioException) {
               ioException.printStackTrace();
          }
          return null;
     }

     private PoiImpl getSpecificInformation(StringBuilder stringBuilderObject)
     {
          JSONObject jsonObj = new JSONObject(stringBuilderObject.toString());
          // JSONArray object to navigate to resultsArray.
          JSONArray resultArrayObj = jsonObj.getJSONArray("results");
          // JSONObject to navigate to the poi object within de resultsArray at a specific index(counter).
          JSONObject poi = resultArrayObj.getJSONObject(counter).getJSONObject("poi");
          // JSONArray object to navigate to categorySetArray within poi object.
          JSONArray categorySetArray = poi.getJSONArray("categorySet");
          // JSONObject to navigate to the address object within de resultsArray at a specific index(counter).
          JSONObject addressPOI = resultArrayObj.getJSONObject(counter).getJSONObject("address");
          // JSONObject to navigate to the position object within de resultsArray at a specific index(counter).
          JSONObject positionPoi = resultArrayObj.getJSONObject(counter).getJSONObject("position");

          // If the object at a specific index is not null and the length of the array is smaller than the limit
          // (limited amount of results)? Set the limit with the length of the array to avoid arrayOutOfBoundExceptions.
          if (resultArrayObj.getJSONObject(counter) != null && resultArrayObj.length() < urlBuildService.getLimit())
          {
               urlBuildService.setLimit(resultArrayObj.length());
          }

          if (resultArrayObj.length() == 0)
          {
               System.out.println("geen info");
          }
          String namePoi = poi.getString("name");
          long categoryIdPoi = categorySetArray.getJSONObject(0).getLong("id");
          String addressPoi = addressPOI.getString("freeformAddress");
          String localNamePoi = addressPOI.getString("localName");
          double latPoi = positionPoi.getDouble("lat");
          double lonPoi = positionPoi.getDouble("lon");

          return new PoiImpl(namePoi, categoryIdPoi, addressPoi, localNamePoi, latPoi, lonPoi);
     }


     private void read(BufferedReader br, StringBuilder sbuilderObj) throws IOException {
          String line;
          while ((line = br.readLine()) != null) {
               sbuilderObj.append(line);
          }
     }

     private URL getUrl() throws MalformedURLException {
          return new URL(urlBuildService.getUrlSearchString());
     }
}

package com.tomtomscenery.scenery_backend.services.impl;

import com.tomtomscenery.scenery_backend.model.Impl.PoiImpl;
import com.tomtomscenery.scenery_backend.services.I_DataProcessorService;
import com.tomtomscenery.scenery_backend.services.I_UrlBuildService;
import org.json.JSONArray;
import org.json.JSONException;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class DataProcessorServiceImpl implements I_DataProcessorService
{
     private int counter;
     private final I_UrlBuildService urlBuildService;
     private List<PoiImpl> poiCollection;

     public DataProcessorServiceImpl(I_UrlBuildService urlBuildService) {
          this.urlBuildService = urlBuildService;
     }

     public void setPoiCollection(List<PoiImpl> poiCollection) {
          this.poiCollection = poiCollection;
     }

     public int getCounter() {
          return counter;
     }

     public void setCounter(int counter) {
          this.counter = counter;
     }


     // Read incoming data and store it into the StringBuilder object.
     private void read(BufferedReader br, StringBuilder sbuilderObj) throws IOException {
          String line;
          while ((line = br.readLine()) != null) {
               sbuilderObj.append(line);
          }
     }

     // Create an URL using the UrlSearchString from the UrlBuildService class.
     private URL getUrl() throws MalformedURLException {
          return new URL(urlBuildService.getUrlSearchString());
     }

     // This method checks if the are results, and returns an object with specific information.
     private PoiImpl getSpecificInformation(StringBuilder stringBuilderObject)
     {
          JSONObject jsonObj = new JSONObject(stringBuilderObject.toString());

          // JSONArray object to navigate to resultsArray.
          JSONArray resultArrayObj = jsonObj.getJSONArray("results");
          if (resultArrayObj.length() == 0)
          {
               return null;
          }
          // This method checks if the length of the array is smaller than the limit (limited amount of results).
          // If the length of the array is smaller than the limit it will set the limit to the length of the
          // array to avoid arrayOutOfBoundExceptions.
          if (resultArrayObj.length() < urlBuildService.getLimit())
          {
               urlBuildService.setLimit(resultArrayObj.length());
          }

          // JSONObject to navigate to the poi object within de resultsArray at a specific index(counter).
          JSONObject poi = resultArrayObj.getJSONObject(counter).getJSONObject("poi");
          // JSONArray object to navigate to categorySetArray within poi object.
          JSONArray categorySetArray = poi.getJSONArray("categorySet");
          // JSONObject to navigate to the address object within de resultsArray at a specific index(counter).
          JSONObject addressPOI = resultArrayObj.getJSONObject(counter).getJSONObject("address");
          // JSONObject to navigate to the position object within de resultsArray at a specific index(counter).
          JSONObject positionPoi = resultArrayObj.getJSONObject(counter).getJSONObject("position");

          String namePoi = poi.getString("name");
          long categoryIdPoi = categorySetArray.getJSONObject(0).getLong("id");
          String addressPoi = addressPOI.getString("freeformAddress");
          String localNamePoi = addressPOI.getString("localName");
          double latPoi = positionPoi.getDouble("lat");
          double lonPoi = positionPoi.getDouble("lon");

          return new PoiImpl(namePoi, categoryIdPoi, addressPoi, localNamePoi, latPoi, lonPoi);
     }


     // This method stores the objects with specific information in an arraylist and returns that arraylist.
     @Override
     public List<PoiImpl> getPois() {
          try {
               URL url = getUrl();
               URLConnection urlConnection = url.openConnection();
               List<PoiImpl> listOfPois = new ArrayList<>();

               if (((HttpURLConnection) urlConnection).getResponseCode() == 200) {

                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReaderObject = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilderObject = new StringBuilder();

                    read(bufferedReaderObject, stringBuilderObject);
                    for(int i = 0; i < urlBuildService.getLimit(); i++)
                    {
                         if(getSpecificInformation(stringBuilderObject) == null)
                         {
                              return null;
                         }
                         else
                         {
                              getSpecificInformation(stringBuilderObject);
                              listOfPois.add(getSpecificInformation(stringBuilderObject));
                              setCounter(i);
                         }
                    }
                    setPoiCollection(listOfPois);
                    return poiCollection;
               }
          }
          catch (IOException | JSONException exception) {
               String message = exception.getMessage();
          }
          return null;
     }
}

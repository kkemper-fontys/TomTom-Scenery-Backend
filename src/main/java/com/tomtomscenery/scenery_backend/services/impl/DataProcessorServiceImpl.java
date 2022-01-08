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

          private final I_UrlBuildService urlBuildService;

          public DataProcessorServiceImpl(I_UrlBuildService urlBuildService) {
               this.urlBuildService = urlBuildService;
          }

          @Override
          public PoiImpl readDataReturnPoi()
          {
               try
               {
                    URL url = getUrl();
                    URLConnection urlConnection = url.openConnection();

                    if (((HttpURLConnection) urlConnection).getResponseCode() == 200)
                    {
                         InputStream inputStream = urlConnection.getInputStream();
                         BufferedReader bufferedReaderObject = new BufferedReader(new InputStreamReader(inputStream));
                         StringBuilder stringBuilderObject = new StringBuilder();

                         readIncomingData(bufferedReaderObject, stringBuilderObject);
                         return specificPoiInformation(stringBuilderObject);
                    }
               }
               catch (MalformedURLException malformedURLException) {
                    System.out.println(malformedURLException);
               }
               catch (IOException ioException) {
                    System.out.println(ioException);
               }
               return null;
          }

          // This method retrieves specific information from the incoming data,
          // stores it, and returns it as a PoiImpl object to the frontend.
          private PoiImpl specificPoiInformation(StringBuilder stringBuilderObject) {

               // Use the JSONObject that contains the json data.
               JSONObject jsonObj = new JSONObject(stringBuilderObject.toString());

               // Create a JsonArrayObject from the "results" array called resultArrayObj.
               JSONArray resultArrayObj = jsonObj.getJSONArray("results");
               //System.out.println("All the result data: " + resultArrayObj.toString());

               // Create a JSONObject 'poi'. The POI information can be found at index 0 of
               // the 'results' array. So use the 'poi' object to search the 'results' array.
               JSONObject poi = resultArrayObj.getJSONObject(0).getJSONObject("poi");
               //System.out.println("All the POI data from the 'poi' object within the 'results' array : " + poi.toString());

               // Retrieve the String information within the 'poi' JSONObject called 'name'.
               String namePoi = poi.getString("name");
               //System.out.println("String data from the nameObject within 'poi': " + namePoi);

               // Retriev the Long  information from the 'id' object from the 'categorySet' array
               // within the 'poi' object.
               JSONArray categorySetArray = poi.getJSONArray("categorySet");
               long categoryIdPoi = categorySetArray.getJSONObject(0).getLong("id");
               //System.out.println("Long data from the categorySetArray within 'poi': " + categoryIdPoi);


               // Then retrieve the String information of the 'freeformAddress' JSONObject
               // within the 'poi' JSONObject called 'address'.
               JSONObject addressPOI = resultArrayObj.getJSONObject(0).getJSONObject("address");
               String addressPoi = addressPOI.getString("freeformAddress");
               //System.out.println("String data from the 'freeformaddress' object within the 'address object within 'poi': " + addressPoi);

               // within the 'poi' JSONObject called 'address' and retrieve the String information
               // from the 'localName' object.
               JSONObject placeNameObject = resultArrayObj.getJSONObject(0).getJSONObject("address");
               String localNamePoi = addressPOI.getString("localName");
               //System.out.println("String data from the 'localName' object within the 'address' object within the 'poi' object: " + localNamePoi);


               // Retrieve the 'lat' and 'lon' information of type double from the JSONObject called 'position'
               JSONObject positionPoi = resultArrayObj.getJSONObject(0).getJSONObject("position");
               double latPoi = positionPoi.getDouble("lat");
               double lonPoi = positionPoi.getDouble("lon");

               return new PoiImpl(namePoi, categoryIdPoi, addressPoi, localNamePoi, latPoi, lonPoi);
          }

          private void readIncomingData(BufferedReader bufferedReaderObject, StringBuilder stringBuilderObject) throws IOException {
               String line;
               while ((line = bufferedReaderObject.readLine()) != null) {
                    stringBuilderObject.append(line);
               }
          }

          private URL getUrl() throws MalformedURLException {
               return new URL(urlBuildService.getUrlSearchString());
          }
}

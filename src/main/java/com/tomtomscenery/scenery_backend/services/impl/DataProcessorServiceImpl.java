package com.tomtomscenery.scenery_backend.services.impl;

import com.tomtomscenery.scenery_backend.model.Impl.PoiImpl;
import com.tomtomscenery.scenery_backend.services.I_DataProcessorService;
import com.tomtomscenery.scenery_backend.services.I_UrlBuildService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

@Service
public class DataProcessorServiceImpl implements I_DataProcessorService {

     private final I_UrlBuildService urlBuildService;

     public DataProcessorServiceImpl(I_UrlBuildService urlBuildService) {
          this.urlBuildService = urlBuildService;
     }


     @Override
     public PoiImpl readDataReturnPoi() {
                  try
               {
                    URL url = new URL(urlBuildService.getUrlSearchString());
                    URLConnection hr = url.openConnection();

                    if (((HttpURLConnection) hr).getResponseCode() == 200)
                    {
                         InputStream inputStr = hr.getInputStream();
                         BufferedReader br = new BufferedReader(new InputStreamReader(inputStr));
                         String line;
                         StringBuilder sbuilderObj = new StringBuilder();
                         while ((line = br.readLine()) != null)
                         {
                              sbuilderObj.append(line);
                         }


                         // Use the JSONObject that contains the json data.
                              JSONObject jsonObj = new JSONObject(sbuilderObj.toString());
                         // Create a JsonArrayObject from the "results" array called resultArrayObj.
                              JSONArray resultArrayObj = jsonObj.getJSONArray("results");
                         // Create a JSONObject 'poi'. The POI information can be found at index 0 of
                         // the 'results' array. So use the 'poi' object to search the 'results' array.
                              JSONObject poi = resultArrayObj.getJSONObject(0).getJSONObject("poi");

                         // Retrieve the String information within the 'poi' JSONObject called 'name'.
                              String namePoi = poi.getString("name");
                         // Retriev the Long  information from the 'id' object from the 'categorySet' array
                         // within the 'poi' object.
                              JSONArray categorySetArray = poi.getJSONArray("categorySet");
                              long categoryIdPoi = categorySetArray.getJSONObject(0).getLong("id");
                         // Then retrieve the String information of the 'freeformAddress' JSONObject
                         // within the 'poi' JSONObject called 'address'.
                              JSONObject addressPOI = resultArrayObj.getJSONObject(0).getJSONObject("address");
                              String addressPoi = addressPOI.getString("freeformAddress");

                         // within the 'poi' JSONObject called 'address' and retrieve the String information
                         // from the 'localName' object.
                              JSONObject placeNameObject = resultArrayObj.getJSONObject(0).getJSONObject("address");
                              String localNamePoi = addressPOI.getString("localName");


                         // Retrieve the 'lat' and 'lon' information of type double from the JSONObject called 'position'
                              JSONObject positionPoi = resultArrayObj.getJSONObject(0).getJSONObject("position");
                              double latPoi  = positionPoi.getDouble("lat");
                              double lonPoi  = positionPoi.getDouble("lon");

                         return new PoiImpl(namePoi, categoryIdPoi, addressPoi, localNamePoi, latPoi, lonPoi);
                    }
               }
               catch (Exception e)
               {
                    System.out.println(e);
               }

               return null;
          }
     }

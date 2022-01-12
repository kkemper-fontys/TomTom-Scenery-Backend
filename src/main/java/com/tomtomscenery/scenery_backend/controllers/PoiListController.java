package com.tomtomscenery.scenery_backend.controllers;

import com.tomtomscenery.scenery_backend.model.Impl.PoiImpl;
import com.tomtomscenery.scenery_backend.services.I_DataProcessorService;
import com.tomtomscenery.scenery_backend.services.I_PoiCollectorService;
import com.tomtomscenery.scenery_backend.services.I_UrlBuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PoiListController
{

     @Autowired
     I_DataProcessorService dataProcessorService ;

     @Autowired
     I_UrlBuildService urlBuildService;

     @Autowired
     I_PoiCollectorService poiCollectorService;

     List<PoiImpl> collectionOfPois = new ArrayList<>();


     @GetMapping("getpois/{category}/{latitude}/{longitude}")
     public List<PoiImpl> getPoiUser    (@PathVariable(name = "category") String category,
                                         @PathVariable(name = "latitude") double lat,
                                         @PathVariable(name = "longitude") double lon)
     {
          urlBuildService.setUrlSearchString(category, lat, lon);
          collectionOfPois.clear();

          for(int i = 0; i < urlBuildService.getLimit(); i++)
          {
               if(dataProcessorService.getPois() != null)
               {
                    collectionOfPois.add(dataProcessorService.getPois());
                    dataProcessorService.setCounter(i);
               }
          }
          poiCollectorService.setPoiCollection(collectionOfPois);


          return poiCollectorService.getPoiCollection();
     }
}

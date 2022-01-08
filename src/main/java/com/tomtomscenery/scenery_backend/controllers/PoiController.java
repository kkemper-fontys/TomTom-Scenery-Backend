package com.tomtomscenery.scenery_backend.controllers;

import com.tomtomscenery.scenery_backend.model.Impl.PoiImpl;
import com.tomtomscenery.scenery_backend.services.I_DataProcessorService;
import com.tomtomscenery.scenery_backend.services.I_UrlBuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PoiController
{

     @Autowired
     I_UrlBuildService urlBuildService;
     @Autowired
     I_DataProcessorService dataProcessorService;

     public PoiController(I_UrlBuildService urlBuildService, I_DataProcessorService dataProcessorService) {
          this.urlBuildService = urlBuildService;
          this.dataProcessorService = dataProcessorService;
     }


     @GetMapping("getpoi/{category}/{latitude}/{longitude}")
     public PoiImpl getPoiUser(@PathVariable(name = "category") String category,
                               @PathVariable(name = "latitude") double lat,
                               @PathVariable(name = "longitude") double lon)
     {
          urlBuildService.setUrlSearchString(category, lat, lon);
          return dataProcessorService.readDataReturnPoi();
     }

}

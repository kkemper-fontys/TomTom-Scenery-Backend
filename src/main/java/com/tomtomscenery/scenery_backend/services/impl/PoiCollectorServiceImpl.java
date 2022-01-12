package com.tomtomscenery.scenery_backend.services.impl;

import com.tomtomscenery.scenery_backend.model.Impl.PoiImpl;
import com.tomtomscenery.scenery_backend.services.I_PoiCollectorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PoiCollectorServiceImpl implements I_PoiCollectorService
{
     private List<PoiImpl> poiCollection;

     public PoiCollectorServiceImpl() {
     }

     @Override
     public List<PoiImpl> getPoiCollection() {
          return poiCollection;
     }

     @Override
     public void setPoiCollection(List<PoiImpl> poiCollection) {
          this.poiCollection = poiCollection;
     }
}

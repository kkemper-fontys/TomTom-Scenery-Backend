package com.tomtomscenery.scenery_backend.services;

import com.tomtomscenery.scenery_backend.model.Impl.PoiImpl;

import java.util.List;

public interface I_PoiCollectorService
{
     List<PoiImpl> getPoiCollection();
     void setPoiCollection(List<PoiImpl> poiCollection);
}

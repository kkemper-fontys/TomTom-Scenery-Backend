package com.tomtomscenery.scenery_backend.services;

import com.tomtomscenery.scenery_backend.model.Impl.PoiImpl;

public interface I_DataProcessorService
{
     PoiImpl getPois();
     void setCounter(int counter);
     int getCounter();
}

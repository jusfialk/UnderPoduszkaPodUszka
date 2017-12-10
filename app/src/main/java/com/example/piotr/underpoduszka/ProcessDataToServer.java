package com.example.piotr.underpoduszka;


import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;


/**
 * Created by Piotr on 28.11.2017.
 */

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class ProcessDataToServer extends AsyncTask<Void,Void,Void> {
    DataModel dataModel;
    public ProcessDataToServer(DataModel model)  {

        this.dataModel = model;
    }




    @Override
    protected Void doInBackground(Void... voids) {

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.0.6:8080/getData/{gyroscope}/{accelerometer}/{time}/{userName}/{city}";
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
        restTemplate.getForObject(url, DataModel.class,1,2,3,4,5);
        return null;
    }



}

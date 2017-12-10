package rest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;

import org.springframework.web.util.UriComponentsBuilder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {  

    @RequestMapping(value="/getData/{gyroscope}/{accelerometer}/{time}/{userName}/{city}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DataModel passRecord(@PathVariable(value="gyroscope") String gyroscopeValue, 
								@PathVariable(value="accelerometer") String acceloremeterValue,
								@PathVariable(value="time") String time,
								@PathVariable(value="userName") String userName,
								@PathVariable(value="city") String city) 
	{      
		
		SQLLite db = new SQLLite();		
		db.insert(gyroscopeValue, acceloremeterValue, time, userName, city);
		return null;
    }
	
	
	@RequestMapping("/getCities")
	public ResponseEntity<List<String>> getCities(){
		SQLLite db = new SQLLite();
		return new ResponseEntity<List<String>>(db.getCityList(), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/getUserList/{city}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getUserList(@PathVariable(value="city") String city){
		
		SQLLite db = new SQLLite();
				
		return new ResponseEntity<List<String>>(db.getUserListOnCity(city), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/getDate/{city}/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getDate(@PathVariable(value="city") String city,
												@PathVariable(value="userName") String userName)
	{
		SQLLite db = new SQLLite();				
		return new ResponseEntity<List<String>>(db.getDates(city,userName), HttpStatus.CREATED);
		
	}
	
		@RequestMapping(value = "/getDataSets/{city}/{userName}/{time}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DataModel>> getDataSets(@PathVariable(value="city") String city,
													@PathVariable(value="userName") String userName,
													@PathVariable(value="time") String time)
	{		
		SQLLite db = new SQLLite();	
		return new ResponseEntity<List<DataModel>>(db.getDataSets(city,userName,time), HttpStatus.CREATED);

														
	}
	
	
	
	
}
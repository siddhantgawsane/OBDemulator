package com.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Geocoder {
	/*
	 * Sample url
	 * http://maps.googleapis.com/maps/api/directions/json?origin=pune
	 * &destination=amravati&sensor=false&mode=driving
	 * 
	 * result{ "routes" : [{ "bounds" : { "northeast" : { "lat" : 20.92891 "lng"
	 * : 77.764730 }, "southwest" : { "lat" : 18.520650,"lng" : 73.856740 }
	 * "copyrights" : "Map data ©2013 Google", "legs" : [ { "distance" : {
	 * "text" : "603 km", "value" : 602864 }, "duration" : { "text" :
	 * "10 hours 1 min", "value" : 36060 }, "end_address" :
	 * "Amravati, Maharashtra, India", "end_location" : { "lat" : 20.925830,
	 * "lng" : 77.764730 }, "start_address" : "Pune, Maharashtra, India",
	 * "start_location" : { "lat" : 18.520650, "lng" : 73.856740 },
	 */
	public static String getlocationCity(double lat, double lonng) {
		String location = "";
		JSONArray ja;
		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
				+ lat+"," + lonng + "&sensor=true";
		StringBuffer json = StringHelper.readURLContent(url);
		try {
			System.out.println("URL " + url);

			JSONObject myjson = new JSONObject(json.toString());

			ja = (JSONArray) myjson.get("results");
			JSONArray add= (JSONArray) ((JSONObject) ja.get(0)).get("address_components");
			
			JSONObject city = (JSONObject) add.get(4);
			location=city.getString("long_name");
			System.out.println("......................................"+location);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return location;

	}

	public static String[] getLocation(String address){
		String addresspincode[]=new String[2];
		String add = " ";
		String pincode=" ";
		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
				+ address + "&sensor=true";
		try {System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			if(address.equalsIgnoreCase("0.0,0.0")){
				add="";
				addresspincode[0]=add;
				addresspincode[1]=pincode;
				return addresspincode;
			}
		System.out.println("URL " + url);
		BufferedReader in = new BufferedReader(new InputStreamReader(new URL(
				url).openStream()));
		String line;
		StringBuffer json = new StringBuffer();
		
		int statusCode = -1;
		while ((line = in.readLine()) != null)
			json.append(line);
		JSONArray ja;
		
		
			JSONObject myjson = new JSONObject(json.toString());
			// ja = new JSONArray(json.toString());
			ja = (JSONArray) myjson.get("results");
			// myjson = new JSONObject(json.toString());
			myjson.get("results");
			Object last = ((JSONObject) ja.get(0)).get("formatted_address");
			
			JSONArray jarray=(JSONArray)(((JSONObject) ja.get(0)).get("address_components"));
			for(int i=0;i<jarray.length();i++){
				JSONObject jo=jarray.getJSONObject(i);
				
				JSONArray types= (JSONArray )jo.get("types");
				for(int j=0;j<types.length();j++){
					if(types.getString(0).equalsIgnoreCase("postal_code")){
						pincode=jo.get("long_name").toString();
						break;
					}
					
				}
				
			}
			System.out.println(last);
			System.out.println("pincode "+pincode);
		
			
			add = last.toString();
			System.out.println("Final adrress is---" + add);
			addresspincode[0]=add;
			addresspincode[1]=pincode;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return addresspincode;
	}

	public static void main(String[] argv) throws Exception {
		String[] arr=Geocoder.getLocation("28.4992907,73.7938519");
		System.out.println("==================");
		String test = arr[0] +" "+arr[1]; 
		System.out.println("Test "+test);
		HashMap map = Geocoder.getDIstanceTimeDetails("Pune Station Maharashtra", test);
		String i =(String) map.get("DISTANCE");
		System.out.println("Distance " +i);
		Scanner st = new Scanner(i);
        while (!st.hasNextDouble())
        {
            st.next();
        }
        double value = st.nextDouble();
        System.out.println(value);
	}

	public static HashMap getDIstanceTimeDetails(String srcLocationName,
			String destLocationName) {
		String[] startlatlong = new String[2];
		String[] endlatlong = new String[2];
		HashMap<String, Object> dataMap = new HashMap();
		String distance = "";
		String time = "";

		srcLocationName = URLEncoder.encode(srcLocationName);
		destLocationName = URLEncoder.encode(destLocationName);

		String url = "http://maps.googleapis.com/maps/api/directions/json?origin="
				+ srcLocationName
				+ "&destination="
				+ destLocationName
				+ "&sensor=false&mode=driving";
		JSONArray ja;
		try {
			System.out.println("URL " + url);
			StringBuffer json = StringHelper.readURLContent(url);
			JSONObject myjson = new JSONObject(json.toString());

			ja = (JSONArray) myjson.get("routes");

			JSONArray legs = (JSONArray) ((JSONObject) ja.get(0)).get("legs");

			JSONObject last = (JSONObject) legs.get(0);

			JSONObject distanceObject = (JSONObject) last.get("distance");

			distance = distanceObject.getString("text");
			System.out.println("distanceeeeeeeeeee " + distance);

			JSONObject durationObject = (JSONObject) last.get("duration");
			time = durationObject.getString("text");
		

		
		} catch (Exception e) {
			e.printStackTrace();
			endlatlong[0] = "-1";
			endlatlong[1] = "-1";
			startlatlong[0] = "-1";
			startlatlong[1] = "-1";
			distance = "-1";
			time = "-1";
		} finally {
			dataMap.put("START_LATLNG", startlatlong);
			dataMap.put("END_LATLNG", endlatlong);
			dataMap.put("DISTANCE", distance);
			dataMap.put("TIME", time);
	
			System.out.println("Test for Source lat long		"+dataMap);
		}
		return dataMap;
		

	}

}
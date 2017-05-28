package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class File_Open {
	public static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
	
	public static void readStreamJSON(String filename) {
	    try {
	    	
	        JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
	        Gson gson = new GsonBuilder().create();

	        // Read file in stream mode
	        reader.beginArray();
	        while (reader.hasNext()) {
	            // Read data into object model
	            /*Person person = gson.fromJson(reader, Person.class);
	            if (person.getId() == 0 ) {
	                System.out.println("Stream mode: " + person);
	            }
	            break;*/
	        }
	        reader.close();
	    } catch (UnsupportedEncodingException ex) {
	        
	    } catch (IOException ex) {
	       
	    }
	}
}

package org.yamini.simpledb;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.yamini.Scheduler.SchoolSchedule;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.DeleteDomainRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;
import com.amazonaws.services.simpledb.model.SelectRequest;
public class DBManage {
	 String myDomain = "yamini-Schedule";
	 PrintWriter out;
	 AmazonSimpleDB sdb;
		
	 // Constructor
	 public DBManage(PrintWriter out) throws IOException
	 {
	   // Location to write output to
	   this.out = out;
	   // SimpleDB management client (based on login stored in AwsCredentials.properties file)
	   sdb = new AmazonSimpleDBClient(new PropertiesCredentials(
	   this.getClass().getClassLoader().getResourceAsStream("AwsCredentials.properties")));
	 }
		
	 // Create Domain (i.e. a "table" to store data)
	 public void createDomain()
	 {
	   out.println
	   ("<!DOCTYPE html>\n" +
	   "<html>\n" +
	   "<head><title>DBManage:createDomain()</title></head>\n" +
	   "<body>\n" +
	   "<h1>Result of page:</h1>\n" +
	   "<pre>\n");
			
	   out.println("===========================================");
	   out.println("Creating SimpleDB domain");
	   out.println("===========================================\n");

	   try {
	     // Create a domain
	     out.println("Creating domain called " + myDomain + ".\n");
	     sdb.createDomain(new CreateDomainRequest(myDomain));
			
	     // List domains
	     out.println("Listing all domains in your account:\n");
	     for (String domainName : sdb.listDomains().getDomainNames()) {
	       out.println("  " + domainName);
	     }
	     out.println();           
	           
	     } catch (AmazonServiceException ase) {
	       handleExceptions(ase);
	     } catch (AmazonClientException ace) {
	       handleExceptions(ace);
	     }
			
	   out.println("<pre>\n" +
	   "</body></html>");	
	 }

	 // Handle exceptions produced by SimpleDB operations
	 // Two types: ServiceExceptions and ClientExceptions
	 private void handleExceptions(AmazonServiceException ase)
	 {
	   out.println("<pre>");
	   out.println("Caught an AmazonServiceException, which means your request made it "
	   + "to Amazon SimpleDB, but was rejected with an error response for some reason.");
	   out.println("Error Message:    " + ase.getMessage());
	   out.println("HTTP Status Code: " + ase.getStatusCode());
	   out.println("AWS Error Code:   " + ase.getErrorCode());
	   out.println("Error Type:       " + ase.getErrorType());
	   out.println("Request ID:       " + ase.getRequestId());	
	   out.println("</pre>");
	 }
		
	 private void handleExceptions(AmazonClientException ace)
	 {
	   out.println("<pre>");
	   out.println("Caught an AmazonClientException, which means the client encountered "
	   + "a serious internal problem while trying to communicate with SimpleDB, "
	   + "such as not being able to access the network.");
	   out.println("Error Message: " + ace.getMessage());
	   out.println("</pre>");
	 }
	// Store a class entry into a Domain previously created
	 public void storeClass(String title, int starttime, int endtime, int day)
	 {
	   try {
	     // Put data into a domain            		            
	     ArrayList<ReplaceableAttribute> newAttributes = new ArrayList<ReplaceableAttribute>();
	     newAttributes.add(new ReplaceableAttribute("Title", title, false));
	     newAttributes.add(new ReplaceableAttribute("Starttime", Integer.toString(starttime), false));
	     newAttributes.add(new ReplaceableAttribute("Endtime", Integer.toString(endtime), false));
	     newAttributes.add(new ReplaceableAttribute("Day", Integer.toString(day), false));
	     PutAttributesRequest newRequest = new PutAttributesRequest();
	     newRequest.setDomainName(myDomain);
	     newRequest.setItemName(UUID.randomUUID().toString()); // Random item name
	     newRequest.setAttributes(newAttributes);

	     sdb.putAttributes(newRequest); 
	   } catch (AmazonServiceException ase) {
	     handleExceptions(ase);
	   } catch (AmazonClientException ace) {
	     handleExceptions(ace);
	   }
	 }
	// Populate a new SchoolSchedule with data previously saved into a domain
	 public SchoolSchedule loadSchedule()
	 {
	   SchoolSchedule newSchedule = new SchoolSchedule();
	 		
	   String title="";
	   int startTime=0;
	   int endTime=0;
	   int day=0;
	 			
	   try {
	     // Select data from a domain
	     // Notice the use of backticks around the domain name in our select expression.
	     //String selectExpression = "select * from `" + myDomain + "` where Category = 'Clothes'";
	     String selectExpression = "select * from `" + myDomain + "`";
	     SelectRequest selectRequest = new SelectRequest(selectExpression);
	     for (Item item : sdb.select(selectRequest).getItems()) {
	       // For item name, use: item.getName());
	       //out.println("Name=" + item.getName() + ",Attribute=" + attribute.getName() + ",Value=" + attribute.getValue());
	             	
	       // Got a single item from SimpleDB
	       for (Attribute attribute : item.getAttributes()) {
	         if(attribute.getName().equals("Title"))
	           title = attribute.getValue();
	         else if(attribute.getName().equals("Starttime"))
	           startTime = Integer.valueOf(attribute.getValue());
	         else if(attribute.getName().equals("Endtime"))
	           endTime = Integer.valueOf(attribute.getValue());
	         else if(attribute.getName().equals("Day"))
	           day = Integer.valueOf(attribute.getValue());
	       }
	                 
	       out.println("<pre>");
	       out.println("Loading SchoolClass from SimpleDB: "
	       + "title=" + title
	       + ",startTime=" + startTime
	       + ",endTime=" + endTime
	       + ",day=" + day);
	       out.println("</pre>");
	       newSchedule.addClass(new SchoolClass(title, startTime, endTime, day));
	     } 
	   } catch (AmazonServiceException ase) {
	     handleExceptions(ase);
	   } catch (AmazonClientException ace) {
	     handleExceptions(ace);
	   }
	   return newSchedule;
	 }
}


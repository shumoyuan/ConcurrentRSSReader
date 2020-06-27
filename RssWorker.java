package Rss;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import java.io.*;
import java.nio.file.Files;

public class RssWorker {
    	String fileName = "rssadd.txt";
	    static CopyOnWriteArrayList<String> threadUriList = new CopyOnWriteArrayList<String>();
	    static CopyOnWriteArrayList<String> threadNewDate = new CopyOnWriteArrayList<String>();
	    static boolean testSwitch = true; //for testing, when true, force update all, when false, just update the renew ones
		
    public String RssWorker() throws Exception {
	    String uri;
    	String savedDate;
	    String fileName = "rssadd.txt";
	    
	    //for sequential
	    InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName));
	    BufferedReader br = new BufferedReader(reader);
	    ArrayList<String> uriList = new ArrayList<String>();
	    ArrayList<String> newDate = new ArrayList<String>();
	    long startTime = System.currentTimeMillis();
	    
	    String result = "\r\n";
	    
	    //RandomAccessFile outputFile = new RandomAccessFile(startTime+".html", "rw");
	    
	    String line;
	    while ((line = br.readLine()) != null){
	    	int blankIndex = line.indexOf(" ");
	    	uri = line.substring(0,blankIndex);
	    	savedDate = line.substring(blankIndex+1,line.length());
	    	
	    	RSSFeedParser parser = new RSSFeedParser(uri);
	    	Feed feed = parser.readFeed();
	    	uriList.add(uri);
	    	if(feed.pubDate!=" ") {
	    		newDate.add(feed.pubDate);
	    	}
	    	else {
	    		newDate.add("0");
	    	}
	    	
		    if(feed.pubDate != savedDate || testSwitch)
		    {
		    	if(feed.description.isEmpty())
		    	{
		    		result = result +feed.title+"\r\n";
		    		//outputFile.writeBytes("<p><a href= \""+feed.link+"\" style=\"font-size:300%\">"+feed.title+"</a></p>");
		    	}
		    	else {
		    		result = result + feed.description+feed.title+"\r\n";
		    		//outputFile.writeBytes("<p><a href= \""+feed.description+"\" style=\"font-size:300%\">"+feed.title+"</a></p>");
		    	}
		    	result = result + feed.pubDate+"\r\n"+"\r\n";
		    	//outputFile.writeBytes(feed.pubDate+"<br><br>");
			    //System.out.println(feed);
			    for (FeedMessage message : feed.getMessages()) {
			    	result = result + message.title.replace("\r","").replace("\n","") +"\r\n";
			    	result = result + message.link +"\r\n";
			    	//outputFile.writeBytes("<p><a href= \""+message.link+"\">"+message.title+"</a>"+"<br>");
			    	if(!message.author.isEmpty())
			    	{
			    		result = result + message.author+"\r\n";
			    		//outputFile.writeBytes(message.author+"<br>");
			    	}
			    	result = result + message.description.replace("\r","").replace("\n","").replace("	", "").replace("  ", " ")+"\r\n\r\n";
			    	//outputFile.writeBytes(message.description+"<br></p>");	    	
			    	//System.out.println(message);
			    } 
		    }
	    }
	    
	    //result = result + "</html>";
	    //outputFile.writeBytes("</html>");
	    //outputFile.close();
	    reader.close();
	    br.close();
	    
	    FileWriter fileWriter =new FileWriter(fileName);
	    fileWriter.write("");
	    fileWriter.flush();
	    fileWriter.close();
	    
	    RandomAccessFile rf = new RandomAccessFile(fileName, "rw");
	    for(int i = 0; i < uriList.size(); i++)
	    {
	    	rf.writeBytes(uriList.get(i) + " "+newDate.get(i) +"\r\n");
	    }
	    
	    long endTime = System.currentTimeMillis();
	    
	    System.out.println("Total time is "+(endTime-startTime)+" ms.");
	    rf.close();
	    uriList.clear();
	    newDate.clear();
	    
	    return result;
    }
}
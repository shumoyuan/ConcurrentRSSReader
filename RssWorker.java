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
		
    public static void main(String[] args) throws Exception {
	    String uri;
    	String savedDate;
	    String fileName = "rssadd.txt";
	    
	    //for sequential
	    InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName));
	    BufferedReader br = new BufferedReader(reader);
	    ArrayList<String> uriList = new ArrayList<String>();
	    ArrayList<String> newDate = new ArrayList<String>();
	    long startTime = System.currentTimeMillis();
	    
	    RandomAccessFile outputFile = new RandomAccessFile(startTime+".html", "rw");
	    outputFile.writeBytes("<html>");
	    outputFile.writeBytes("<head><meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\"></head>");
	    
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
		    	if(feed.title.isEmpty())
		    	{
		    		outputFile.writeBytes("<p><a href= \""+feed.link+"\" style=\"font-size:300%\">"+feed.description+"</a></p>");
		    	}
		    	else {
		    		outputFile.writeBytes("<p><a href= \""+feed.link+"\" style=\"font-size:300%\">"+feed.title+"</a></p>");
		    	}
		    	outputFile.writeBytes(feed.pubDate+"<br><br>");
			    //System.out.println(feed);
			    for (FeedMessage message : feed.getMessages()) {
			    	outputFile.writeBytes("<p><a href= \""+message.link+"\">"+message.title+"</a><br>");
			    	if(!message.author.isEmpty())
			    	{
			    		outputFile.writeBytes(message.author+"<br>");
			    	}
			    	outputFile.writeBytes(message.description+"<br></p>");	    	
			    	//System.out.println(message);
			    } 
		    }
	    }
	    
	    outputFile.writeBytes("</html>");
	    outputFile.close();
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
	    
	    //for concurrent
	    reader = new InputStreamReader(new FileInputStream(fileName));
	    br = new BufferedReader(reader);
	    startTime = System.currentTimeMillis();
	    List<Thread> list= new ArrayList<Thread>();
	    
	    RandomAccessFile outputFileCon = new RandomAccessFile(startTime+".html", "rw");
	    outputFileCon.writeBytes("<html>");
	    outputFileCon.writeBytes("<head><meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\"></head>");
	    
	    while ((line = br.readLine()) != null){
	    	int blankIndex = line.indexOf(" ");
	    	uri = line.substring(0,blankIndex);
	    	savedDate = line.substring(blankIndex+1,line.length());
	    	Thread rss = new RssThread(uri,savedDate,outputFileCon);
	    	list.add(rss);
	    	rss.start();
	    }
	    
    	try {
    		for(Thread thread:list) {
    			thread.join();
    		}
    	}
    	catch(InterruptedException e)
    	{
    		e.printStackTrace();
    	}
    	
	    outputFileCon.writeBytes("</html>");
	    outputFileCon.close();
	    
	    reader.close();
	    br.close();
	    
	    fileWriter =new FileWriter(fileName);
	    fileWriter.write("");
	    fileWriter.flush();
	    fileWriter.close();
	    
	    rf = new RandomAccessFile(fileName, "rw");
	    for(int i = 0; i < threadUriList.size(); i++)
	    {
	    	rf.writeBytes(threadUriList.get(i) + " "+threadNewDate.get(i) +"\r\n");
	    }
	       
	    endTime = System.currentTimeMillis();
	    
	    System.out.println("Total time is "+(endTime-startTime)+" ms.");
	    rf.close();
	    threadUriList.clear();
	    threadNewDate.clear();	    
    }
    
	static class RssThread extends Thread{
	    String uri;
    	String savedDate;
    	RandomAccessFile outputFileCon;
    	

    	RssThread(String uri, String savedDate,RandomAccessFile outputFileCon)
    	{
	        this.uri = uri;
	        this.savedDate = savedDate;
	        this.outputFileCon = outputFileCon;
    	}
			
	    public void run() 
	    {
	    	RSSFeedParser parser = new RSSFeedParser(uri);
		    Feed feed = parser.readFeed();
		    StringBuffer resStr = new StringBuffer();
		    threadUriList.add(uri);
		    if(feed.pubDate!=" ") {
		    	threadNewDate.add(feed.pubDate);
		    }
		    else {
		    	threadNewDate.add("0");
		    }
		    if(feed.pubDate != savedDate || testSwitch)
		    {
		    	if(feed.title.isEmpty())
		    	{
		    		resStr.append("<p><a href= \""+feed.link+"\" style=\"font-size:300%\">"+feed.description+"</a></p>");
		    		//outputFile.writeBytes("<p><a href= \""+feed.link+"\" style=\"font-size:300%\">"+feed.description+"</a></p>");
		    	}
		    	else {
		    		resStr.append("<p><a href= \""+feed.link+"\" style=\"font-size:300%\">"+feed.title+"</a></p>");
		    		//outputFile.writeBytes("<p><a href= \""+feed.link+"\" style=\"font-size:300%\">"+feed.title+"</a></p>");
		    	}
		    }
		    resStr.append(feed.pubDate+"<br><br>");
			    	//outputFileCon.writeBytes(feed.pubDate+"<br><br>");
				   //System.out.println(feed);
			for (FeedMessage message : feed.getMessages()) {
				resStr.append("<p><a href= \""+message.link+"\">"+message.title+"</a><br>");
							//outputFileCon.writeBytes("<p><a href= \""+message.link+"\">"+message.title+"</a><br>");
				if(!message.author.isEmpty())
				{
					resStr.append(message.author+"<br>");
					//outputFileCon.writeBytes(message.author+"<br>");
				}
				resStr.append(message.description+"<br></p>");
				//outputFileCon.writeBytes(message.description+"<br></p>");    	 		
		    }
			synchronized (this){
				try {
					String res = new String(resStr);
					outputFileCon.writeBytes(res);
				}
				catch (Exception e) {
				}
			}
		}	
	}
}
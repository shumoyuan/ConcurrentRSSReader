package Rss;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Callable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import java.io.*;

public class ConcurrentWorker {
	String fileName = "rssadd.txt";
    static CopyOnWriteArrayList<String> threadUriList = new CopyOnWriteArrayList<String>();
    static CopyOnWriteArrayList<String> threadNewDate = new CopyOnWriteArrayList<String>();
    static boolean testSwitch = true; //for testing, when true, force update all, when false, just update the renew ones
    
    public String ConcurrentWorker() throws Exception {
	    String uri;
    	String savedDate;
	    String fileName = "rssadd.txt";
	    
	    InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName));
	    BufferedReader br = new BufferedReader(reader);
	    List<Future> list= new ArrayList<Future>();
	    long startTime = System.currentTimeMillis();
	    ExecutorService pool = Executors.newFixedThreadPool(10);
	    
	    String result = "\r\n";
	    //Atomic<String> result = "\r\n";
	    
	    //RandomAccessFile outputFileCon = new RandomAccessFile(startTime+".html", "rw");
	    
	    String line;
	    while ((line = br.readLine()) != null){
	    	int blankIndex = line.indexOf(" ");
	    	uri = line.substring(0,blankIndex);
	    	savedDate = line.substring(blankIndex+1,line.length());
	    	RssThread rss = new RssThread(uri,savedDate);
			Future future = pool.submit(rss);
			list.add(future);
	    }
    	try {
    		for(Future future:list) {
    			result= result+future.get();
    		}
    	}
    	catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

	    
	    pool.shutdown();
	    //outputFileCon.writeBytes("</html>");
	    //outputFileCon.close();
	    
	    reader.close();
	    br.close();
	    
	    FileWriter fileWriter =new FileWriter(fileName);
	    fileWriter.write("");
	    fileWriter.flush();
	    fileWriter.close();
	    
	    RandomAccessFile rf = new RandomAccessFile(fileName, "rw");
	    for(int i = 0; i < threadUriList.size(); i++)
	    {
	    	rf.writeBytes(threadUriList.get(i) + " "+threadNewDate.get(i) +"\r\n");
	    }
	       
	    long endTime = System.currentTimeMillis();
	    
	    System.out.println("Total time is "+(endTime-startTime)+" ms.");
	    rf.close();
	    threadUriList.clear();
	    threadNewDate.clear();
	    
	    return result.toString();
    }
    
	public class RssThread implements Callable{
	    String uri;
    	String savedDate;
    	
    	RssThread(String uri, String savedDate)
    	{
	        this.uri = uri;
	        this.savedDate = savedDate;
    	}
			
	    public String call() 
	    {
	    	StringBuffer tmpString = new StringBuffer();
	    	RSSFeedParser parser = new RSSFeedParser(uri);
		    Feed feed = parser.readFeed();
		    threadUriList.add(uri);
		    if(feed.pubDate!=" ") {
		    	threadNewDate.add(feed.pubDate);
		    }
		    else {
		    	threadNewDate.add("0");
		    }
		    if(feed.pubDate != savedDate || testSwitch)
		    {
		    	if(feed.description.isEmpty())
		    	{
		    		tmpString.append(feed.title+"\r\n");
		    		//outputFileCon.writeBytes("<a href= \""+feed.link+"\">"+feed.title+"</a>"+"<br>");
		    	}
		    	else {
		    		tmpString.append(feed.description+feed.title+"\r\n");
		    		//outputFileCon.writeBytes("<a href= \""+feed.description+"\">"+feed.title+"</a>"+"<br>");
		    	}
		    	tmpString.append(feed.pubDate+"\r\n"+"\r\n");
		    	//outputFileCon.writeBytes(feed.pubDate+"<br><br>");
		    	//System.out.println(feed);
		    	for (FeedMessage message : feed.getMessages()) {
		    		tmpString.append(message.title.replace("\r","").replace("\n","") +"\r\n");
		    		tmpString.append(message.link +"\r\n");
		    		//outputFileCon.writeBytes("<a href= \""+message.link+"\">"+message.title+"</a>"+"<br>");
		    		if(!message.author.isEmpty())
		    		{
		    			tmpString.append(message.author+"\r\n");
		    			//outputFileCon.writeBytes(message.author+"<br>");
		    		}
		    		tmpString.append(message.description.replace("\r","").replace("\n","").replace("	", "").replace("  ", " ")+"\r\n\r\n");
		    		//outputFileCon.writeBytes(message.description+"<br><br>");    	
		    		//System.out.println(message);
		    	}
		    	
		    }
			String res = new String(tmpString);
		    return res;
		}
	    
	}
}

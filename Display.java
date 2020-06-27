package Rss;

import java.awt.*; 
import javax.swing.*; 
import java.io.*;
import java.util.ArrayList;
import java.awt.event.*; 
import javax.swing.plaf.metal.*; 
import javax.swing.text.*;

class Display extends JFrame implements ActionListener { 
    // Text component 
    //JEditorPane t;
	JTextArea t; 
  
    // Frame 
    JFrame f; 
  
    // Constructor 
    Display() 
    { 
        // Create a frame 
        f = new JFrame("editor"); 
  
        try { 
            // Set metl look and feel 
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); 
  
            // Set theme to ocean 
            MetalLookAndFeel.setCurrentTheme(new OceanTheme()); 
        } 
        catch (Exception e) { 
        } 
        //t = new JEditorPane();
        t = new JTextArea();
        t.setLineWrap(true);
        // Text component 
        
         
        // Create a menubar 
        JMenuBar mb = new JMenuBar(); 
  
        // Create amenu for menu 
        JMenu m1 = new JMenu("RssFeed"); 
  
        // Create menu items 
        JMenuItem mi1 = new JMenuItem("New"); 
        JMenuItem mi2 = new JMenuItem("Open"); 
        JMenuItem mi3 = new JMenuItem("Save"); 
    //    JMenuItem mi9 = new JMenuItem("Print"); 
  
        // Add action listener 
        mi1.addActionListener(this); 
        mi2.addActionListener(this); 
        mi3.addActionListener(this); 
 
  
        m1.add(mi1); 
        m1.add(mi2); 
        m1.add(mi3); 

        // Add action listener 

        
        JMenu m3 = new JMenu("Run");
        JMenuItem mi7 = new JMenuItem("Sequential Get"); 
        JMenuItem mi8 = new JMenuItem("Concurrent Get"); 
        
        mi7.addActionListener(this);
        m3.add(mi7);
        mi8.addActionListener(this);
        m3.add(mi8);
  
        JMenuItem mc = new JMenuItem("close"); 
  
        mc.addActionListener(this); 
  
        mb.add(m1); 
        mb.add(m3);
        mb.add(mc); 
     
        
        f.setJMenuBar(mb); 
        f.add(t); 
        f.setSize(500, 500); 
        f.setVisible(true);
        JScrollPane scroll = new JScrollPane(t); 
        f.add(scroll);
    } 
  
    // If a button is pressed 
    public void actionPerformed(ActionEvent e) 
    { 
        String s = e.getActionCommand(); 
  
        if (s.equals("cut")) { 
            t.cut(); 
        } 
        else if (s.equals("copy")) { 
            t.copy(); 
        } 
        else if (s.equals("paste")) { 
            t.paste(); 
        } 
        else if (s.equals("Sequential Get")) {
        	RssWorker m=new RssWorker();
    	    String ob = null;
			try {
				ob = m.RssWorker();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            /*ArrayList<String> list=new ArrayList<String>();
            String smm="";
            for(int i=0;i<ob.size();i++) {
    			String p=ob.getString(i);
    			String[] temp=p.split("[,{}\"]");
    			int j=0;
                while(j<temp.length-2) {
                	
                	if(temp[j].equals("author")||temp[j].equals("description")||temp[j].equals("link")||temp[j].equals("title")||temp[j].equals("guid")) {
                		if(temp[j].equals("author")) {
                			int no=i+1;
                			smm=smm+"\n"+"Content"+no+"\n";
                		}
                		smm=smm+temp[j]+temp[j+1]+temp[j+2]+"\n";
                		j=j+3;
                	}
                	else {j=j+1;}
                	}
                   smm=smm+"\n";
                }*/
            
            t.setText(ob);
        } 
        else if (s.equals("Concurrent Get")) {
        	ConcurrentWorker m=new ConcurrentWorker();
    	    String ob = null;
			try {
				ob = m.ConcurrentWorker();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            /*ArrayList<String> list=new ArrayList<String>();
            String smm="";
            for(int i=0;i<ob.size();i++) {
    			String p=ob.getString(i);
    			String[] temp=p.split("[,{}\"]");
    			int j=0;
                while(j<temp.length-2) {
                	
                	if(temp[j].equals("author")||temp[j].equals("description")||temp[j].equals("link")||temp[j].equals("title")||temp[j].equals("guid")) {
                		if(temp[j].equals("author")) {
                			int no=i+1;
                			smm=smm+"\n"+"Content"+no+"\n";
                		}
                		smm=smm+temp[j]+temp[j+1]+temp[j+2]+"\n";
                		j=j+3;
                	}
                	else {j=j+1;}
                	}
                   smm=smm+"\n";
                }*/
            
            t.setText(ob);
        } 
        else if (s.equals("Save")) { 
            // Create an object of JFileChooser class 
            JFileChooser j = new JFileChooser("f:"); 
  
            // Invoke the showsSaveDialog function to show the save dialog 
            int r = j.showSaveDialog(null); 
  
            if (r == JFileChooser.APPROVE_OPTION) { 
  
                // Set the label to the path of the selected directory 
                File fi = new File(j.getSelectedFile().getAbsolutePath()); 
  
                try { 
                    // Create a file writer 
                    FileWriter wr = new FileWriter(fi, false); 
  
                    // Create buffered writer to write 
                    BufferedWriter w = new BufferedWriter(wr); 
                    String[] textArray = t.getText().split("\n"); //"2:3:4:5".split(":")//将返回["2", "3", "4", "5"]
                    StringBuffer text = new StringBuffer();
                    for(int i=0;i<textArray.length;i++)
                    {
                    text.append(textArray[i]+"\n");
                    }
                    String ss=text.toString();
                    w.write(ss); 
                    w.flush(); 
                    w.close(); 
  
                    // Write 
                    w.write(t.getText()); 
                    w.flush(); 
                    w.close(); 
                } 
                catch (Exception evt) { 
                    JOptionPane.showMessageDialog(f, evt.getMessage()); 
                } 
            } 
            // If the user cancelled the operation 
            else
                JOptionPane.showMessageDialog(f, "the user cancelled the operation"); 
        } 
        else if (s.equals("Print")) { 
            try { 
                // print the file 
                t.print(); 
            } 
            catch (Exception evt) { 
                JOptionPane.showMessageDialog(f, evt.getMessage()); 
            } 
        } 
        else if (s.equals("Open")) { 
            // Create an object of JFileChooser class 
            JFileChooser j = new JFileChooser("f:"); 
  
            // Invoke the showsOpenDialog function to show the save dialog 
            int r = j.showOpenDialog(null); 
  
            // If the user selects a file 
            if (r == JFileChooser.APPROVE_OPTION) { 
                // Set the label to the path of the selected directory 
                File fi = new File(j.getSelectedFile().getAbsolutePath()); 
  
                try { 
                    // String 
                    String s1 = "", sl = ""; 
  
                    // File reader 
                    FileReader fr = new FileReader(fi); 
  
                    // Buffered reader 
                    BufferedReader br = new BufferedReader(fr); 
  
                    // Initilize sl 
                    sl = br.readLine(); 
  
                    // Take the input from the file 
                    while ((s1 = br.readLine()) != null) { 
                        sl = sl + "\n" + s1; 
                    } 
  
                    // Set the text 
                    t.setText(sl); 
                } 
                catch (Exception evt) { 
                    JOptionPane.showMessageDialog(f, evt.getMessage()); 
                } 
            } 
            // If the user cancelled the operation 
            else
                JOptionPane.showMessageDialog(f, "the user cancelled the operation"); 
        } 
        else if (s.equals("New")) { 
            t.setText(""); 
        } 
        else if (s.equals("close")) { 
            f.setVisible(false); 
        } 
    } 
  
    // Main class 
public static void main(String args[]) 
    { 
    	Display e = new Display(); 
    } 
} 

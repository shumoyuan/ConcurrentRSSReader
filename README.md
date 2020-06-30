# ConcurrentRSSReader

Design of an RSS reader by java and XML. The RSS readers automatically check a series of RSS feeds for new
items on an ongoing basis, making it is possible to keep track of changes to mutilple
websites without needing to tediously read and re-read each of the websites yourself. Its main functions
include support for subscriptions and modification of RSS channels, reading of subscriptions content, and 
support for background sync updates.

Instruction:

The main function is in Display.java, set the main function path and compile as a jar, then you can
run it with the command below:
java -jar xxxx.jar

Remember to put the file rssadd.txt in the same path with jar file

Then, you can click "Run" and choose "Sequential Get" or "Concurrent Get", the final result will show
below, and the runtime will show in the terminal after each processing.

To clear the interface, click "RSSFeed" then click "New"

To add more RSS addresses from file, click "RSSFeed" then click "Open", choose the file has these addresses
And then click "Save".

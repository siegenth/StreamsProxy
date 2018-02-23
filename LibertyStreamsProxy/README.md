# LibertyStreamsProxy Servlet - readme

Dynamically build a bridge between the open Web and a Streams application running in IBM Cloud.

The servlet, running on BlueMix's *Liberty for Java Foundry App*, locates the connected Streams *Streaming Analytics* server and
binds it Liberty's web address via an embedded proxy. The proxy configuration occurs on the first access from the web. 

The servlet works in conjunction with Streams' INET toolkit (https://github.com/IBMStreams/streamsx.inet), transparently. 
Streams applications that are accessed via HTTP or REST in a development environment with no changes to the proxy server.

The goal of this servlet, to easily move a Streams application from development to the web, for demonstration purposes.
Leaving the application up may incur significant resource usage by nefarious web trolls.


## Demonstration walk through. 

Assumed:
 * You have a [Bluemix account](https://console.ng.bluemix.net/registration/). 
 * You have downloaded and installed [cf](https://github.com/cloudfoundry/cli#downloads) and [Bluemix](https://console.bluemix.net/docs/starters/install_cli.html) command-line tools. 
 
Provided in Box:
 * UpperRest.sab : The Streams application bundle file, with the demonstration Streams application, source below. 
 * A video walking through the 


Overview of Steps.
1. Build Streams application, generate sab.
2. Allocate and attach resources in Bluemix. 
3. Start the Streams application. 
4. Start the liberty application. 
5. Access the Streams application running in Bluemix. 


### The Streams application.

Below is a simple Streams demo application, in conjunction with the servlet, it implements a REST service to uppercase a string. 

```
namespace application ;

use com.ibm.streamsx.inet.rest::HTTPRequestProcess ;
use com.ibm.streamsx.inet.http::HTTPRequest ;

composite UpperRest
{
	graph
		(stream<HTTPRequest> httpRequest) as rest =
			HTTPRequestProcess(httpResponse)
		{
			param
				context : "myStreams" ;
				contextResourceBase : "/dev/null" ;
				port : 8080 ;
		}

		(stream<HTTPRequest> httpResponse as O) as doWork = Functor(httpRequest as I)
		{
			output
				O : response = upper(I.request) ;
		}
}
```
Invoking the URL, "http://localhost:8080/myStreams/rest/ports/analyze/0?to_upper_case" in your Streams development environment look like...

```
$ curl http://localhost:8080/myStreams/rest/ports/analyze/0?to_upper_case
TO_UPPER_CASE
```

The application uses the **HTTPRequestProcess()** operator of the INET toolkit.


A built version of the program, UpperRest.sab, can be found in BOX. 

### Create a Liberty for Java Cloud Foundry App on Bluemix.

1. Login into  your Blumemix account.
2. Create an application, on the 'Apps' page select 'Create App' button. 
3. Select 'Liberty for Java' under 'Cloud Foundry Apps'. 
4. Provide a 'App name:', for the demo I'll use 'LibertyRiver'. 

### Create Streams Service
1. Go to the 'Overview' section. 
2. Select 'Connect new'.
3. Select 'Streaming Analytics' in the 'Data & Analytics' section. 
4. Select 'Create'
5. On completion you'll be returned to the 'All Apps' page

### Connect Liberty App to Streams service. 
1. Select the *Liberty App* you created above (LibertyRiver). 
2. Select 'Connect existing' in the 'Connections' pane.
3. Under 'Services', select the 'Streaming Analytics...' service you created above then select 'Connect'. 
4. Select 'Restage' when prompted. 

### Start the Streams application. 
1. Go to the 'Connections' section. 
2. Select the 'Streaming Analytics...' tile. 
3. Select 'LAUNCH'.
4. Select the "Play" button to the right of 'Jobs'.
5. Browse to the location of of the Streams application bundle file (UpperRest.sab)
6. Select 'Submit'.

### Upload the LibertyStreamsProxy server. 
1. Go to the command-line.

2. Login to your bluemix account, my logging in looks like...

```
$ bluemix login  -o siegenth@us.ibm.com -s dev -sso
````
When prompted, follow directions for password. 

3. Change to the directory LibertyStreamsProxy.

4. Build the proxy server with maven, output will be in 'target' directory. 

```
mvn clean install
```

4. Upload the war file to IBM Cloud, using *Liberty App* name (LibertyRiver) and the war file (LibertyStreamsProxy-v1.0.war). 
```
$ bluemix cf push LibertyRiver -p target/LibertyStreamsProxy-v1.0.war.
```
On completion the application is ready to be accessed. 

### Test the application .
1. Build and texts the tests. 

```
 mvn clean test -DappUrl=LibertyRiver.mybluemix.net -DskipTests=false
```
2. Execute the application using curl, swap in your *Liberty App* name before executing 

*$ curl http://LibertyRiver.mybluemix.net/myStreams/rest/ports/analyze/0?upper_case*

The execution looks like...

```
$ curl http://libertyriver.mybluemix.net/myStreams/rest/ports/analyze/0?upper_case
UPPER_CASE
```

## Appendix : Notes :

Built versions of the files can be found on [box](https://ibm.box.com/s/gnofq4rd0910v1g9i3vavxgi3zola83u)


Shut down the web application with....

```
bluemix cf stop LibertyRiver
```





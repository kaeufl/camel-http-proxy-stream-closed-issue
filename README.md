Based on the camel-example-servlet-tomcat-no-spring example.

This demonstrates an issue that occurs when proxying an http endpoint that returns
a status code 304 (or potentially any other code listed in org.apache.http.protocol.HttpRequestExecutor#canResponseHaveBody) 
using a servlet in Tomcat.

We use the route definition
    
    from("servlet:///?matchOnUriPrefix=true")
        .to("http4://localhost:8080/respondWith/304?bridgeEndpoint=true&throwExceptionOnFailure=false");

to proxy an HTTP endpoint listening at

    http://localhost:8080/respondWith/{status_code} 
    
that replies to any GET request with status {status_code} and an empty response body.

##### Expected behaviour:

    curl -I http://localhost:8080/camel/
    
should produce the output

    HTTP/1.1 304
    # [...] lines omitted
    

##### Actual behaviour:
 
    curl -I http://localhost:8080/camel/
    
yields

    HTTP/1.1 500
    # [...] lines omitted

containing the response body

    <!doctype html><html lang="en"><head><title>HTTP Status 500 – Internal Server Error</title><style type="text/css">h1 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:22px;} h2 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:16px;} h3 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:14px;} body {font-family:Tahoma,Arial,sans-serif;color:black;background-color:white;} b {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;} p {font-family:Tahoma,Arial,sans-serif;background:white;color:black;font-size:12px;} a {color:black;} a.name {color:black;} .line {height:1px;background-color:#525D76;border:none;}</style></head><body><h1>HTTP Status 500 – Internal Server Error</h1><hr class="line" /><p><b>Type</b> Exception Report</p><p><b>Message</b> org.apache.camel.RuntimeCamelException: java.io.IOException: Stream closed</p><p><b>Description</b> The server encountered an unexpected condition that prevented it from fulfilling the request.</p><p><b>Exception</b></p><pre>javax.servlet.ServletException: org.apache.camel.RuntimeCamelException: java.io.IOException: Stream closed
    	org.apache.camel.http.common.CamelServlet.doService(CamelServlet.java:216)
    	org.apache.camel.http.common.CamelServlet.service(CamelServlet.java:74)
    	javax.servlet.http.HttpServlet.service(HttpServlet.java:742)
    	org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)
    </pre><p><b>Root Cause</b></p><pre>org.apache.camel.RuntimeCamelException: java.io.IOException: Stream closed
    	org.apache.camel.http.common.HttpMessage.createBody(HttpMessage.java:77)
    	org.apache.camel.impl.MessageSupport.getBody(MessageSupport.java:53)
    	org.apache.camel.http.common.DefaultHttpBinding.doWriteResponse(DefaultHttpBinding.java:391)
    	org.apache.camel.http.common.DefaultHttpBinding.writeResponse(DefaultHttpBinding.java:322)
    	org.apache.camel.http.common.CamelServlet.doService(CamelServlet.java:210)
    	org.apache.camel.http.common.CamelServlet.service(CamelServlet.java:74)
    	javax.servlet.http.HttpServlet.service(HttpServlet.java:742)
    	org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)
    </pre><p><b>Root Cause</b></p><pre>java.io.IOException: Stream closed
    	org.apache.catalina.connector.InputBuffer.read(InputBuffer.java:346)
    	org.apache.catalina.connector.CoyoteInputStream.read(CoyoteInputStream.java:152)
    	org.apache.camel.util.IOHelper.copy(IOHelper.java:196)
    	org.apache.camel.util.IOHelper.copy(IOHelper.java:169)
    	org.apache.camel.util.IOHelper.copyAndCloseInput(IOHelper.java:218)
    	org.apache.camel.util.IOHelper.copyAndCloseInput(IOHelper.java:214)
    	org.apache.camel.http.common.HttpHelper.readResponseBodyFromInputStream(HttpHelper.java:244)
    	org.apache.camel.http.common.HttpHelper.readRequestBodyFromServletRequest(HttpHelper.java:195)
    	org.apache.camel.http.common.DefaultHttpBinding.parseBody(DefaultHttpBinding.java:577)
    	org.apache.camel.http.common.HttpMessage.createBody(HttpMessage.java:75)
    	org.apache.camel.impl.MessageSupport.getBody(MessageSupport.java:53)
    	org.apache.camel.http.common.DefaultHttpBinding.doWriteResponse(DefaultHttpBinding.java:391)
    	org.apache.camel.http.common.DefaultHttpBinding.writeResponse(DefaultHttpBinding.java:322)
    	org.apache.camel.http.common.CamelServlet.doService(CamelServlet.java:210)
    	org.apache.camel.http.common.CamelServlet.service(CamelServlet.java:74)
    	javax.servlet.http.HttpServlet.service(HttpServlet.java:742)
    	org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)
    * Closing connection 0
    </pre><p><b>Note</b> The full stack trace of the root cause is available in the server logs.</p><hr class="line" /><h3>Apache Tomcat/8.5.16</h3></body></html>

### Build
    mvn package

### Run
    mvn cargo:run
    
Open <http://localhost:8080/> in your browser and click on the "Proxy route" link.

Above stack trace is displayed.
package org.apache.camel.example;

import org.apache.camel.builder.RouteBuilder;

public class ProxyRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
//        from("servlet:///?matchOnUriPrefix=true&eagerCheckContentAvailable=true")
        from("servlet:///?matchOnUriPrefix=true")
//            .convertBodyTo(String.class)
            .to("http4://localhost:8080/respondWith/304?bridgeEndpoint=true&throwExceptionOnFailure=false");
    }
}

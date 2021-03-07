package DiagnosticsClient.CommunicationTests;

import DiagnosticsClient.Communication.ClientPlatformConnection;
import DiagnosticsClient.Communication.ServerInformation;
import REST.Exception.RESTClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class PlatformConnectionTests {

    @Rule //Mock server on port 10500
    public WireMockRule rule = new WireMockRule(options().port(10500),false);

    private final ClientPlatformConnection connection = new ClientPlatformConnection(
            "http://localhost:10500/rest",
            "/client/register",
            "/client/assign",
            "/client/get_node/diagnostics_client");
    private final String json = "{\"id\":\"diagnostics_client\"}";


    @Test
    public void registrationRequestIsMade() throws RESTClientException {
        String path = "/rest/client/register";
        stubFor(post(urlEqualTo(path))
                .willReturn(status(200)));
        connection.register(json);
        verify(postRequestedFor(urlEqualTo(path))
                .withRequestBody(equalToJson(json)));
    }

    @Test
    public void assignRequestIsMadeAndServerIsObtained() throws RESTClientException, JsonProcessingException {
        String path1 = "/rest/client/assign";
        String path2 = "/rest/client/get_node/diagnostics_client";
        stubFor(post(urlEqualTo(path1))
                .willReturn(status(200)));
        stubFor(get(urlEqualTo(path2))
                .willReturn(aResponse()
                .withBody("{\"id\":\"diagnostics_server\",\"ipAddress\":\"172.19.112.1:4444\"}")
                .withStatus(200)));
        ServerInformation info = connection.getServer(json);
        verify(postRequestedFor(urlEqualTo(path1))
                .withRequestBody(equalToJson(json)));
        verify(getRequestedFor(urlEqualTo(path2)));
        String expectedAddress = "172.19.112.1";
        int expectedPort = 4444;
        Assert.assertEquals(expectedAddress,info.getIpAddress());
        Assert.assertEquals(expectedPort, info.getPort());

    }
}

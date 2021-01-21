/*
package InfrastructureManager.Modules.MatchMaking.OutputUnitTests;

import InfrastructureManager.Modules.CommonTestingMethods;
import InfrastructureManager.Modules.MatchMaking.MatchMakerType;
import InfrastructureManager.Modules.MatchMaking.MatchesList;
import InfrastructureManager.Modules.MatchMaking.Output.MatchMakerOutput;
import org.junit.Assert;
import org.junit.Test;

public class MatchMakingRandomTests {

    private final MatchesList matchesList = new MatchesList();
    private final MatchMakerOutput matchMaker = new MatchMakerOutput("mm", MatchMakerType.RANDOM, matchesList);

    @Test
    public void assignNodeToClientCompleteTest() throws InterruptedException {

        matchMaker.out("matchMaker register_node {\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true}");
        matchMaker.out("matchMaker register_client {\"id\":\"client1\"}");
        matchMaker.out("matchMaker assign_client client1");
        String result = matchesList.getLastAdded();
        String expected = "client1 {\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true,\"resource\":0,\"network\":0,\"location\":0,\"totalResource\":0,\"totalNetwork\":0}";
        Assert.assertEquals(expected,result);
    }
}

*/

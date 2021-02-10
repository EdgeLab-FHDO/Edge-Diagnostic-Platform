package InfrastructureManager.Modules.HistoryDiagnostic;


import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import java.util.HashMap;

/**
 * A class to store edge client disconnect and connect history.
 * May add more in this history package (such as where was the client)
 * @author Zero
 */
public class EdgeClientDCHistory {


    /**
     *  Data package should look like this
     *      Multimap with (string, boolean, TimeData).
     *      node - dc status - time
     *      So 1 node, with lots of dc status and timeData
     */

    /**
     * WANTED FUNCTION:
     * Get a list of this client recent connect and disconnect status (in a period of time)
     * - If disconnect a lot, then something is wrong
     *
     * Get a list of this client recent connect and disconnect to a node (in a period of time)
     * - If disconnect a lot (or frequent), then something is wrong too
     *
     *
     */
//=============================================Variable initiation===========================================
    String clientID; //this client ID
    String nodeID; //this client connected/disconnected nodeID
    boolean connected = true; // boolean variable to put in the map of the client

    //multimap with node ID - TimeData - connected bool
    private Multimap<String, Multimap<TimeData, Boolean>> historyMMap = LinkedListMultimap.create();

//============================================================================================================

    /**
     * put history related data into our multimap
     */
    private void inputHistory (){

    }

    /**
     * list of time where this client has been disconnected to a node
     * @param nodeID
     */
    private void getDisconnectedListToNode (String nodeID){

    }

    /**
     * list of time where this client has been connected to a node
     * @param nodeID
     */
    private void getConnectedListToNode (String nodeID){

    }

    /**
     * list of time where this client has been disconnected overall
     */
    private void getFullDCList (){

    }

    /**
     * list of time where this client has been connected and disconnected
     */
    private void getFullList() {

    }

    @Override
    public String toString(){
        return "Client";
    }


}

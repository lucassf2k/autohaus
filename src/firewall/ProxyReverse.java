package firewall;

import gateway.GatewayRemote;
import gateway.ImplGatewayRemote;
import gateway.Response;
import shared.Message;
import shared.MessageTypes;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;

public class ProxyReverse implements ProxyReverseProtocol {
    public static final int PORT = 20008;
    private final List<Integer> filters =  new ArrayList<>();
    private final GatewayRemote gatewayStub;

    public ProxyReverse() throws RemoteException, NotBoundException {
        final var registry = LocateRegistry.getRegistry(ImplGatewayRemote.PORT);
        this.gatewayStub = (GatewayRemote) registry.lookup("gateway");
    }

    public void setFilters(final List<Integer> ports) {
        filters.addAll(ports);
    }

    @Override
    public Response execute(final Package pack) {
        Response output = null;
        if (this.isAllowed(pack.sender())) {
            System.out.println("serviço permitdo para " + pack.sender());
            if (this.isAllowed(pack.receiver())) {
                System.out.println("serviço permitido para " + pack.receiver());
                output = this.toForward(pack.content());
            }
        }
        return output;
    }

    private boolean isAllowed(Integer port) {
        for (final var filter : filters) {
            if (filter.equals(port)) return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private Response toForward(Message message) {
        Response response = null;
        try {
            response = this.gatewayStub.execute(message);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}

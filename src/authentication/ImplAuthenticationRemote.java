package authentication;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ImplAuthenticationRemote implements AuthenticationRemote {
    public final static int PORT = 20004;
    private final List<User> USERS = new ArrayList<>();

    public ImplAuthenticationRemote() {
        USERS.add(new User("admin@autohaus.com", "autohaus2022", UserTypes.EMPLOYEE));
        USERS.add(new User("pedro@mail.com", "flamengo123", UserTypes.CUSTOMER));
        USERS.add(new User("maria@mail.com", "gatodebotas123", UserTypes.CUSTOMER));
    }

    @Override
    public Credentials login(User user) throws RemoteException {
        if (USERS.contains(user)) return new Credentials(Boolean.TRUE, user.type());
        return new Credentials(Boolean.FALSE, user.type());
    }

    public record Credentials(Boolean isRegistered, UserTypes useType) {};
}

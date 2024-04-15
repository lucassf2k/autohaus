package authentication;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImplAuthenticationRemote implements AuthenticationRemote {
    public final static int PORT = 20004;
    private final List<User> USERS = new ArrayList<>();

    public ImplAuthenticationRemote() {
        USERS.add(new User("admin@autohaus.com", "autohaus2022", UserTypes.EMPLOYEE));
        USERS.add(new User("pedro@mail.com", "flamengo123", UserTypes.CUSTOMER));
        USERS.add(new User("vasco@mail.com", "dagama123", UserTypes.CUSTOMER));
    }

    @Override
    public Credentials login(String email, String password) throws RemoteException {
        System.out.println("processando login...");
        final var user = USERS.stream()
                .filter(u -> u.email().equals(email) && u.password().equals(password))
                .findFirst();
        if (user.isEmpty()) return new Credentials(Boolean.FALSE, UserTypes.CUSTOMER);
        else return new Credentials(Boolean.TRUE, user.get().type());
    }

    public record Credentials(boolean isRegistered, UserTypes useType) implements Serializable {};
}

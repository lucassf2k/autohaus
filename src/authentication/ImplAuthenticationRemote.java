package authentication;

import crypto.PBKDF2Password;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImplAuthenticationRemote implements AuthenticationRemote {
    public final static int PORT = 20004;
    private final List<User> USERS = new ArrayList<>();
    private static int loginAttempts = 0;

    public ImplAuthenticationRemote() {
        final var userAdminPassword = PBKDF2Password.create("autohaus2022", null);
        final var userAdmin = new User(
                "admin@autohaus.com",
                userAdminPassword[1],
                UserTypes.EMPLOYEE,
                userAdminPassword[0]);
        USERS.add(userAdmin);
        final var user1Password = PBKDF2Password.create("flamengo123", null);
        final var user1 = new User(
                "pedro@mail.com",
                user1Password[1],
                UserTypes.CUSTOMER,
                user1Password[0]);
        USERS.add(user1);
        final var user2Password = PBKDF2Password.create("dagama123", null);
        final var user2 = new User(
                "vasco@mail.com",
                user2Password[1],
                UserTypes.CUSTOMER,
                user2Password[0]);
        USERS.add(user2);
    }

    @Override
    public Credentials login(String email, String password) throws RemoteException {
        System.out.println("processando login...");
        System.out.println(email);
        System.out.println(password);
        final var user = USERS.stream()
                .filter(u -> u.email().equals(email) && u.password().equals(password))
                .findFirst();
        if (user.isEmpty()) {
            loginAttempts++;
            if (loginAttempts == 3) {
                loginAttempts = 0;
                try {
                    System.out.println("3 tentativas erradas de login...");
                    System.out.println("Espere 1 min e tente novamente");
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return new Credentials(Boolean.FALSE, UserTypes.CUSTOMER);
        } else return new Credentials(Boolean.TRUE, user.get().type());
    }

    @Override
    public User get(String email) throws RemoteException {
        System.out.println("buscando usuário...");
        final var user = USERS
                .stream()
                .filter(u -> u.email().equals(email))
                .findFirst();
        return user.orElse(null);
    }

    private record AttemptControl() {}
    public record Credentials(boolean isRegistered, UserTypes useType) implements Serializable {};
}

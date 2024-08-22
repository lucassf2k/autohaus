package authentication;

import crypto.PBKDF2Password;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;
import java.util.function.Predicate;

public class ImplAuthenticationRemote implements AuthenticationRemote {
    public final static int PORT = 20004;
    private final List<User> USERS = new ArrayList<>();
    private static int loginAttempts = 0;
    private List<AttemptControl> attemptControl = new ArrayList<>();

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
        System.out.println(USERS.toString());
    }

    @Override
    public Credentials login(String email, String password) throws RemoteException {
        System.out.println("processando login...");
        System.out.println(email);
        System.out.println(password);
        final var user = this.get(email);
        if (user != null) {
            final var comparePassword = PBKDF2Password.create(password, user.salt());
            IsValidPassword isValidPassword = () -> comparePassword[1].equals(user.password());
            if (isValidPassword.verify()) {
//                attempts(email);
                return new Credentials(Boolean.TRUE, user.type());
            } else {
//                attempts(email);
                return new Credentials(Boolean.FALSE, user.type());
            }
        }
//        attempts(email);
        return new Credentials(Boolean.FALSE, UserTypes.CUSTOMER);
    }

    @Override
    public User get(String email) throws RemoteException {
        System.out.println("buscando usuário...");
        final Predicate<User> filterByEmail = (u) -> u.email().equals(email);
        final var user = USERS
                .stream()
                .filter(filterByEmail)
                .findFirst();
        return user.orElse(null);
    }

    private void attempts(String email) {
        for (var att : attemptControl) {
            if (att.email.equals(email)) {
                att.attemps++;
                if (att.attemps == 3) {
                    att.attemps = 0;
                    try {
                        System.out.println("3 tentativas erradas de login...");
                        System.out.println("Espere 1 min e tente novamente");
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else attemptControl.add(new AttemptControl(email));
        }
    }

    private static class AttemptControl {
        public String email;
        public int attemps;

        public AttemptControl(String email) {
            this.email = email;
            attemps = 0;
        }
    }
    public record Credentials(boolean isRegistered, UserTypes useType) implements Serializable {};
}

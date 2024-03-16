package authentication;

public record User(String email, String password, UserTypes type) {
}

package authentication;

import java.io.Serializable;

public record User(String email, String password, UserTypes type, String salt) implements Serializable {}

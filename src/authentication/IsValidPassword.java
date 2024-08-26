package authentication;

@FunctionalInterface
public interface IsValidPassword {
    Boolean verify();
}

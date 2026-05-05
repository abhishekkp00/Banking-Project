package domain;

public class Customer {
    private String id;
    private String name;
    private String email;

    public Customer(String email, String name, String id) {
        this.email = email;
        this.name = name;
        this.id = id;
    }
}

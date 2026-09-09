package com.example.demo.User;
import jakarta.persistence.*;

@Entity
@Table(name = "gateway_users")
public class user {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String phoneNumber;

    private String password;
    private double accountBalance=25000.00;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPhoneNumber() {return phoneNumber;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public double getAccountBalance() {return accountBalance;}
    public void setAccountBalance(double accountBalance) {this.accountBalance = accountBalance;}

}

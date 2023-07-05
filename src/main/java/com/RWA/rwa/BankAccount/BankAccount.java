package com.RWA.rwa.BankAccount;

import com.RWA.rwa.Transcactions.Transaction;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "BankAccounts")
public class BankAccount {
    @Id
    @GeneratedValue
    private int id;
    private String accountNumber;
    private String accountName;
    private double balance;



    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

    public BankAccount(String accountNumber, String accountName, double balance) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.balance = balance;
    }
    public BankAccount(){}

    public BankAccount(int id, String accountNumber, String accountName, double balance, List<Transaction> transactions) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.balance = balance;
        this.transactions=transactions;
    }

    public BankAccount(int id, String accountName, String accountNumber, Double balance) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}

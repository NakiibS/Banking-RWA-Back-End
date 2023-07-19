package com.RWA.rwa.Transcactions;

import com.RWA.rwa.BankAccount.BankAccount;
import com.RWA.rwa.User.User;
import jakarta.persistence.*;

@Entity
@Table(name = "Transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String remitente;
    private String Destinatario;
    private double importe;

    @ManyToOne
    @JoinColumn(name = "bank_accounts.id")
    private BankAccount bankAccount;

    public Transaction(int id, String remitente, String destinatario, double importe) {
        this.id = id;
        this.remitente = remitente;
        Destinatario = destinatario;
        this.importe = importe;
    }

    public Transaction(String remitente, String destinatario, double importe) {
        this.remitente = remitente;
        Destinatario = destinatario;
        this.importe = importe;
    }

    public Transaction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getDestinatario() {
        return Destinatario;
    }

    public void setDestinatario(String destinatario) {
        Destinatario = destinatario;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }
}

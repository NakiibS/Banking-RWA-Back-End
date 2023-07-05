package com.RWA.rwa.Transcactions;

import com.RWA.rwa.Conexion.Conexion;
import com.RWA.rwa.User.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TransactionController {

    @GetMapping("/transactions")
    public List<Transaction> listAll (){
        //creamos la lista
        List<Transaction> listTransactions =new ArrayList<>();
        try (Connection connection = Conexion.getConnection()){
            String query = "Select * from Transaction";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String remitente = rs.getString("remitente");
                String destinatario = rs.getString("destinatario");
                Double importe = rs.getDouble("importe");
                Transaction transaction = new Transaction(id,remitente,destinatario,importe);
                listTransactions.add(transaction);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        //System.out.println("Lista"+listTransactions);
        return listTransactions;

    }
    @PostMapping("/accounts/{id_account}/transactions")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction, @PathVariable int id_account){
        try (Connection connection = Conexion.getConnection() ){
            String sql = "INSERT INTO Transaction (remitente, destinatario, importe) VALUES (?, ?, ?);";
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1, transaction.getRemitente());
                statement.setString(2, transaction.getDestinatario());
                statement.setDouble(3, transaction.getImporte());
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0){
                    System.out.println("Se ha añadido la transaccion correctamente");

                }
            }

            //Actualizar el saldo de la cuenta bancaria
            String sqlSaldo = "UPDATE BankAccounts SET balance = balance - ? WHERE (id = ?);";
            try (PreparedStatement statementSaldo = connection.prepareStatement(sqlSaldo)){
                statementSaldo.setDouble(1, transaction.getImporte());
                statementSaldo.setInt(2, id_account);
                int rowsUpdated = statementSaldo.executeUpdate();
                if (rowsUpdated >0){
                    System.out.println("Se ha actualizado el saldo de la cuenta correctamente");
                    return ResponseEntity.ok(transaction);
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }
    @GetMapping("/accounts/{id_account}/transactions/id")
    public ResponseEntity<Transaction> getTransaction(@PathVariable int id){
        Transaction transaction = null;
        try (Connection connection = Conexion.getConnection() ){
            String sql = "SELECT * FROM Transaction WHERE id= ?;";
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setInt(1,id);
                try (ResultSet rs = statement.executeQuery(sql)){
                    transaction = new Transaction();
                    transaction.setId(rs.getInt("id"));
                    transaction.setRemitente(rs.getString("remitente"));
                    transaction.setDestinatario(rs.getString("destinatario"));

                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        if (transaction != null)
            return ResponseEntity.ok(transaction);
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }

}

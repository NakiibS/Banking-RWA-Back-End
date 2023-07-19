package com.RWA.rwa.BankAccount;

import com.RWA.rwa.Conexion.Conexion;
import com.RWA.rwa.User.User;
import jakarta.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class BankAccountController {


    @GetMapping("/accounts")
    public List<BankAccount> listAll (){
        //creamos la lista
        List<BankAccount> listAccounts =new ArrayList<>();
        try (Connection connection = Conexion.getConnection()){
            String query = "Select * from bank_accounts";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String accountNumber = rs.getString("account_Number");
                String accountName = rs.getString("account_Name");
                Double balance = rs.getDouble("balance");

                BankAccount account = new BankAccount(id,accountName,accountNumber,balance);
                listAccounts.add(account);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("Lista"+listAccounts);
        return listAccounts;

    }
    @GetMapping("/accounts/{id}")
    public ResponseEntity<BankAccount> getAccount(@PathVariable("id") int id){
        BankAccount account = null;
        try (Connection connection = Conexion.getConnection()){
            String sql = "SELECT * FROM bank_accounts WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setInt(1,id);
                try (ResultSet rs = statement.executeQuery()){
                    if(rs.next()){
                        account = new BankAccount();
                        account.setId(rs.getInt("id"));
                        account.setAccountName(rs.getString("account_Name"));
                        account.setAccountNumber(rs.getString("account_Number"));
                        account.setBalance(rs.getDouble("balance"));

                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        if (account != null){
            return ResponseEntity.ok(account);
        }else
            return ResponseEntity.notFound().build();


    }
    @PostMapping("/accounts")
    public ResponseEntity<BankAccount> createAccount(@RequestBody BankAccount account){
        try (Connection connection = Conexion.getConnection() ){
            String sql = "INSERT INTO bank_accounts (account_Number, account_Name, balance) VALUES (?, ?, ?);";
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1, account.getAccountNumber());
                statement.setString(2, account.getAccountName());
                statement.setDouble(3, account.getBalance());
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0){
                    System.out.println("Se ha añadido la cuenta correctamente");
                    return ResponseEntity.ok(account);
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable("id") int id){
        try (Connection connection = Conexion.getConnection()){
            String sql = "DELETE FROM bank_accounts WHERE (id = ?);";
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setInt(1,id);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0){
                    return ResponseEntity.ok("Account deleted successfully");
                }else {
                    return ResponseEntity.notFound().build();
                }

            }
        }catch (SQLException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete account");
        }
    }


}

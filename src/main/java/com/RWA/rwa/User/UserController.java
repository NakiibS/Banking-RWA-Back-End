package com.RWA.rwa.User;

import com.RWA.rwa.Conexion.Conexion;
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
public class UserController {


    @GetMapping("/users")
    public List<User> listAll (){
        //creamos la lista
        List<User> listUsers =new ArrayList<>();
        try (Connection connection = Conexion.getConnection()){
            String query = "Select * from users";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String first_name = rs.getString("First_Name");
                String last_name = rs.getString("Last_Name");
                String user_name = rs.getString("User_Name");
                String mail = rs.getString("Mail");
                String cellNumber = rs.getString("CellNumber");
                String passwd = rs.getString("Passwd");

                User user = new User(id,first_name,last_name,user_name,mail,cellNumber,passwd);
                listUsers.add(user);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        //System.out.println("Lista"+listUsers);
        return listUsers;

    }
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") int id){
        User user = null;
        try (Connection connection = Conexion.getConnection()){
            String sql = "SELECT * FROM users WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setInt(1,id);
                try (ResultSet rs = statement.executeQuery()){
                    if(rs.next()){
                        user = new User();
                        user.setId(rs.getInt("id"));
                        user.setFirst_Name(rs.getString("First_Name"));
                        user.setLast_Name(rs.getString("Last_Name"));
                        user.setUser_Name(rs.getString("User_Name"));
                        user.setMail(rs.getString("Mail"));
                        user.setCellNumber(rs.getString("CellNumber"));
                        user.setPasswd(rs.getString("Passwd"));
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        if (user != null){
            return ResponseEntity.ok(user);
        }else
            return ResponseEntity.notFound().build();


    }
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user){
        try (Connection connection = Conexion.getConnection() ){
            String sql = "INSERT INTO users (First_Name, Last_Name, User_Name, Mail, CellNumber, Passwd) VALUES (?, ?, ?, null, null, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1, user.getFirst_Name());
                statement.setString(2, user.getLast_Name());
                statement.setString(3, user.getUser_Name());
                statement.setString(4, user.getPasswd());
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0){
//                    System.out.println("Se ha añadido el usuario correctamente");
                    return ResponseEntity.ok(user);
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    @PutMapping("/users-settings")
    public ResponseEntity<User> editUser(@RequestBody User user){
        try (Connection connection = Conexion.getConnection() ){
            String sql = "UPDATE  users Set First_Name = ?, Last_Name =?, Mail=?, CellNumber =? where id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1, user.getFirst_Name());
                statement.setString(2, user.getLast_Name());
                statement.setString(3, user.getMail());
                statement.setString(4, user.getCellNumber());
                statement.setInt(5, user.getId());
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0){
                    System.out.println("Se ha editado el usuario correctamente");
                    return ResponseEntity.ok(user);
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    @GetMapping("/connection")
    public String checkConnection() throws SQLException {
        if(Conexion.getConnection().isValid(5000))
            return "Database connection is successful.";
        else
            return "Database connection has failed.";

    }

}

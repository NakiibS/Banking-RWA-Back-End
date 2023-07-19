package com.RWA.rwa.User;

import com.RWA.rwa.Conexion.Conexion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
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
                String cellNumber = rs.getString("Cell_Number");
                String passwd = rs.getString("passwd");

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
                        user.setCellNumber(rs.getString("Cell_Number"));
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
            // Check if the user already exists
            String query = "SELECT * FROM users WHERE User_Name = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(query)){
                checkStatement.setString(1, user.getUser_Name());
                try (ResultSet rs = checkStatement.executeQuery()){
                    if (rs.next()) {
                        // User already exists, return an error response
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                        //status(HttpStatus.BAD_REQUEST).body("User already exists");
                    }
                }
            }

            // If the user does not exist, proceed with the insertion
            String sql = "INSERT INTO users (First_Name, Last_Name, User_Name, Mail, Cell_Number, Passwd) VALUES (?, ?, ?, null, null, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1, user.getFirst_Name());
                statement.setString(2, user.getLast_Name());
                statement.setString(3, user.getUser_Name());
                statement.setString(4, user.getPasswd());
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0){
                    return ResponseEntity.ok(user);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    @PutMapping("/user/settings")
    public ResponseEntity<User> editUser(@RequestBody User user) {
        try (Connection connection = Conexion.getConnection()) {
            // Primero, consulta el usuario actual desde la base de datos
            String selectSql = "SELECT * FROM users WHERE id = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                selectStatement.setInt(1, user.getId());
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Obtiene los valores actuales de la base de datos
                        String currentFirstName = resultSet.getString("First_Name");
                        String currentLastName = resultSet.getString("Last_Name");
                        String currentMail = resultSet.getString("Mail");
                        String currentCellNumber = resultSet.getString("Cell_Number");

                        // Verifica qué campos se deben actualizar y establece los valores en el objeto user
                        if (user.getFirst_Name() != null) {
                            currentFirstName = user.getFirst_Name();
                        }
                        if (user.getLast_Name() != null) {
                            currentLastName = user.getLast_Name();
                        }
                        if (user.getMail() != null) {
                            currentMail = user.getMail();
                        }
                        if (user.getCellNumber() != null) {
                            currentCellNumber = user.getCellNumber();
                        }

                        // Construye la consulta de actualización dinámica
                        String updateSql = "UPDATE users SET First_Name = ?, Last_Name = ?, Mail = ?, Cell_Number = ? WHERE id = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                            updateStatement.setString(1, currentFirstName);
                            updateStatement.setString(2, currentLastName);
                            updateStatement.setString(3, currentMail);
                            updateStatement.setString(4, currentCellNumber);
                            updateStatement.setInt(5, user.getId());

                            int rowsAffected = updateStatement.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Se ha editado el usuario correctamente");
                                return ResponseEntity.ok(user);
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    //variable para simular el inicio de sesion
    private boolean loggedIn = false;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User usercred){
        //Validamos el usuario
        try (Connection connection = Conexion.getConnection()){
            String sql = "SELECT * FROM users WHERE User_Name = ? AND Passwd = ?";
            try (PreparedStatement statement = Conexion.getConnection().prepareStatement(sql)){
                statement.setString(1, usercred.getUser_Name());
                statement.setString(2, usercred.getPasswd());
                try (ResultSet rs = statement.executeQuery()){
                    if (rs.next()) {
                        loggedIn = true;
                        return ResponseEntity.ok("Login successful");
                    }
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loggedIn = false;
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");

    }
    @GetMapping("/logout")
    public ResponseEntity<String> logout(){
        //restablecer el loggedIn para simular el cierrer de sesion
        loggedIn = false;
        return ResponseEntity.ok("Logout successful");
    }
    @GetMapping("/connection")
    public String checkConnection() throws SQLException {
        if(Conexion.getConnection().isValid(5000))
            return "Database connection is successful.";
        else
            return "Database connection has failed.";

    }

}

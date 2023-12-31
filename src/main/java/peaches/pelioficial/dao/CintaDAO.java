/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package peaches.pelioficial.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import peaches.pelioficial.model.Cinta;

/**
 *
 * @author q-ql
 */
public class CintaDAO implements Dao<Cinta>{
    private Connection connection;
    
    public CintaDAO(Connection connection){
        this.connection = connection;
    }
    
    @Override
    public Optional<Cinta> get(long id){
        Cinta cinta = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Cintas WHERE cinta_id = ?")){
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                cinta = new Cinta();
                cinta.setCintaId(resultSet.getInt("cinta_id"));
                cinta.setPeliculaId(resultSet.getInt("pelicula_id"));
                cinta.setEstado(resultSet.getString("estado"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(cinta);
    }
    
    @Override
    public List<Cinta> getAll(){
        List<Cinta> cintas = new ArrayList<>();
        try (Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Cintas");
            while(resultSet.next()){
                Cinta cinta = new Cinta();
                cinta.setCintaId(resultSet.getInt("cinta_id"));
                cinta.setPeliculaId(resultSet.getInt("pelicula_id"));
                cinta.setEstado(resultSet.getString("estado"));
                cintas.add(cinta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cintas;
    }
    
    @Override
    public int save(Cinta cinta) {
        String sql = "INSERT INTO Cintas (pelicula_id, estado) VALUES (?, ?)";
        int generatedId = 0;
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, cinta.getPeliculaId());
            statement.setString(2, cinta.getEstado());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }
    
    @Override
    public void update(Cinta cinta, String[] params){
        String sql = "UPDATE Cintas SET pelicula_id = ?, estado = ? WHERE cinta_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, cinta.getPeliculaId());
            statement.setString(2, cinta.getEstado());
            statement.setInt(3, cinta.getCintaId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void delete(Cinta cinta){
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM Cintas WHERE cinta_id = ?")){
            statement.setInt(1, cinta.getCintaId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void agregarCinta(Cinta cinta) {
        String sql = "INSERT INTO cintas (pelicula_id, estado) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, cinta.getPeliculaId());
            statement.setString(2, cinta.getEstado());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating cinta failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cinta.setCintaId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating cinta failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int obtenerPeliculaIdPorNombre(String nombre) {
        String sql = "SELECT pelicula_id FROM peliculas WHERE titulo = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nombre);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("pelicula_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public List<Cinta> obtenerTodasLasCintas() {
        List<Cinta> cintas = new ArrayList<>();
        String sql = "SELECT cintas.*, peliculas.titulo FROM cintas " +
                     "JOIN peliculas ON cintas.pelicula_id = peliculas.pelicula_id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Cinta cinta = new Cinta();
                cinta.setCintaId(resultSet.getInt("cinta_id"));
                cinta.setPeliculaId(resultSet.getInt("pelicula_id"));
                cinta.setEstado(resultSet.getString("estado"));
                cinta.setTituloPelicula(resultSet.getString("titulo"));
                cintas.add(cinta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cintas;
    }

    
    public void actualizarCinta(Cinta cinta) {
        String sql = "UPDATE cintas SET pelicula_id = ?, estado = ? WHERE cinta_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cinta.getPeliculaId());
            statement.setString(2, cinta.getEstado());
            statement.setInt(3, cinta.getCintaId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Cinta obtenerCintaPorId(int cintaId) {
        Cinta cinta = null;
        String sql = "SELECT * FROM cintas WHERE cinta_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cintaId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                cinta = new Cinta();
                cinta.setCintaId(resultSet.getInt("cinta_id"));
                cinta.setPeliculaId(resultSet.getInt("pelicula_id"));
                cinta.setEstado(resultSet.getString("estado"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cinta;
    }
    
    public boolean eliminarCinta(int cintaId) {
        String sql = "DELETE FROM cintas WHERE cinta_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cintaId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Cinta> buscarCintasPorNombrePelicula(String nombrePelicula) {
        List<Cinta> cintas = new ArrayList<>();
        String sql = "SELECT cintas.*, peliculas.titulo FROM cintas " +
                     "JOIN peliculas ON cintas.pelicula_id = peliculas.pelicula_id " +
                     "WHERE peliculas.titulo LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + nombrePelicula + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Cinta cinta = new Cinta();
                cinta.setCintaId(resultSet.getInt("cinta_id"));
                cinta.setPeliculaId(resultSet.getInt("pelicula_id"));
                cinta.setTituloPelicula(resultSet.getString("titulo"));
                cinta.setEstado(resultSet.getString("estado"));
                cintas.add(cinta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cintas;
    }
    
    public Optional<Cinta> buscarPorTitulo(String titulo) {
        Optional<Cinta> cinta = Optional.empty();
        String sql = "SELECT * FROM cintas WHERE titulo = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, titulo);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                cinta = Optional.of(new Cinta(
                    resultSet.getInt("cinta_id"),
                    resultSet.getInt("pelicula_id"),
                    resultSet.getString("estado")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cinta;
    }

}

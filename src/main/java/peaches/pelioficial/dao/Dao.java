/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package peaches.pelioficial.dao;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author q-ql
 */
public interface Dao<T>{
    Optional<T> get(long id);
    List<T> getAll();
    int save(T t);
    void update(T t, String[] params);
    void delete(T t);
}

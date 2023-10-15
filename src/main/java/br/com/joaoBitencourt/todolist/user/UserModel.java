package br.com.joaoBitencourt.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

//vai adicionar automaticamente os get e set dinamicamente comforme for usado 
@Data
@Entity(name="td_users")
public class UserModel {
    
    //id composto de 3 grupos de numeros, mais seguro
    @Id
    //auto gerar esse UUID
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(unique = true)
    private String username;
    private String name;
    private String password;
    
    @CreationTimestamp
    //vai criar automaticamente com o "_" separando created do at
    private LocalDateTime createdAt;

}

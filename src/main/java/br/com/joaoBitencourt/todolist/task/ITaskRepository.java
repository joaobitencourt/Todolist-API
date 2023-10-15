package br.com.joaoBitencourt.todolist.task;

import java.util.List;
import java.util.UUID;


import org.springframework.data.jpa.repository.JpaRepository;

//JpaRepository<Entidade, id de referencia dessa entidade>
public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {
   List<TaskModel> findByIdUser(UUID idUser);
}

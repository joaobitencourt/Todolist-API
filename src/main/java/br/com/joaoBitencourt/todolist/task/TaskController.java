package br.com.joaoBitencourt.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.joaoBitencourt.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity crate(@RequestBody TaskModel taskModel, HttpServletRequest request){
        var idUser =  request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);
        System.out.println("Chegou TasksController" + idUser);

        //validação de data e hora
        var currentDate = LocalDateTime.now();
        if(currentDate.isAfter(taskModel.getStartAt()) ||
         currentDate.isAfter(taskModel.getEndAt()) ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("A data de início e de termino deve ser maior do que a data atual");
        }

        if(taskModel.getStartAt().isAfter(taskModel.getEndAt()) ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("A data de início deve ser menor que a data de termino");
        }

        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }


    //listando tasks de um usuario expecifico
    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request){
        var idUser =  request.getAttribute("idUser");
        var tasks =  this.taskRepository.findByIdUser((UUID)idUser);

        return tasks;
    }

    //update parcial de dados com percistencia dos demais não alterados
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, @PathVariable UUID id,  HttpServletRequest request){
        //buscando no db para fazer a mesclagem de dados cads no banco e que vamos alterar nos campos
        var task = this.taskRepository.findById(id).orElse(null);

        //Verificando se a tarefa existe 
        if(task == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Tarefa não encontrada ou inexistente");
        }

        var idUser =  request.getAttribute("idUser");

        //validando se é o usuario autor da task que esta querendo altera-la
        if(!task.getIdUser().equals(idUser)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Usuário não tem permissão para alterar essa tarefa");
        }

        //fazendo a mesclagem dos dados null e não null
        Utils.copyNonNullProperties(taskModel, task);
        var taskUpdated = this.taskRepository.save(task);
        return ResponseEntity.ok().body(this.taskRepository.save(taskUpdated));
    }


}

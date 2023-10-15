package br.com.joaoBitencourt.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
//Rata dos usuarios
@RequestMapping("/users")
public class UserController {
    
    //O spring vai gerenciar o ciclo de vida dessa classe, instanciar ect..
    @Autowired
    private IUserRepository userRepository;

    //trazendo os dados dentro do body da requisição
    @PostMapping("/")
    //usamos o responsyEntity para retornar varios tipos de codigos HTTP em uma mesma requisição
    public ResponseEntity create(@RequestBody UserModel userModel){
        var user = this.userRepository.findByUsername(userModel.getUsername());
        if(user != null){
            System.out.println("Usuario já existente");
            //mensagem
            //Status code
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existente");
        }

        var passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashed);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userCreated);
    }

}

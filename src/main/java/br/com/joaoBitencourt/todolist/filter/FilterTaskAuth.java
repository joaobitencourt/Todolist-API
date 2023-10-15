package br.com.joaoBitencourt.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.joaoBitencourt.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//spring gerencie(classe bem generica) falando que é para passar por essa classe
@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository iUserRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                //deixanod a autenticação expecificamente em uma rota 
                var serveletPath =  request.getServletPath();
                if(serveletPath.startsWith("/tasks/")){

                //Pegando a autentificação (usuario e senha)
                var authorization = request.getHeader("Authorization");
                System.out.println(authorization);
                
                /*
                substring irá procurar pelo parametro em ""
                + o length irá calcular a quantidade de caracteres 
                + O trim remove os espassos que tem
                */
                //Pegando a string de autentificação em base64
                var authEncoded = authorization.substring("Basic".length()).trim();
                System.out.println(authEncoded);

                //Decodificando essa string
                byte[] authDecode =  Base64.getDecoder().decode(authEncoded);
                System.out.println(authDecode);

                //convertendo de bytes para string
                var authString = new String(authDecode);
                System.out.println("Authorization");
                System.out.println(authString);

                //Pegando o login (user e senha) e dividindo em duas strings
                String[] credentials = authString.split(":");
                //populando o array de string 
                String username = credentials[0];
                String password = credentials[1];
                System.out.println(username);
                System.out.println(password);

                //validar User
                var user = this.iUserRepository.findByUsername(username);
                if(user == null){
                    response.sendError(401);
                }else{

                    //validar a senha
                    var passWordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                    if(passWordVerify.verified){
                        request.setAttribute("idUser", user.getId());
                        filterChain.doFilter(request, response);
                    }else{
                        response.sendError(401);
                    }

                }
                }else{
                    filterChain.doFilter(request, response);
                }


    }


}

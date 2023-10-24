package br.com.fiap.epictask.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.fiap.epictask.user.User;
import br.com.fiap.epictask.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginFilter extends OncePerRequestFilter{

    private UserRepository repository;

    public LoginFilter(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                
                // pegar o auth
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                // se estiver logado
                if (authentication != null){
                    // pego o usuário
                    var principal = (OAuth2User) authentication.getPrincipal();
                    

                    //se não existe no BD
                    Optional<User> optional = repository.findById(Long.valueOf(principal.getName()));
                    if (optional.isEmpty()){
                        //salvo user
                        repository.save(User.convert(principal));

                    }


                }

            filterChain.doFilter(request, response);
    }
    
}

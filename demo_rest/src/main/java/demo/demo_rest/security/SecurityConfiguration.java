package demo.demo_rest.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
//In questa classe diciamo a Spring Boot come usare il token e tutto il processo di sicurezza
public class SecurityConfiguration {

    //All'avvio dell'applicazione Spring Security proverà a cercare un Bean (quindi un involucro) di tipo SecurityFilterChain (catena di filtri di sicurezza) questo Bean è il responsabile della configurazione di tutta la sicurezza HTTP dell'applicazione
    private final JwtAuthenticationFilter jwtAuthFilter;
    //final perchè così verra iniettato direttamente da Spring Boot
    private final AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        //disabilitiamo il csrf token
    http
        .csrf()
        .disable()
        //possiamo scegliere quali URL o path vogliamo proteggere, ma in ogni applicazione c'è sempre una white list, ciò significa che abbiamo alcuni endpoint (URL o path), che non richiedono alcuna autenticazione o token per esempio una register o una login.
        //con authorizeHttpRequests() implementiamo la white list
        .authorizeHttpRequests()
        //nella stringa va il path
        .requestMatchers("/auth/**").permitAll()
        // .requestMatchers("/products/*" ).hasAnyAuthority("ADMIN", "USER")

        //tutte le richieste nel requestMatchers devono essere autorizzate
        //tutte le altre richieste devono essere autenticate
        .anyRequest().authenticated()
        //ora configuriamo la decisione della gestione della sessione e delle decisioni, quindi quando implementiamo il filtro, vogliamo un filtro per richiesta cioè richiesta deve essere autenticata, ciò significa che non dovremmo memorizzare lo stato di autenticazione o della sessione, quindi la sessione dovrebbe essere senza stato e questo ci aiuterà a garantire che ogni richiesta venga autenticata correttamente
        //Usiamo l'and per aggiungere una nuova configurazione  
        .and()
        //gestione della sessione
        .sessionManagement()
        //policy di creazione della sessione, quindi come vogliamo creare la sessione sensa stato (stateless)
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        //ora dobbiamo dire a Spring Boot quale fornitore di autenticazione voglio usare
        .authenticationProvider(authenticationProvider)
        //ora usiamo il filtro JWT che abbiamo già creato. Before perchè deve essere eseguito prima del filtro nome utente, password e autenticazione
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        

        return http.build();
    } 
    
}

package demo.demo_rest.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    //Generiamo la signature key
    private static final String SECRET_KEY = "357538782F4125442A472D4B6150645367566B59703373367639792442264528"; 

    //Per manipolare i token è necessario aggiungere delle dipendenze, che sono il gruppo delle jjwt
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    //Estrae un singolo claim
    //Una funzione di tipo Claims, T è il tipo che voglio restituire
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
    } 

    //Controlla se il token è valido e se appartiene allo user passato
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        //stiamo verificando se lo username che c'è dentro il token è uguale a quello nell'input e che il token non sia scaduto
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    //verifichiamo se il token nella data odierna è scaduto o no
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        //estraiamo la claim dal token 
        return extractClaim(token, Claims::getExpiration);
    }

    //Estrae tutti i claims dal token passato
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
        //settiamo la chiave di firma che ci serve quando andiamo a creare, generare o decodificare un token
        .setSigningKey(getSignInKey())
        //creiamo l'oggetto getSignInKey
        .build()
        //questo metodo analizza le claims jws e il token
        .parseClaimsJws(token)
        //e prendiamo il body del token, quindi tutte le claims che ha all'interno
        .getBody();
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        //questo è uno degli algoritmi 
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
}




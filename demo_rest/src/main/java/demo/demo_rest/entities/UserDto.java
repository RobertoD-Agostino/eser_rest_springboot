package demo.demo_rest.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    
    private int id;
    private String email;
    

}


//creare uno userDTO con email e id quando si crea l'utente voglio ricevere lo userDTO

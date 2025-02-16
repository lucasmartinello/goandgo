package org.goandgo.usuario;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private Long id;

    @Size(max = 100, message = "O nome não pode ter mais de 100 caracteres!")
    private String nome;

    @Size(max = 20, message = "O login não pode ter mais de 20 caracteres!")
    private String login;

    @Size(max = 100, message = "A senha não pode ter mais de 100 caracteres!")
    private String senha;
}
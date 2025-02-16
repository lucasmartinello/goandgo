package org.goandgo.cliente;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    private Long id;

    @Size(max = 100, message = "O nome não pode ter mais de 100 caracteres!")
    private String nome;

    @Size(max = 20, message = "O telefone não pode ter mais de 20 caracteres!")
    private String telefone;

    @Size(max = 100, message = "O e-mail não pode ter mais de 100 caracteres!")
    private String email;

    @Size(max = 11, message = "O CPF não pode ter mais de 11 caracteres!")
    private String cpf;
}
package sp.senai.br.cafearomae.CafeAromaESabor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "movimentacao")
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tipo da movimentação: ENTRADA ou SAIDA
    @NotNull(message = "O tipo de movimentação é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoMovimentacao tipo;

    @NotNull(message = "A quantidade é obrigatória")
    @Column(nullable = false)
    private Integer quantidade;

    private LocalDate data;

    // Relacionamento com o produto (muitas movimentações para um produto)
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    // Enum interno para o tipo de movimentação
    public enum TipoMovimentacao {
        ENTRADA, SAIDA
    }
}

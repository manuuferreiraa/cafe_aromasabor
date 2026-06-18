package sp.senai.br.cafearomae.CafeAromaESabor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório")
    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 300)
    private String descricao;

    @NotNull(message = "A quantidade é obrigatória")
    @Column(nullable = false)
    private Integer quantidade;

    @Column(length = 50)
    private String lote;

    private LocalDate validade;

    // Relacionamento com movimentações (um produto pode ter várias movimentações)
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<Movimentacao> movimentacoes;
}

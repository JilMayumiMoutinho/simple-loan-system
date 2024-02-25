package me.dio.creditapplicationsystem.entity

//No canto esquerdo, após instalar a extensão JPA Buddy, show DDL, seleciona o banco q utiliza e irá gerar o migration

/*import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table*/
import jakarta.persistence.*
import java.math.BigDecimal

@Entity //Anotação do jakarta q mostra q será uma entidade ou tabela e seus atfibutos serão colunas
@Table(name = "Customer")   //Anotação pra infomar o nome da tabela, se não colocar, fica o nome da class
class Customer (
    @Column(nullable = false) var firstName: String = "",
    //Dentro no parenteses, ctrl + p, mostra as opções d settings
    //Obs: Automaticamente ele irá transformar em snake_case dentro da tabela
    @Column(nullable = false) var lastName: String = "",
    @Column(nullable = false, unique = true) val cpf: String = "",
    @Column(nullable = false, unique = true) val email: String = "",
    @Column(nullable = false) var income: BigDecimal = BigDecimal.ZERO,
    @Column(nullable = false) var password: String = "",
    @Column(nullable = false) @Embedded var address: Address = Address(),
    // Inserimos address dentro de customer, assim, não precisa ser uma tabela
    @Column(nullable = false) @OneToMany(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        mappedBy = "customer")
    var credits: List<Credit> = mutableListOf(),
    //@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    //Indica a relação, ou seja um customer pode ter vários créditos
    //Type.LAZY = Indica q ao carregar o customer não precisa carregar todos os créditos apenas quando solicitar isso em específico
    //cascade. Anot. q o q fazer s eo pai for deletado, ex se deletar customer, os creditos tbm o serão
    //mappedBy. Anot. de qual é a foreing key correspondente
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null
    //@Id anot. p mostrar q é a primary key
    //@GeneratedValue Anot. p gerar um valor. após o iuqal, ao colocar q é identity, não gera 2 iguais
    ) {
}

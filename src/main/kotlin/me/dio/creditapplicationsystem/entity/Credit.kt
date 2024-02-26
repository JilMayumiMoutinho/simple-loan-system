package me.dio.creditapplicationsystem.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import me.dio.creditapplicationsystem.enummeration.Status
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "Credit")
data class Credit (
    @Column(nullable = false, unique = true) var creditCode: UUID = UUID.randomUUID(),
    @Column(nullable = false) val creditValue: BigDecimal = BigDecimal.ZERO,
    @Column(nullable = false) val dayFirstInstallment: LocalDate,
    @Column(nullable = false) val numberOfInstallments: Int = 0,
    @Enumerated var status: Status = Status.IN_PROGRESS,
    //@Enumerated Só informando q é um enum
    //Se declarar @Enumerated(value = EnumType.STRING), migrations gera como string e não INT
    @ManyToOne var customer: Customer? = null,
    // Informando q pode haver varios credits para um customer
    // TODOJIL Não entendi pq é var? Deveria já ser o número não
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null
)

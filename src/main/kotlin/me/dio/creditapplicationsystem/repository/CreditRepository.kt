package me.dio.creditapplicationsystem.repository

import me.dio.creditapplicationsystem.entity.Credit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
// Bean q vai ser usado no repo. Não é obrigatório colocar  essa anotação de q é repository, mas é boa prática
interface CreditRepository: JpaRepository<Credit, Long> {
    //JpaRepository já tem método prontos q poderemos usar
    fun findByCreditCode(creditCode: UUID) : Credit?
    //spring tem biblioteca q só de escrever find by x entende o q é pra buscar

    @Query(value = "SELECT * FROM CREDIT WHERE CUSTOMER_ID = ?1", nativeQuery = true)
    // @Query Anot. de q é n sql nativo
    //?1 é o primeiro parametro, se huvesse mais, era só referenciar cada um, ex: ?3
    fun findAllByCustomerId(customerId: Long): List<Credit>
}
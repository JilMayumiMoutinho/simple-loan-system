package me.dio.creditapplicationsystem.controller

import jakarta.validation.Valid
import me.dio.creditapplicationsystem.dto.request.CreditDto
import me.dio.creditapplicationsystem.dto.response.CreditView
import me.dio.creditapplicationsystem.dto.response.CreditViewList
import me.dio.creditapplicationsystem.entity.Credit
import me.dio.creditapplicationsystem.service.impl.CreditService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.stream.Collectors

@RestController
//Anot. mostra q é um controller
@RequestMapping("/api/credits")
// ANot. mostra o path
class CreditResource(
    private val creditService: CreditService
    // De front p api, insere camada de service
) {

    @PostMapping
    //Para salvar
    fun saveCredit(@RequestBody @Valid creditDto: CreditDto): ResponseEntity<CreditView> {
        //Anot. ResponseEntity informa q sera do tipo res
        //Anot. Valid informa q tem anotações para validar
        val savedCredit: Credit = this.creditService.save(creditDto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED)
            //nativo do spring par qd a criação da sucesso
            //.body("Credit ${credit.creditCode} - Customer ${credit.customer?.firstName} saved!")
            //Se fsse voltar a frase acima, o retorno após o nome da função seria ResponseEntity<String>
            //Pra poder verificar os valores de retorno nos testes, troquei por:
            .body(CreditView(savedCredit))
    }

    @GetMapping
    fun findAllByCustomerId(@RequestParam(value = "customerId") customerId: Long):
            ResponseEntity<List<CreditViewList>> {
        val creditViewList: List<CreditViewList> = this.creditService.findAllByCustomer(customerId)
            .stream()
            .map { credit: Credit -> CreditViewList(credit) }
            .collect(Collectors.toList())
        //Recebe uma lista, faz um map pq quer de outra forma. Então manipula, volta a ser lista no collect e retorna
        return ResponseEntity.status(HttpStatus.OK)
        //nativo do spring par qd a recuração da sucesso
            .body(creditViewList)
    }

    @GetMapping("/{creditCode}")
    //Neste caso informamos aqui q o creditCode vai no path
    fun findByCreditCode(
        @RequestParam(value = "customerId") customerId: Long,
        @PathVariable creditCode: UUID
    ): ResponseEntity<CreditView> {
        val credit: Credit = this.creditService.findByCreditCode(customerId, creditCode)
        return ResponseEntity.status(HttpStatus.OK).body(CreditView(credit))
    }
}

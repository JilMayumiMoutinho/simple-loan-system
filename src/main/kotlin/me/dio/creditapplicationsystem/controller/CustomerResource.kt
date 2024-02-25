package me.dio.creditapplicationsystem.controller

import jakarta.validation.Valid

import me.dio.creditapplicationsystem.dto.request.CustomerDto
import me.dio.creditapplicationsystem.dto.request.CustomerUpdateDto
import me.dio.creditapplicationsystem.dto.response.CustomerView
import me.dio.creditapplicationsystem.entity.Customer
import me.dio.creditapplicationsystem.service.impl.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
//Anot. q mostra q é classe de controller
@RequestMapping("/api/customers")
//Anot. qual url q vai atender ou seja, aqui local: localhost/8080/ai/customer
class CustomerResource(
    private val customerService: CustomerService
) {
    @PostMapping
    //Anot. post ou seja, salva dados
    fun saveCustomer(@RequestBody @Valid customerDto: CustomerDto): ResponseEntity<CustomerView> {
        //@RequestBody Anot.: Tem um body
        val savedCustomer: Customer = this.customerService.save(customerDto.toEntity())
        //return "Customer {savedCustomer.firstName} saved"
        return ResponseEntity.status(HttpStatus.CREATED)
            //Qd a criação da sucesso
            .body(CustomerView(savedCustomer))
    }

    @GetMapping("/{id}")
    //Anot. get ou seja, recupera dados, passa entre chaves os parametros q vão na url ou seja o path
    fun findById(@PathVariable id: Long): ResponseEntity<CustomerView> {
        //@PathVariable Anot. do q é o parametro
        val customer: Customer = this.customerService.findById(id)
        return ResponseEntity.status(HttpStatus.OK)
            //Qd a recuração da sucesso
            .body(CustomerView(customer))
        //CustomerView(customer) pq faz um construtor para deixar da forma q queremos
    }

    @DeleteMapping("/{id}")
    //Anot. delete ou seja, deleta dados, passa entre chaves os parametros q vão na url  ou seja o path
    @ResponseStatus(HttpStatus.NO_CONTENT)
    //Anot. para dar 204, q não encontrou, mas q não existe no dB como ele queria
    fun deleteCustomer(@PathVariable id: Long) = this.customerService.delete(id)

    @PatchMapping
    //update PUT ou PATH,
    fun updateCustomer(
        @RequestParam(value = "customerId") id: Long,
        //Anot. outra forma de mostrar no lugar de path. Qd por param não precisa ter {"/id"} como acima
        @RequestBody @Valid customerUpdateDto: CustomerUpdateDto
        //RequestBody Anot q será um body. @Valid. Anot q tem q cumprir aquele schema
    ): ResponseEntity<CustomerView> {
        val customer: Customer = this.customerService.findById(id)
        val customerToUpdate: Customer = customerUpdateDto.toEntity(customer)
        val customerUpdated: Customer = this.customerService.save(customerToUpdate)
        return ResponseEntity.status(HttpStatus.OK)
            //Qd a recuperação da sucesso
            .body(CustomerView(customerUpdated))
    }
    //é update pq não muda o id q é a primary key
}
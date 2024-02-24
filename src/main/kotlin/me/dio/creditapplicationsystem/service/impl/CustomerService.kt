package me.dio.creditapplicationsystem.service.impl

import me.dio.creditapplicationsystem.entity.Customer
import me.dio.creditapplicationsystem.repository.CustomerRepository
import me.dio.creditapplicationsystem.service.ICustomerService
import org.springframework.stereotype.Service

@Service
//Obrigatório colocar anotação de q é service, diferente de Repository
class CustomerService(private val customerRepository: CustomerRepository): ICustomerService {
    override fun save(customer: Customer): Customer =
        this.customerRepository.save(customer)

    override fun findById(id: Long): Customer =
        this.customerRepository.findById(id).orElseThrow{throw RuntimeException("Client with id: $id not found")}

    override fun delete(id: Long) =
        this.customerRepository.deleteById(id)
    //Não retorna nada
}

//Spring não tem função de update, ele sobre-escreve com o save
//REPO: conexão com banco de dados
//ENTITY: classes da tabela do banco de dados

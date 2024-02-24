package me.dio.creditapplicationsystem.service

import me.dio.creditapplicationsystem.entity.Customer

interface ICustomerService {
    fun save(customer: Customer): Customer
    fun findById(id: Long): Customer
    fun delete(id: Long)
    // Como vemos q não há : o que retorna, não retorna nada
}

package me.dio.creditapplicationsystem.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.dio.creditapplicationsystem.dto.request.CreditDto
import me.dio.creditapplicationsystem.dto.request.CustomerDto
import me.dio.creditapplicationsystem.entity.Address
import me.dio.creditapplicationsystem.entity.Credit
import me.dio.creditapplicationsystem.entity.Customer
import me.dio.creditapplicationsystem.repository.CreditRepository
import me.dio.creditapplicationsystem.repository.CustomerRepository
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditResourceTest {

    @Autowired
    private lateinit var creditRepository: CreditRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/credits"
    }

    @BeforeEach
    fun setup() {
        creditRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        creditRepository.deleteAll()
    }

    @Test
    fun `should create a credit and return 201 status`() {
        val customer = this.customerRepository.save(buildCustomer())
        val creditDto: CreditDto = builderCreditDto(customerId = 1L)
        val valueAsString: String = objectMapper.writeValueAsString(creditDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON).content(valueAsString)
        )   .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(1000.0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallment").value(5))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("IN_PROGRESS"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value(customer.email))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value(customer.income))
    }

    @Test
    fun `should find a credit by customerId and return 200 status`() {
        val customer: Customer = customerRepository.save(buildCustomer())
        creditRepository.save(buildCredit())

        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL?customerId=${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize<Any>(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditCode").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditValue").value(1000.0))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].numberOfInstallments").value(5))
            .andDo(MockMvcResultHandlers.print())

    }


    @Test
    fun `should find a credit by creditCode and return 200 status`() {
        val creditCode = UUID.randomUUID()
        val customer = this.customerRepository.save(buildCustomer())
        val credit: Credit = creditRepository.save(buildCredit(creditCode = creditCode))

        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/{creditCode}?customerId=${customer.id}", credit.creditCode.toString())
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(credit.creditValue))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallment").value(5))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("IN_PROGRESS"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value(customer.email))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value(customer.income))
            .andDo(MockMvcResultHandlers.print())
    }

    private fun builderCreditDto(
        creditValue: BigDecimal = BigDecimal.valueOf(1000.0),
        dayFirstOfInstallment: LocalDate = LocalDate.now().plusMonths(1),
        numberOfInstallments: Int = 5,
        customerId: Long = 1L,
    ) = CreditDto(
        creditValue = creditValue,
        dayFirstOfInstallment = dayFirstOfInstallment,
        numberOfInstallments = numberOfInstallments,
        customerId = customerId
    )

    private fun builderCustomerDto(
        firstName: String = "Mia",
        lastName: String = "Miante",
        cpf: String = "28475934625",
        email: String = "mia@mia.com",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
        password: String = "1234",
        zipCode: String = "12345-67",
        street: String = "Rua da Mia, 123",
    ) = CustomerDto(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        income = income,
        password = password,
        zipCode = zipCode,
        street = street
    )

    private fun buildCustomer(
        firstName: String = "Mia",
        lastName: String = "Miante",
        cpf: String = "28475934625",
        email: String = "mia@mia.com",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
        password: String = "1234",
        zipCode: String = "12345-67",
        street: String = "Rua da Mia, 123",
        id: Long = 1L
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        address = Address(
            zipCode = zipCode,
            street = street,
        ),
        income = income,
        id = id
    )

    private fun buildCredit(
        creditCode: UUID = UUID.randomUUID(),
        value: BigDecimal = BigDecimal.valueOf(1000.0),
        dayFirstInstallment: LocalDate = LocalDate.now().plusDays(30),
        numberOfInstallments: Int = 5,
        customer: Customer = buildCustomer()
    ): Credit {
        return Credit(
            creditCode = creditCode,
            creditValue = value,
            dayFirstInstallment = dayFirstInstallment,
            numberOfInstallments = numberOfInstallments,
            customer = customer
        )
    }
}
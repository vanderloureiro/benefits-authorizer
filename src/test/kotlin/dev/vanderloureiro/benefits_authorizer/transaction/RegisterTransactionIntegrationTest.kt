package dev.vanderloureiro.benefits_authorizer.transaction

import com.fasterxml.jackson.databind.ObjectMapper
import dev.vanderloureiro.benefits_authorizer.transaction.io.TransactionRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.math.BigDecimal

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisterTransactionIntegrationTest {

    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    lateinit var transactionResource: TransactionResource

    lateinit var mvc: MockMvc;

    @BeforeEach
    fun setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .build()
    }

    @Test
    fun whenSendFoodRequestAndHasAmount_registerBalance() {
        val request = TransactionRequest(1, BigDecimal(100.0), "Padaria do Zé", "5811");

        this.mvc.perform(MockMvcRequestBuilders
            .post("/v3/transactions")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
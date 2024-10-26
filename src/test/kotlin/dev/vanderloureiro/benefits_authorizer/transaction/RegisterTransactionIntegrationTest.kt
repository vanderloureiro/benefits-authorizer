package dev.vanderloureiro.benefits_authorizer.transaction

import com.fasterxml.jackson.databind.ObjectMapper
import dev.vanderloureiro.benefits_authorizer.account.AccountRepository
import dev.vanderloureiro.benefits_authorizer.transaction.io.TransactionRequest
import org.assertj.core.api.Assertions.assertThat
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
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisterTransactionIntegrationTest {

    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var accountRepository: AccountRepository;

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

    @Test
    fun whenTheFoodBalanceRunsAndHasCash_registerCashBalance() {
        /*
        * Conta de id 2 tem 200 reais em vale refeição e 300 em cash
        * */
        val accountId = 2L;
        val request = TransactionRequest(accountId, BigDecimal(250.0), "Mercearia do Zé", "5811");

        val accountBefore = accountRepository.findById(accountId).get();

        this.mvc.perform(MockMvcRequestBuilders
            .post("/v3/transactions")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());

        val accountAfter = accountRepository.findById(accountId).get();
        assertEquals(accountAfter.cashAmount, accountBefore.cashAmount.subtract(BigDecimal(250)))
        assertEquals(accountAfter.mealAmount, accountBefore.mealAmount)
    }

    @Test
    fun whenSendIncorrectMcc_registerBalanceByMerchant() {
        val accountId = 1L;
        val request = TransactionRequest(accountId, BigDecimal(100.0), "Restaurante do Zé", "5811");

        val accountBefore = accountRepository.findById(accountId).get();

        this.mvc.perform(MockMvcRequestBuilders
            .post("/v3/transactions")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());

        val accountAfter = accountRepository.findById(accountId).get();
        assertEquals(accountAfter.foodAmount, accountBefore.foodAmount.subtract(BigDecimal(100)))
        assertEquals(accountAfter.mealAmount, accountBefore.mealAmount)
    }

    @Test
    fun whenHasNoAmount_doNotRegisterBalance() {
        val request = TransactionRequest(3, BigDecimal(500.0), "Padaria do Zé", "5811");
        val balanceErrorStatusCode: String = "51";

        val result = this.mvc.perform(
            MockMvcRequestBuilders
                .post("/v3/transactions")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        assertThat(result.response.contentAsString.contains(balanceErrorStatusCode))
    }

}
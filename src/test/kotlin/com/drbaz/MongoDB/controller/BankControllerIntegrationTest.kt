package com.drbaz.MongoDB.controller

import com.drbaz.MongoDB.model.Bank
import com.drbaz.MongoDB.repository.BankRepository
import com.drbaz.MongoDB.request.BankRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.*
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension:: class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class BankControllerIntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    private val bankRepository: BankRepository,
    private val restTemplate: TestRestTemplate
) {
    private val defaultBankId = ObjectId.get()

    @LocalServerPort
    protected var port: Int = 0

    @BeforeEach
    fun setUp() {
        bankRepository.deleteAll()
    }

    private fun getRootUrl(): String = "http://localhost:$port/banks"

    private fun saveOneBank() = bankRepository.save(Bank(defaultBankId, "Account", 2.9, 1))

    private fun prepareBankRequest() = BankRequest("defaultAccNo",3.9,2)

    @Test
    fun `should return all tasks mockMvc`() {
        saveOneBank()
        mockMvc.get(getRootUrl().toString())
            .andDo { print("***** ALL BANKS TEST REPORT *****") }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }

    }
    @Test
    fun `should return all tasks response`() {
        saveOneBank()
        val response = restTemplate.getForEntity(getRootUrl(), List::class.java)
        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(1, response.body?.size)
    }

    @Test
    fun `should return single task by id`() {
        saveOneBank()
        mockMvc.get(getRootUrl()+"/$defaultBankId")
            .andDo{ print("****** SINGLE BANK TEST REPORT *****")}
            .andDo{ print()}
            .andExpect{
                status {isOk()}
            }
        /*val response = restTemplate.getForEntity(
            getRootUrl() + "/$defaultBankId",
            Bank::class.java
        )

        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(defaultBankId, response.body?.id)

         */
    }
    @Test
    fun `should create a new bank` () {
        val bankRequest = prepareBankRequest()
        val performPost = mockMvc.post(getRootUrl().toString()) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(bankRequest)
        }

        performPost
            .andDo{ print("****** CREATE BANK TEST REPORT *****")}
            .andDo{ print()}
            .andExpect{
                status {isCreated()}
            }
           }//end of test
    @Test
    fun `should update existing task`() {
        saveOneBank()
        val bankRequest = prepareBankRequest()

        val updateResponse = restTemplate.exchange(
            getRootUrl() + "/$defaultBankId",
            HttpMethod.PUT,
            HttpEntity(bankRequest, HttpHeaders()),
            Bank::class.java
        )
        val updatedTask = bankRepository.findOneById(defaultBankId)

        assertEquals(200, updateResponse.statusCode.value())
        assertEquals(defaultBankId, updatedTask.id)
        assertEquals(bankRequest.accountNumber, updatedTask.accountNumber)
        assertEquals(bankRequest.trust, updatedTask.trust)
    }// end of Test
    @Test
    fun `should delete existing bank` () {
        //given
        saveOneBank()
        val delete = restTemplate.exchange(
            getRootUrl() + "/$defaultBankId",
            HttpMethod.DELETE,
            HttpEntity(null, HttpHeaders()),
            ResponseEntity::class.java
        )

        assertEquals(204, delete.statusCode.value())
        assertThrows(EmptyResultDataAccessException::class.java) { bankRepository.findOneById(defaultBankId) }
        //when

        //then

    }//end of test
}
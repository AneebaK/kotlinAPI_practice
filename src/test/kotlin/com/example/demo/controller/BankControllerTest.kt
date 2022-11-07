package com.example.demo.controller

import com.example.demo.Models.Bank
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor(
        val mockMvc: MockMvc,
        val objectMapper: ObjectMapper
){
    val baseUrl = "/api/banks"

    @Nested
    @DisplayName("getBanks()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBanks {

        @Test
        fun `should get all Banks`(){
            mockMvc.get("$baseUrl")
                    .andDo { print() }
                    .andExpect {
                        status { isOk() }
                        content { contentType(MediaType.APPLICATION_JSON) }
                        jsonPath("$[0].accountNumber"){value("1234")}
                    }

        }
    }


    @Nested
    @DisplayName("getBank()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBank {
        @Test
        fun `should return one Bank with given account no`(){
            val  accountNum = 1234
            mockMvc.get("$baseUrl/$accountNum")
                    .andDo { print() }
                    .andExpect {
                        status { isOk() }
                        content { contentType(MediaType.APPLICATION_JSON) }
                        jsonPath("$.interestRate"){value("1.2")}
                    }

        }

        @Test
        fun `should return Not Found if no bank exit with this account no`(){
            //Given
            val  accountNo = "bank_doesnt_exist"
            //When Then
            mockMvc.get("$baseUrl/$accountNo")
                    .andDo { print() }
                    .andExpect {
                        status { isNotFound() }
                    }

        }

    }

    @Nested
    @DisplayName("POST postBank()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostNewBank {

        @Test
        fun `should post a new Bank`(){
            //Given
            val newBank = Bank("acc12", 21.24,2)
            //When Then
            val response = mockMvc.post("$baseUrl"){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank)
            }
            response.andDo { print() }
                    .andExpect {
                        status { isCreated() }
                        content {
                            contentType(MediaType.APPLICATION_JSON)
                            json(objectMapper.writeValueAsString(newBank))
                        }
                        jsonPath("$.accountNumber"){value("acc12")}
                    }
            mockMvc.get("$baseUrl/${newBank.accountNumber}").andExpect {
                content { json(objectMapper.writeValueAsString(newBank)) }
            }

        }

        @Test
        fun `should return Bad Request`(){
            //Given
            val invalidBank = Bank("acc12", 21.24,2)
            //When Then
            val response = mockMvc.post("$baseUrl"){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }
            response.andDo { print() }
                    .andExpect {
                        status { isBadRequest() }
                    }

        }
    }

    @Nested
    @DisplayName("PATCH patchBank()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class UpdateBank {
        @Test
        fun `should Update Bank`() {
            //Given
            val updateBank = Bank("acc12", 21.24, 2)
            //When Then
            val responsePatch = mockMvc.patch("$baseUrl") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updateBank)


            }
            responsePatch
                    .andDo { print() }
                    .andExpect {
                        status { isOk() }
                        content {
                            contentType(MediaType.APPLICATION_JSON)
                            json(objectMapper.writeValueAsString(updateBank))
                        }
                    }
            mockMvc.get("$baseUrl/${updateBank.accountNumber}").andExpect {
                content { json(objectMapper.writeValueAsString(updateBank)) }
            }
        }

        @Test
        fun `should return Bad Request if account number dont exist`(){
            //Given
            val invalidBank = Bank("bank_dont_exist", 21.24,2)
            //When Then
            val response = mockMvc.patch("$baseUrl"){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }
            response.andDo { print() }
                    .andExpect {
                        status { isNotFound() }
                    }

        }
    }


    @Nested
    @DisplayName("DELETE deleteBank()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteBank {
        @Test
        fun `should Delete given bank`() {
            val accountNum = 1234
            mockMvc.delete("$baseUrl/$accountNum")
                    .andDo { print() }
                    .andExpect {
                        status { isNoContent() }
                    }
            mockMvc.get("$baseUrl/$accountNum")
                    .andExpect {
                        status { isNotFound() }
                    }
        }
        @Test
        fun `should return Not Found `() {
            val invalidAccountNum = "account_dont_exist"
            mockMvc.delete("$baseUrl/$invalidAccountNum")
                    .andDo { print() }
                    .andExpect {
                        status { isNotFound() }
                    }
        }
    }

}
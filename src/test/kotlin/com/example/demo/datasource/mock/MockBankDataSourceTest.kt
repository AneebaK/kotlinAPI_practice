package com.example.demo.datasource.mock

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MockBankDataSourceTest{

    private val mockDataSource = MockBankDataSource()

    @Test
    fun `should provide a collection of Banks`(){
        //When
        val banks = mockDataSource.retrieveBanks()
        //Then
        assertThat(banks).isNotEmpty

    }

    @Test
    fun `should provide mock data of Banks`(){
        //When
        val banks = mockDataSource.retrieveBanks()
        //Then
        assertThat(banks).allMatch { it.accountNumber.isNotBlank() }
        assertThat(banks).anyMatch { it.interestRate !== 0.0 }
        assertThat(banks).allMatch { it.transactionFee !== 0 }

    }
}
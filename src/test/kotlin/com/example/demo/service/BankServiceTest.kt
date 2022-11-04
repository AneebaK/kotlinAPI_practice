package com.example.demo.service

import com.example.demo.datasource.BankDataSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BankServiceTest{

    private val dataSource : BankDataSource = mockk(relaxed = true)
    private val bankService = BankService(dataSource)
    @Test
    fun `should retrieve bank data source`(){

        every { dataSource.retrieveBanks() } returns emptyList()
        //When
        bankService.getBanks()
        //Then
        verify(exactly = 1) { dataSource.retrieveBanks() }


    }
}
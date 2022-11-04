package com.example.demo.datasource.mock

import com.example.demo.Models.Bank
import com.example.demo.datasource.BankDataSource
import org.springframework.stereotype.Repository

@Repository
class MockBankDataSource: BankDataSource {
    val banks = listOf(
            Bank("1234",1.20,9),
            Bank("5678",4.20,6),
            Bank("123476",7.20,9)
    )

    override fun retrieveBanks(): Collection<Bank> = banks
    override fun retrieveBank(accountNumber: String): Bank {
        return banks.first { it.accountNumber == accountNumber }
    }
}
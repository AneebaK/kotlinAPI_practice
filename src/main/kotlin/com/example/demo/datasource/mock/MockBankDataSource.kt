package com.example.demo.datasource.mock

import com.example.demo.Models.Bank
import com.example.demo.datasource.BankDataSource
import org.springframework.stereotype.Repository

@Repository
class MockBankDataSource: BankDataSource {
    val banks = mutableListOf(
            Bank("1234",1.20,9),
            Bank("5678",4.20,6),
            Bank("123476",7.20,9)
    )

    override fun retrieveBanks(): Collection<Bank> = banks
    override fun retrieveBank(accountNumber: String): Bank {
        return banks.firstOrNull { it.accountNumber == accountNumber }
                ?:throw NoSuchElementException("Bank with account no $accountNumber does not exist")
    }

    override fun createBank(bank: Bank): Bank {
        if (banks.any{ it.accountNumber == bank.accountNumber}){
            throw IllegalArgumentException("Account no ${bank.accountNumber} already exists")
        }
        banks.add(bank)
        return bank
    }

    override fun updateBank(bank: Bank): Bank {
        val currentBank = banks.firstOrNull{it.accountNumber == bank.accountNumber}
                ?: throw NoSuchElementException("Bank with account no ${bank.accountNumber} does not exist")
        banks.remove(currentBank)
        banks.add(bank)
        return bank
    }

    override fun deleteBank(accountNumber: String): Unit {
        val currentBank = banks.firstOrNull{it.accountNumber == accountNumber}
                ?: throw NoSuchElementException("Bank with account no $accountNumber does not exist")
        banks.remove(currentBank)
    }
}
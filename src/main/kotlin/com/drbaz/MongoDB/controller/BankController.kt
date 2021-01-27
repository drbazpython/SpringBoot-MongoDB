package com.drbaz.MongoDB.controller

import com.drbaz.MongoDB.model.Bank
import com.drbaz.MongoDB.repository.BankRepository
import com.drbaz.MongoDB.request.BankRequest
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/banks")
class BankController ( private val bankRepository: BankRepository
) {
    @GetMapping
    fun getAllBanks(): ResponseEntity<List<Bank>> {
        val banks = bankRepository.findAll()
        return ResponseEntity.ok(banks)
    }

    @GetMapping("/{id}")
    fun getOneTask(@PathVariable("id") id: String): ResponseEntity<Bank> {
        val task = bankRepository.findOneById(ObjectId(id))
        return ResponseEntity.ok(task)
    }

    @PostMapping
    fun createBank(@RequestBody request: BankRequest): ResponseEntity<Bank> {
        val task = bankRepository.save(
            Bank(
                accountNumber = request.accountNumber,
                trust = request.trust,
                transactionFee = request.transactionFee
            ))
        return ResponseEntity(task, HttpStatus.CREATED)

    }//end of POSTMAPPING

    @PutMapping("/{id}")
    fun updateBank(@RequestBody request: BankRequest, @PathVariable("id") id: String): ResponseEntity<Bank> {
        val bank = bankRepository.findOneById(ObjectId(id))
        val updatedBank= bankRepository.save(Bank(
            id = bank.id,
            accountNumber = request.accountNumber,
            trust = request.trust,
            transactionFee = request.transactionFee
        ))
        return ResponseEntity.ok(updatedBank)
    }// end of PUTMAPPING

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable("id") id: String): ResponseEntity<Unit> {
        bankRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }//end of DELETEMAPPIMG
}
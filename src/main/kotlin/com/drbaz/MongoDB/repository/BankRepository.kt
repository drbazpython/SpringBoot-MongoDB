package com.drbaz.MongoDB.repository

import com.drbaz.MongoDB.model.Bank
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface BankRepository: MongoRepository<Bank, String> {
    fun findOneById(id: ObjectId): Bank
}
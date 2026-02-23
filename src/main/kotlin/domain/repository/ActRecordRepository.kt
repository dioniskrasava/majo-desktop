package app.majodesk.domain.repository

import app.majodesk.domain.model.ActRecord

interface ActRecordRepository {
    fun createRecord(record: ActRecord)
    fun getRecordById(id: Long): ActRecord?
    fun getAllRecords(): List<ActRecord>
    fun getRecordsByActId(actId: Long): List<ActRecord>
    fun updateRecord(record: ActRecord)
    fun deleteRecord(id: Long)
}
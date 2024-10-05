package com.example.swissknife.data.task

import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.swissknife.domain.task.Task
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "tasks.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "tasks"
        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "nome"
        const val COLUMN_TASK_DESCRIPTION = "descricao"
        const val COLUMN_CREATION_DATE = "data_criacao"
        const val COLUMN_EDIT_DATE = "data_edicao"
        const val COLUMN_SCHEDULE_DATE = "data_agendamento"
        const val COLUMN_NOTIFICATION_TIME = "tempo_notificacao"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable =
            "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME TEXT NOT NULL," +
                "$COLUMN_TASK_DESCRIPTION TEXT," +
                "$COLUMN_CREATION_DATE TEXT NOT NULL," +
                "$COLUMN_EDIT_DATE TEXT," +
                "$COLUMN_SCHEDULE_DATE TEXT," +
                "$COLUMN_NOTIFICATION_TIME INTEGER)"
        db.execSQL(createTable)
    }

    fun addTask(taskName: String, taskDescription: String, creationDate: Date, editDate: Date?,
                scheduleDate: Date?, notificationTime: Int?) : Int {
        val db = writableDatabase

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val creationDateStr = dateFormat.format(creationDate)
        val editDateStr = editDate?.let { dateFormat.format(it) }
        val scheduleDateStr = scheduleDate?.let { dateFormat.format(it) }

        val values = ContentValues().apply {
            put(COLUMN_NAME, taskName)
            put(COLUMN_TASK_DESCRIPTION, taskDescription)
            put(COLUMN_CREATION_DATE, creationDateStr)
            put(COLUMN_EDIT_DATE, editDateStr)
            put(COLUMN_SCHEDULE_DATE, scheduleDateStr)
            put(COLUMN_NOTIFICATION_TIME, notificationTime)
        }
        val taskId = db.insert(TABLE_NAME, null, values)
        db.close()
        return taskId.toInt()
    }

    fun updateTask(id: Int, taskName: String, taskDescription: String, creationDate: Date, editDate: Date?, scheduleDate: Date?, notificationTime: Int?) {
        val db = this.writableDatabase

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val creationDateStr = dateFormat.format(creationDate)
        val editDateStr = editDate?.let { dateFormat.format(it) }
        val scheduleDateStr = scheduleDate?.let { dateFormat.format(it) }

        val values = ContentValues().apply {
            put(COLUMN_NAME, taskName)
            put(COLUMN_TASK_DESCRIPTION, taskDescription)
            put(COLUMN_CREATION_DATE, creationDateStr)
            put(COLUMN_EDIT_DATE, editDateStr)
            put(COLUMN_SCHEDULE_DATE, scheduleDateStr)
            put(COLUMN_NOTIFICATION_TIME, notificationTime)
        }

        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun getTasks(ids: List<Int>): List<Task> {
        val taskList = mutableListOf<Task>()
        val db = readableDatabase
        val cursor: Cursor?

        if (ids.isEmpty()) {
            cursor = db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
            )
        }
        else {
            val placeholders = ids.joinToString(",") { "?" }
            val selection = "$COLUMN_ID IN ($placeholders)"
            val selectionArgs = ids.map { it.toString() }.toTypedArray()

            cursor = db.query(
                TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
            )
        }

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))
                val name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))
                val description = it.getString(it.getColumnIndexOrThrow(COLUMN_TASK_DESCRIPTION))
                val creationDateStr = it.getString(it.getColumnIndexOrThrow(COLUMN_CREATION_DATE))
                val editDateStr = it.getString(it.getColumnIndexOrThrow(COLUMN_EDIT_DATE))
                val scheduleDateStr = it.getString((it.getColumnIndexOrThrow(COLUMN_SCHEDULE_DATE)))
                val notificationTime = it.getInt(it.getColumnIndexOrThrow(
                    COLUMN_NOTIFICATION_TIME))

                val dateFormat = SimpleDateFormat("yyyy-MM-dd")

                val creationDate: Date = dateFormat.parse(creationDateStr)

                val editDate: Date? = try {
                    dateFormat.parse(editDateStr)
                } catch (e: Exception) {
                    null
                }

                val scheduleDate: Date? = try {
                    dateFormat.parse(scheduleDateStr)
                } catch (e: Exception) {
                    null
                }

                val task = Task(id, name, description, creationDate, editDate, scheduleDate,
                    notificationTime)
                taskList.add(task)
            }
        }

        return taskList
    }

    fun deleteTask(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}
package uz.umarxon.registration.DB

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import uz.umarxon.registration.DB.Const.ADDRESS
import uz.umarxon.registration.DB.Const.COUNTRIES
import uz.umarxon.registration.DB.Const.DB_NAME
import uz.umarxon.registration.DB.Const.DB_VERSION
import uz.umarxon.registration.DB.Const.ID
import uz.umarxon.registration.DB.Const.IMG
import uz.umarxon.registration.DB.Const.NAME
import uz.umarxon.registration.DB.Const.PASSWORD
import uz.umarxon.registration.DB.Const.PHONE
import uz.umarxon.registration.DB.Const.TABLE_NAME
import uz.umarxon.registration.Models.User

class DbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION),
    ServiceInterface {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_NAME ($ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,$NAME TEXT NOT NULL,$PHONE TEXT NOT NULL,$COUNTRIES INTEGER NOT NULL,$ADDRESS TEXT NOT NULL,$PASSWORD TEXT NOT NULL,$IMG TEXT NOT NULL)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    override fun addMember(user: User) {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(NAME, user.name)
        cv.put(PHONE, user.phone)
        cv.put(COUNTRIES, user.countries)
        cv.put(ADDRESS, user.address)
        cv.put(PASSWORD, user.password)
        cv.put(IMG,user.image)

        db.insert(TABLE_NAME, null, cv)
        db.close()
    }

    override fun editMember(user: User): Int {
        return 0
    }

    override fun deleteMember(user: User) {

    }

    @SuppressLint("Recycle")
    override fun getAllMember(): ArrayList<User> {
        val list = ArrayList<User>()
        val query = "Select * From $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val user = User()
                user.id = cursor.getInt(0)
                user.name = cursor.getString(1)
                user.phone = cursor.getString(2)
                user.countries = cursor.getInt(3)
                user.address = cursor.getString(4)
                user.password = cursor.getString(5)
                user.image = cursor.getString(6)
                list.add(user)
            } while (cursor.moveToNext())
        }

        return list
    }


}
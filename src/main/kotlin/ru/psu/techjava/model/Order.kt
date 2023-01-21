package ru.psu.techjava.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.json.JsonObject

/********************************************************************************************************
  Организация
  Описание: класс для хранения данных об организациях и для преобразования данных из JSON и обратно
 *******************************************************************************************************/
class Order (
    id : Int = 0,
    date : LocalDate = LocalDate.parse("01.01.1978", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
    status : String = "") : JsonModel
{
    val propertyId = SimpleIntegerProperty(id)
    var id by propertyId

    val propertyDate = SimpleObjectProperty(date)
    var date by propertyDate

    val propertyStatus = SimpleStringProperty(status)
    var status by propertyStatus

    /****************************************************************************************************
      Обновление полей текущего класса по данным из JSON
      Вход: json объект
      Выход: обновленные поля организации
     ***************************************************************************************************/
    override fun updateModel( json : JsonObject)
    {
        with(json) {
            id = int("id")!!
            date = LocalDate.parse(string("date"))
            status = string("status")
        }
    }
    /****************************************************************************************************
      Создание JSON по данным из полей текущего класса
      Вход: json конструктор
      Выход: json объект
     ***************************************************************************************************/
    override fun toJSON( json : JsonBuilder)
    {
        with(json) {
            add("id", id)
            add("name", date)
            add("status", status)
        }
    }
    /****************************************************************************************************
      Проверка на соответствие двух объектов друг другу по идентификтаорам
      Вход: other - другой обект для проверки
      Выход: true, если объект описывает ту же сущность
     ***************************************************************************************************/
    override fun equals( other : Any?) = (other is Order) && id == other.id

    /****************************************************************************************************
      Проверка наличия изменений в объекте по сравнению с другим объектом
      Вход: other - другой обект для проверки
      Выход: true, если объект описывает ту же сущность, но имеет отличия в полях
     ***************************************************************************************************/
    fun hasChanges(other : Order) = equals(other) && (date != other.date
            || status != other.status)
}
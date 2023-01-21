package ru.psu.techjava.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javax.json.JsonObject

/********************************************************************************************************
 Студент
 Описание: класс для хранения данных о гражданах и для преобразования данных из JSON и обратно
 *******************************************************************************************************/
class Provider(
    id : Int = 0,
    address : String = "",
    phoneNumber : String= "",
    productName : String= "") : JsonModel
        {
        val propertyId = SimpleIntegerProperty(id)
        var id by propertyId

        val propertyAddress = SimpleStringProperty(address)
        var address by propertyAddress

        val propertyPhoneNumber = SimpleStringProperty(phoneNumber)
        var phoneNumber by propertyPhoneNumber

        val propertyProductName = SimpleStringProperty(productName)
        var productName by propertyProductName

        /****************************************************************************************************
          Обновление полей текущего класса по данным из JSON
          Вход: json объект
          Выход: обновленные поля гражданина
         ***************************************************************************************************/
        override fun updateModel( json : JsonObject)
        {
                with(json) {
                id = int("id")!!
                address = string("address")
                phoneNumber = string("phoneNumber")
                productName = string("productName")
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
                add("address", address)
                add("phoneNumber", phoneNumber)
                add("productName", productName)
                }
        }
        /****************************************************************************************************
         Проверка на соответствие двух объектов друг другу по идентификтаорам
         Вход: other - другой обект для проверки
         Выход: true, если объект описывает ту же сущность
        ***************************************************************************************************/
        override fun equals( other : Any?) = (other is Provider) && id == other.id

        /****************************************************************************************************
         Проверка наличия изменений в объекте по сравнению с другим объектом
         Вход: other - другой обект для проверки
         Выход: true, если объект описывает ту же сущность, но имеет отличия в полях
        ***************************************************************************************************/
        fun hasChanges(other : Provider) = equals(other) && (
        address != other.address ||
        phoneNumber != other.phoneNumber ||
        productName != other.productName
        )
        }
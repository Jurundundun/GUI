package ru.psu.techjava.model

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import javax.json.JsonObject

/********************************************************************************************************
 Обращение
 Описание: класс для хранения данных об обращениях и для преобразования данных из JSON и обратно
 *******************************************************************************************************/
class Position(
    id : Int = 0,
    weight : Double? = 0.1,
    provider : Int? = 0,
    order : Int? = 0) : JsonModel
    {
        val propertyId = SimpleIntegerProperty(id)
        var id by propertyId

        val propertyWeight = SimpleDoubleProperty(weight!!)
        var weight by propertyWeight

        val propertyProvider = SimpleIntegerProperty(provider!!)
        var providerId by propertyProvider

        val propertyOrder = SimpleIntegerProperty(order!!)
        var orderId by propertyOrder

        /****************************************************************************************************
          Обновление полей текущего класса по данным из JSON
          Вход: json объект
          Выход: обновленные поля обращения
         ***************************************************************************************************/
        override fun updateModel( json : JsonObject)
        {
            with(json) {
                id = int("id")!!
                weight = double("weight")!!
                providerId = int("providerId")!!
                orderId = int("orderId")!!
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
                add("weight", weight)
                add("providerId", providerId)
                add("orderId", orderId)
            }
        }
        /****************************************************************************************************
          Проверка на соответствие двух объектов друг другу по идентификтаорам
          Вход: other - другой обект для проверки
          Выход: true, если объект описывает ту же сущность
         ***************************************************************************************************/
        override fun equals( other : Any?) = (other is Position) && id == other.id

        /****************************************************************************************************
          Проверка наличия изменений в объекте по сравнению с другим объектом
          Вход: other - другой обект для проверки
          Выход: true, если объект описывает ту же сущность, но имеет отличия в полях
         ***************************************************************************************************/
        fun hasChanges(other : Position) = equals(other) && (
                weight != other.weight
                        ||
                providerId != other.providerId ||
                orderId != other.orderId
                )

}
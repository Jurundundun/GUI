package ru.psu.techjava.viewmodels

import javafx.beans.property.SimpleBooleanProperty
import ru.psu.techjava.model.Order
import ru.psu.techjava.services.OrderService
import tornadofx.Controller

/********************************************************************************************************
  Модель представления для списка обращений
  Описание: позволяет добавить пустую запись в таблицу, удалить запись
  и отправить все текущие записи на сервер
 *******************************************************************************************************/
class OrderListViewModel : Controller()
{
    //Сервис с логикой для работы со списком обращений
    private val orderService : OrderService by inject()

    //Список обращений с возможность отслеживания изменений
    val orders = orderService.getAll()

    //Факт наличия выделения в таблице
    val elementSelected = SimpleBooleanProperty(false)

    /****************************************************************************************************
      Добавление пустой записи в таблицу
     ***************************************************************************************************/
    fun add()
    {
        orderService.add(Order())
    }
    /****************************************************************************************************
      Удаление записи из списка
      Вход: item - элемент для удаления
     ***************************************************************************************************/
    fun delete(item : Order?)
    {
        //Если элемент не указан - ничего не делаем
        item ?: return
        orderService.delete(item)
    }
    /****************************************************************************************************
      Обработка изменения выделенных элементов в списке
      Вход: selectedItem - выделенный элемент
     ***************************************************************************************************/
    fun onSelectionChange(selectedItem : Order?)
    {
        elementSelected.set(selectedItem != null)
    }
    /****************************************************************************************************
      Отправка всех текущих данных из списка на сервер
     ***************************************************************************************************/
    fun saveAll()
    {
        orderService.saveAll()
    }

}
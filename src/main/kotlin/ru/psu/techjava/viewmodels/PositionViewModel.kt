package ru.psu.techjava.viewmodels

import ru.psu.techjava.model.Position
import tornadofx.ItemViewModel

/********************************************************************************************************
  Модель представления для одной организации
  Описание: описывает данные одного объекта и сохраняет их в кэше
 *******************************************************************************************************/
class PositionViewModel(position : Position) : ItemViewModel<Position>(position)
{
    val id = bind(Position::propertyId)
    val weight = bind(Position :: propertyWeight)
    val provider = bind(Position :: propertyProvider)
    val order = bind(Position :: propertyOrder)

    /****************************************************************************************************
      Сохранение данных из полей формы в объект в оперативной памяти
     ***************************************************************************************************/
    fun save()
    {
        //Сохранение данных из полей на форме в сущность
        this.commit()
    }
}
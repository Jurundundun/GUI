package ru.psu.techjava.viewmodels

import ru.psu.techjava.model.Provider
import tornadofx.ItemViewModel

/********************************************************************************************************
  Модель представления для одного гражданина
  Описание: описывает данные одного объекта и сохраняет их в кэше
 *******************************************************************************************************/
class ProviderViewModel(provider : Provider) : ItemViewModel<Provider>(provider)
{
    val id = bind(Provider::propertyId)
    val address = bind(Provider::propertyAddress)
    val phoneNumber = bind(Provider::propertyPhoneNumber)
    val productName = bind(Provider::propertyProductName)

    /****************************************************************************************************
      Сохранение данных из полей формы в объект в оперативной памяти
     ***************************************************************************************************/
    fun save()
    {
        //Сохранение данных из полей на форме в сущность
        this.commit()
    }
}
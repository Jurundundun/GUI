package ru.psu.techjava.services

import ru.psu.techjava.model.Provider
import ru.psu.techjava.repositories.ProviderRepository
import tornadofx.Controller

/********************************************************************************************************
Сервис с логикой для работы со списком организаций
Описание: взаимодействует с репозиторием организаций
 *******************************************************************************************************/
class ProviderService : Controller()
{
    private val repositoryProvider : ProviderRepository by inject()

    /****************************************************************************************************
    Запрос списка всех доступных объектов на сервере
     ***************************************************************************************************/
    fun getAll() = repositoryProvider.getAll()

    /****************************************************************************************************
    Отправка всех данных из списка на сервер
     ***************************************************************************************************/
    fun saveAll() = repositoryProvider.saveAll()

    /****************************************************************************************************
    Отправка данных для добавления объекта на сервер
    Вход: Organization - объект для отправки
     ***************************************************************************************************/
    fun add(provider : Provider)
    {
        repositoryProvider.add(provider)
    }

    /****************************************************************************************************
    Отправка данных для удаления объекта на сервер
    Вход: Organization - объект для отправки
     ***************************************************************************************************/
    fun delete(provider : Provider) = repositoryProvider.delete(provider)
}
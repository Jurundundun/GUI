package ru.psu.techjava.repositories

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.psu.techjava.model.Provider
import tornadofx.*

/********************************************************************************************************
Репозиторий с запросами к серверу в части списка поставщиков
Описание: обрабатывает данные между сервером и локальным списком
 *******************************************************************************************************/
class ProviderRepository : Controller()
{
    //Объект для отправки запросов к API сервера.
    private val api : Rest by inject()
    //"Кэшированный" список обращений для локальной работы.
    private val provider = FXCollections.observableArrayList<Provider>()

    /****************************************************************************************************
    Запрос списка всех доступных объектов на сервере и сохранение их в локальный список
    Выход: локальный список организаций с сервера
     ***************************************************************************************************/
    fun getAll() : ObservableList<Provider>
    {
        //Запрашиваем актуальные данные на сервере
        val listFromServer = requestAll()
        //Пересохраняем в локальный список, в котором дополнительно будем
        //кэшировать изменения в процессе редактирования таблицы.
        provider.addAll(listFromServer)
        //Возвращаем ссылку на кэш.
        return provider
    }
    /****************************************************************************************************
    Запрос списка организаций на сервере и обработка возможных проблем
    Выход: список организаций с сервера или возможная ошибка
     ***************************************************************************************************/
    private fun requestAll(): ObservableList<Provider>
    {
        var response: Rest.Response? = null
        try {
            //Выполнение GET запроса к пути /organs
            response = api.get("provider")
            if (response.ok()) {
                //Конвертация json в список объектов типа Organization
                return response.list().toModel()
            }
            else if (response.statusCode == 404)
                throw Exception("404")
            else
                throw Exception("getCustomer returned ${response.statusCode} ${response.reason}")
        }
        catch(e : Exception)
        {
            throw Exception("Отсутствует соединение с сервером.", e)
        }
        finally {
            response?.consume()
        }
    }

    /****************************************************************************************************
    Отправка локального списка объектов на сервер
    Описание: перенос изменение данных, имеющихся в локальном списке, на сервер
     ***************************************************************************************************/
    fun saveAll()
    {
        //Запрос данных с сервера
        val organsFromServer = requestAll()
        //Перебираем все организации с сервера, те которых нет в локальном списке удаляем
        organsFromServer.forEach { OrganizationFromServer ->
            if (!provider.contains(OrganizationFromServer))
            {
                api.delete("provider", OrganizationFromServer)
            }
        }
        //Перебираем все записи из локального кэша
        var temp : List<Provider>
        provider.forEach { OrganizationLocal->
            //Для каждой локальной записи находим записи из сервера с такими же id
            temp = organsFromServer.filter { OrganizationFromServer ->
                OrganizationLocal.equals(OrganizationFromServer) }
            //Записи с такими же id фильтруем по наличию изменений
            temp.firstOrNull { OrganizationFromServer ->
                OrganizationLocal.hasChanges(OrganizationFromServer)
            }
                //Если изменения в полях есть, публикуем текущую запись на сервер
                ?.let {api.post("provider", OrganizationLocal)}
            //Если вообще на сервере записей с таким id нет публикуем текущую запись
            if (temp.isEmpty())
                api.post("provider", OrganizationLocal)
        }
    }
    /****************************************************************************************************
    Добавление организации в локальный список.
    Вход: Organization - объект для отправки.
     ***************************************************************************************************/
    fun add(prov : Provider) { provider.add(prov) }

    /****************************************************************************************************
    Удаление организации из локального списка.
    Вход: Organization - объект для отправки.
     ***************************************************************************************************/
    fun delete(prov : Provider) { provider.remove(prov) }
}
package ru.psu.techjava.repositories

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.psu.techjava.model.Position
import tornadofx.*

/********************************************************************************************************
 Репозиторий с запросами к серверу в части списка граждан
 Описание: обрабатывает данные между сервером и локальным списком
 *******************************************************************************************************/
class PositionRepository : Controller()
{
    //Объект для отправки запросов к API сервера.
    private val api : Rest by inject()
    //"Кэшированный" список обращений для локальной работы.
    private val position = FXCollections.observableArrayList<Position>()

    /****************************************************************************************************
     Запрос списка всех доступных объектов на сервере и сохранение их в локальный список
     Выход: локальный список граждан с сервера
     ***************************************************************************************************/
    fun getAll() : ObservableList<Position>
    {
        //Запрашиваем актуальные данные на сервере
        val listFromServer = requestAll()
        //Пересохраняем в локальный список, в котором дополнительно будем
        //кэшировать изменения в процессе редактирования таблицы.
        position.addAll(listFromServer)
        //Возвращаем ссылку на кэш.
        return position
    }
    /****************************************************************************************************
     Запрос списка граждан на сервере и обработка возможных проблем
     Выход: список граждан с сервера или возможная ошибка
     ***************************************************************************************************/
    private fun requestAll() : ObservableList<Position>
    {
        var response : Rest.Response? = null
        try {
            //Выполнение GET запроса к пути /citizens
            response = api.get("position")
            if (response.ok()) {
                //Конвертация json в список объектов типа Citizen
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
        val citizensFromServer = requestAll()

        //Перебираем всех граждан с сервера, тех которых нет в локальном списке удаляем
        citizensFromServer.forEach { CitizenFromServer ->
            if (!position.contains(CitizenFromServer))
            {
                api.delete("position", CitizenFromServer)
            }
        }
        //Перебираем все записи из локального кэша
        var temp : List<Position>
        position.forEach { CitizenLocal->
            //Для каждой локальной записи находим записи из сервера с такими же id
            temp = citizensFromServer.filter { CitizenFromServer ->
                CitizenLocal.equals(CitizenFromServer) }
            //Записи с такими же id фильтруем по наличию изменений
            temp.firstOrNull { CitizenFromServer ->
                CitizenLocal.hasChanges(CitizenFromServer)
            }
                //Если изменения в полях есть, публикуем текущую запись на сервер
                ?.let {api.post("position", CitizenLocal)}
            //Если вообще на сервере записей с таким id нет публикуем текущую запись
            if (temp.isEmpty())
                api.post("position", CitizenLocal)
        }
    }
    /****************************************************************************************************
    Добавление гражданина в локальный список.
    Вход: Citizen - объект для отправки.
     ***************************************************************************************************/
    fun add(pos : Position) { position.add(pos) }

    /****************************************************************************************************
    Удаление гражданина из локального списка.
    Вход: Citizen - объект для отправки.
     ***************************************************************************************************/
    fun delete(pos : Position) { position.remove(pos) }
}
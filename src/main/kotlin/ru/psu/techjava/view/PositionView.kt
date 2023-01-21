package ru.psu.techjava.view

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.layout.BorderPane
import ru.psu.techjava.model.Order
import ru.psu.techjava.model.Position
import ru.psu.techjava.model.Provider
import ru.psu.techjava.viewmodels.*
import tornadofx.*

/********************************************************************************************************
 Окно граждан
 Описание: окно с меню, таблицей граждан и частью окна для её редактирования
 *******************************************************************************************************/
class PositionView : View("Positions")
{
    //Корневой элемент формы - элемент с 5ю областями (Верх, Низ, Левая, Правая, Центр)
    override val root = BorderPane()
    val position: Position by param()
    //Модель представления для списка в целом
    private val viewModelList : PositionListViewModel by inject()
    //Модель представления для списка должностей.
    //Модель представления для одного элемента,
    //отображаемого в правой боковой панели редактирвоания
    val viewModelItem = PositionViewModel(Position())

    //Таблица граждан
    //В качестве параметра передаём список граждан из модели представления
    val table = tableview(viewModelList.positions)
    {
        isEditable = true
        column(messages["ID"], Position::propertyId).makeEditable()
        column(messages["Weight"], Position::propertyWeight).makeEditable()
        column(messages["Provider"], Position::propertyProvider).makeEditable()
        column(messages["Order"], Position::propertyOrder).makeEditable()

        //Изменение веделения предаём в модель представления правой формы
        viewModelItem.rebindOnChange(this) { selectedItem -> item = selectedItem ?: Position() }
        //Изменение веделения передаём в модель представления всего списка
        onSelectionChange {
            viewModelList.onSelectionChange(this.selectedItem)
        }
    }

    init {
        //Верхняя часть окна
        root.top{
            //Делится на 2 строки
            vbox {
                //Самая верхняя строка - навигация по окнам
                menubar {
                    menu(messages["Select_window"]) {
                        item(messages["Orders"]).action{
                            replaceWith<OrderView>()
                        }
                        item(messages["Providers"]).action{
                            replaceWith<ProviderView>()
                        }
                    }
                }
                //Вторая строчка сверху - кнопки для работы со списком объектов
                hbox {
                    //Кнопка для добавления нового пустого элемента таблицы
                    button(messages["Add"]) {
                        action{
                            viewModelList.add()
                        }
                    }
                    //Кнопка для удаления элемента из таблицы
                    button(messages["Delete"]) {
                        //Активность кнопки удалить зависит от поля в модели представления
                        enableWhen(viewModelList.elementSelected)
                        action{
                            viewModelList.delete(table.selectedItem)
                        }
                    }
                    //Кнопка для передачи данных из представления в БД
                    button(messages["Save"]) {
                        action{
                            viewModelList.saveAll()
                        }
                    }
                }
            }
        }
        root.center{
            //В центральной части располагается таблица
            this += table
        }
        root.right  {
            //В правой части располагается форма для редактирования одного объекта
            form{
                fieldset(messages["Edit_position"]) {
                    field(messages["ID"]) {
                        textfield(viewModelItem.id)
                    }
                    field(messages["Weight"]) {
                        textfield(viewModelItem.weight)
                    }
                    field(messages["Provider"]) {
                        textfield(viewModelItem.provider)
                    }
                    field(messages["Order"]) {
                        textfield(viewModelItem.order)
                    }
                    hbox{
                        //Кнопка для сохранения изменений из полей формы в сущность
                        button(messages["Save"]) {
                            enableWhen(viewModelItem.dirty)
                            action{
                                viewModelItem.save()
                            }
                        }
                        //Кнопка для отката изменений к первоначальному состоянию
                        button(messages["Cancel"]).action {
                            viewModelItem.rollback()
                        }
                    }
                }
            }
        }
    }
}
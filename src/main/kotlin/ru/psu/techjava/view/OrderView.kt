package ru.psu.techjava.view

import javafx.scene.layout.BorderPane
import ru.psu.techjava.model.Order
import ru.psu.techjava.viewmodels.OrderViewModel
import ru.psu.techjava.viewmodels.OrderListViewModel
import tornadofx.*


/********************************************************************************************************
  Окно обращений
  Описание: окно с меню, таблицей обращений и частью окна для её редактирования
 *******************************************************************************************************/
class OrderView: View("Orders") {

    //Корневой элемент формы - элемент с 5ю областями (Верх, Низ, Левая, Правая, Центр)
    override val root = BorderPane()
    //Модель представления для списка в целом.
    private val viewModelList : OrderListViewModel by inject()
    //Модель представления для одного элемента,
    //отображаемого в правой боковой панели редактирвоания.
    val viewModelItem = OrderViewModel(Order())

    //Таблица обращений
    //В качестве параметра передаём список обращений из модели представления
    val table = tableview(viewModelList.orders)
    {
        isEditable = true
        column(messages["ID"], Order::propertyId).makeEditable()
        column(messages["Date"], Order::propertyDate).makeEditable()
        column(messages["Status"], Order::propertyStatus).makeEditable()

        //Изменение веделения предаём в модель представления правой формы.
        viewModelItem.rebindOnChange(this) { selectedItem -> item = selectedItem ?: Order() }
        //Изменение веделения передаём в модель представления всего списка.
        onSelectionChange { viewModelList.onSelectionChange(this.selectedItem) }
    }

    init {
        //Верхняя часть окна
        root.top{
            //Делится на 2 строки
            vbox {
                //Самая верхняя строка - меню.
                menubar {
                    menu(messages["Select_window"]) {
                        item(messages["Positions"]).action{
                            //Замена содержимого окна на другое окно
                            replaceWith<PositionView>()
                        }
                        item(messages["Providers"]).action{
                            replaceWith<ProviderView>()
                        }
                    }
                }
                //Вторая строчка сверху - кнопки для работы со списком объектов.
                hbox {
                    button(messages["Add"]) {
                        action{
                            viewModelList.add()
                        }
                    }
                    button(messages["Delete"]) {
                        //Активность кнопки удалить зависит от поля в модели представления.
                        enableWhen(viewModelList.elementSelected)
                        action{
                            viewModelList.delete(table.selectedItem)
                        }
                    }
                    button(messages["Save"]) {
                        action{
                            viewModelList.saveAll()
                        }
                    }
                }
            }
        }
        root.center{
            //В центральной части располагается таблица.
            this += table
        }
        root.right  {
            //В правой части располагается форма для редактирования одного объекта.
            form{
                fieldset(messages["Edit_order"]) {
                    field(messages["ID"]) {
                        textfield(viewModelItem.id)
                    }
                    field(messages["Date"]) {
                        textfield(viewModelItem.date.toString())
                    }
                    field(messages["Status"]) {
                        textfield(viewModelItem.status)
                    hbox{
                        button(messages["Save"]) {
                            enableWhen(viewModelItem.dirty)
                            action{
                                viewModelItem.save()
                            }
                        }
                        button(messages["Cancel"]).action {
                            viewModelItem.rollback()
                        }
                    }
                }
            }
        }
    }
}}
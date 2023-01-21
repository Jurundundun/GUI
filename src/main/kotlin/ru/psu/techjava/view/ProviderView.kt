package ru.psu.techjava.view

import javafx.scene.layout.BorderPane
import ru.psu.techjava.model.Order
import ru.psu.techjava.model.Provider
import ru.psu.techjava.viewmodels.PositionViewModel
import ru.psu.techjava.viewmodels.PositionListViewModel
import ru.psu.techjava.viewmodels.ProviderListViewModel
import ru.psu.techjava.viewmodels.ProviderViewModel
import tornadofx.*

/********************************************************************************************************
 Окно организаций
 Описание: окно с меню, таблицей организаций и частью окна для её редактирования
 *******************************************************************************************************/
class ProviderView: View("Providers") {

    //Корневой элемент формы - элемент с 5ю областями (Верх, Низ, Левая, Правая, Центр)
    override val root = BorderPane()
    //Модель представления для списка в целом
    private val viewModelList : ProviderListViewModel by inject()
    //Модель представления для одного элемента,
    //отображаемого в правой боковой панели редактирвоания
    val viewModelItem = ProviderViewModel(Provider())

    //Таблица поставщиков
    //В качестве параметра передаём список поставщиков из модели представления
    val table = tableview(viewModelList.providers)
    {
        isEditable = true
        column(messages["ID"], Provider::propertyId).makeEditable()
        column(messages["Address"], Provider::propertyAddress).makeEditable()
        column(messages["PhoneNumber"], Provider::propertyPhoneNumber).makeEditable()
        column(messages["ProductName"], Provider::propertyProductName).makeEditable()

        //Изменение выделения предаём в модель представления правой формы
        viewModelItem.rebindOnChange(this) { selectedItem -> item = selectedItem ?: Provider() }
        //Изменение выделения передаём в модель представления всего списка
        onSelectionChange { viewModelList.onSelectionChange(this.selectedItem) }
    }

    init {
        //Верхняя часть окна
        root.top{
            //Делится на 2 строки
            vbox {
                //Самая верхняя строка - меню
                menubar {
                    menu(messages["Select_window"]) {
                        item(messages["Positions"]).action{
                            //Замена содержимого окна на другое окно
                            replaceWith<PositionView>()
                        }
                        item(messages["Orders"]).action{
                            replaceWith<OrderView>()
                        }
                    }
                }
                //Вторая строчка сверху - кнопки для работы со списком объектов
                hbox {
                    button(messages["Add"]) {
                        action{
                            viewModelList.add()
                        }
                    }
                    button(messages["Delete"]) {
                        //Активность кнопки удалить зависит от поля в модели представления
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
            //В центральной части располагается таблица
            this += table
        }
        root.right  {
            //В правой части располагается форма для редактирования одного объекта
            form{
                fieldset(messages["Edit_provider"]) {
                    field(messages["ID"]) {
                        textfield(viewModelItem.id)
                    }
                    field(messages["Address"]) {
                        textfield(viewModelItem.address)
                    }
                    field(messages["PhoneNumber"]) {
                        textfield(viewModelItem.phoneNumber)
                    }
                    field(messages["ProductName"]) {
                        textfield(viewModelItem.productName)
                    }
                    }
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
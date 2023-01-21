package ru.psu.techjava.app

import javafx.stage.Stage
import ru.psu.techjava.view.OrderView
import ru.psu.techjava.view.ProviderView
import tornadofx.App
import tornadofx.FX
import tornadofx.Rest
import java.net.InetAddress
import java.util.*

class CApplication: App(ProviderView::class, Styles::class){
    private val api: Rest by inject()
    init {
        //Язык интерфейса приложения.
        FX.locale = Locale("ru")
        //Базовая часть адреса для подключения к API сервера.
        val ipAddress = "localhost"//getSystemIP()
        api.baseURI = "http://${ipAddress}:8080/"
    }
}
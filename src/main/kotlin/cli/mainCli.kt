package app.majodesk.cli

import app.majodesk.cli.data.LocalActRepositoryImpl
import app.majodesk.domain.model.Act
import app.majodesk.domain.repository.ActRepository


/**УРА! Я САМ НАПИСАЛ ПРОСТОЕ КОНСОЛЬНОЕ МАДЖО В КОТОРОМ Я МОГУ В КОНСОЛИ ДОБАВЛЯТЬ АКТИВНОСТИ И ПРОСМАТРИВАТЬ
 * ИХ ПО УНИКАЛЬНОМУ НОМЕРУ. НО, КАК ОБЫЧНО ПОТЕРЯЛ К ЭТОМУ ИНТЕРЕС, ПОЭТОМУ НАВЕРНОЕ Я ЗАБРОШУ ЭТОТ ФРАГМЕНТ
 * ПРИЛОЖЕНИЯ И ПЕРЕЙДУ К GUI*/

fun mainCli(){



    val localStorage = LocalActRepositoryImpl()

    while (true){
        labelInfo()

        val input = readln()

        if (input == "end" || input == "END") break

        when (input){
            "1" -> addAct(localStorage)
            "2" -> showActById(localStorage)
            else -> println("Неизвестная команда")
        }
    }
}


fun labelInfo(){
    println("""
        Напишите комманду и нажмите enter:
        Список комманд:
        
        1 - Добавить активность
        2 - Вывести активности
        
    """.trimIndent())
}

fun addAct(storage: ActRepository){
    println("ADD COMMAND CODE")
    val act = createActObject()
    storage.createAct(act)
}

fun showActById(storage: ActRepository){
    print("id : ")
    val id = readln().toLong()

    val act = storage.getActById(id)

    println("act = $act")
}


fun createActObject() : Act{
    print("Введите name = ")
    val name = readln()
    val act = Act(0, name)
    return act
}
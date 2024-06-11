package com.utn.cookmate.ui

import com.utn.cookmate.data.Receta

class Server {

    fun login() : Boolean{
        //TODO consultar al server
        return true;
    }

    fun mockRecetasEncontradas(): MutableList<Receta> {
        val list: MutableList<Receta> = mutableListOf()
        list.add(Receta("1","Pollo",mutableListOf<String>("Cortale las alas","Ahora metelo al horno")))
        list.add(Receta("2","Milanesa",mutableListOf<String>("Hacer la milanesa", "Metela al horno")))
        list.add(Receta("14","Ensalada",mutableListOf<String>("Mezcla verduras","Poneles aceite")))
        list.add(Receta("15","Pascualina",mutableListOf<String>("Llena una masa con acelga","ponela al horno")))
        list.add(Receta("16","Empanadas",mutableListOf<String>("Rellena tapas de empanada","cocinalas")))
        return list;
    }

    fun mockRecetas(): MutableList<Receta> {
        val list: MutableList<Receta> = mutableListOf()
        list.add(Receta("1","Pollo",mutableListOf<String>("Cortale las alas","Ahora metelo al horno"),true))
        list.add(Receta("2","Milanesa",mutableListOf<String>("Hacer la milanesa", "Metela al horno"),true))
        list.add(Receta("3","Pizza",mutableListOf<String>("Prepara la masa", "Ponele salsa", "Ponele queso", "Poner en el horno"),true))
        list.add(Receta("4","Matambre",mutableListOf<String>("Comprar el matambre", "Cocinarlo"),true))
        list.add(Receta("5","Huevo frito",mutableListOf<String>("Romper un huevo en la sarten", "Poner la sarten al fuego"),true))
        list.add(Receta("6","Hamburguesa",mutableListOf<String>("Llamar a McDonald", "Pedir un Big Mac"),true))
        list.add(Receta("7","Merluza",mutableListOf<String>("Empanar el pescado","Ponerlo al horno"),true))
        list.add(Receta("8","Picada",mutableListOf<String>("Cortar los fiambres en cubitos","Servirlo en bandeja"),true))
        list.add(Receta("9","Sopa",mutableListOf<String>("Hervir agua","Tirar el sobrecito"),true))
        list.add(Receta("10","Canelones",mutableListOf<String>("Enrollar la comida","Cocinarla"),true))
        list.add(Receta("11","Papas fritas",mutableListOf<String>("Cortalas en juliana","Freilas"),true))
        list.add(Receta("12","Ayuno",mutableListOf<String>("No comas nada"),true))
        list.add(Receta("13","Cerdo",mutableListOf<String>("Carnealo","comelo"),true))
        list.add(Receta("14","Ensalada",mutableListOf<String>("Mezcla verduras","Poneles aceite"),true))
        list.add(Receta("15","Pascualina",mutableListOf<String>("Llena una masa con acelga","ponela al horno"),true))
        list.add(Receta("16","Empanadas",mutableListOf<String>("Rellena tapas de empanada","cocinalas"),true))
        return list;
    }

}
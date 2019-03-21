package com.elaniin.poke.android.Componente

import com.elaniin.poke.android.Api.AppScope
import com.elaniin.poke.android.Interactors.EquipoInteractorImpl
import com.elaniin.poke.android.Modulo.LibModule
import com.elaniin.poke.android.View.*
import dagger.Component

@AppScope
@Component(modules = arrayOf(LibModule::class))
interface InyectComponent {
    fun inject(activity: Splash)
    fun inject(activity: Login)
    fun inject(activity: Home)
    fun inject(activity: SelectPokemon)
    fun inject(activity: Equipos)
    fun inject(activity: EquipoInteractorImpl)

}
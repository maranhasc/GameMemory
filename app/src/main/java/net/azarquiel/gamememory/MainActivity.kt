package net.azarquiel.gamememory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity: AppCompatActivity(), OnClickListener{
    private var isTapando: Boolean = false
    private var inicioTime: Long = 0
    private var finTime: Long = 0
    private lateinit var ivprimera: ImageView
    private lateinit var linearv: LinearLayout
    private lateinit var random: Random
    val vpokemons = Array(809) {i -> i+1}
    var isFirst = true
    var aciertos = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inicioTime = System.currentTimeMillis()
        setContentView(R.layout.activity_main)
        linearv = findViewById<LinearLayout>(R.id.linearv)
        random = Random(System.currentTimeMillis())
        newGame()
    }

    private fun newGame() {
        vpokemons.shuffle(random)
        isFirst = true
        aciertos = 0
        var x=0
        val vjuego = IntArray(30)
        for(j in 0 until 2){
            for (i in 0 until 15){
                val valor = vpokemons[i]
                vjuego[x] = vpokemons[i]
                x++
            }
        }
        vjuego.shuffle(random)
        var c= 0
        for (i in 0 until linearv.childCount){
           var linearh = linearv.getChildAt(i) as LinearLayout
            for (j in 0 until linearh.childCount){
                var ivpokemon = linearh.getChildAt(j) as ImageView
                ivpokemon.isEnabled=true
                ivpokemon.setOnClickListener(this)

                    val foto = "pokemon${vjuego[c]}"
                    ivpokemon.tag = vjuego[c]
                    c++
                    val id = resources.getIdentifier(foto,"drawable" ,packageName)
                    ivpokemon.setBackgroundResource(id)
                    ivpokemon.setImageResource(R.drawable.tapa)
                    //ivpokemon.setImageResource(android.R.color.transparent)
            }
        }

    }
    override fun onClick(v: View?){
        val ivpulsada = v as ImageView
        val pokemonpulsado = ivpulsada.tag as Int

        if(isTapando)return

        ivpulsada.setImageResource(android.R.color.transparent)
        if (isFirst){
            ivprimera = ivpulsada

        }
        else {
            if (ivpulsada == ivprimera) return

            if (pokemonpulsado == ivprimera.tag as Int) {
                aciertos++
                ivpulsada.isEnabled = false
                ivprimera.isEnabled = false

                checkGamesOver()
            } else {
                GlobalScope.launch() {
                    isTapando=true //para que no funcionen los clicks extra que haga
                    SystemClock.sleep(1000)
                    launch(Main) {
                        ivprimera.setImageResource(R.drawable.tapa)
                        ivpulsada.setImageResource(R.drawable.tapa)
                        isTapando=false //para que no funcionen los clicks extra que haga
                    }
                }

            }
        }

        isFirst=!isFirst

    }

    private fun checkGamesOver() {
        if (aciertos == 15){
            finTime = System.currentTimeMillis()
            AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Felicidades.")
                .setMessage("Lo conseguistes en ${(finTime-inicioTime)/1000} segundos")
                .setCancelable(false)
                .setPositiveButton("New Game") { dialog, which ->
                    newGame()
                }
                .setNegativeButton("Fin") { dialog, which ->
                    finish()
                }
                .show()


        }
    }

    fun tostada(msg:String ){
        
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}


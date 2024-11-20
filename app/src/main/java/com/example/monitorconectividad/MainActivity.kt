/*package com.example.monitorconectividad

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Declaracion de las variables globales
    // TextView que mostrara el estado de la conexion
    private lateinit var conexion: TextView
    // Instancia del SERVICIO DEL SISTEMA que permite gestionar la conectividad de red
    private lateinit var connectivityManager: ConnectivityManager

    // Objeto que escucha los cambios del estado de la red (listener)
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        // Se llama cuando una red esta disponible (conectar wifi)
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            updateConnectionStatus()
        }

        // Se llama cuando se pierde una conexion (desconectar wifi)
        override fun onLost(network: Network) {
            super.onLost(network)
            updateConnectionStatus()
        }

        // Se llama cuando se pierden las capacidades de una red (perder wifi)
        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            updateConnectionStatus()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar el TextView
        conexion = findViewById(R.id.textViewConexion)

        // Inicializar el servicio del sistema
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Registrar el NetworkCallback para que el sistema avise sobre cambios en la conexion
        connectivityManager.registerDefaultNetworkCallback(networkCallback)

        // Actualizar estado inicial de conexión
        updateConnectionStatus()
    }

    /**
     * Metodo que comprueba el estado de la conexion para actualizar el textView
     */
    private fun updateConnectionStatus() {
        // Obtiene la red activa, null si no hay conexion
        val network = connectivityManager.activeNetwork
        // Obtiene informacion sobre las capacidades de la red (si es wifi o datos)
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        // Dependiendo de el tipo de conexion tiene un valor u otro, se usa para que almacene
        // un texto para luego actualizar el textView
        val connectionType = when {
            capabilities == null -> "Sin conexión"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Datos móviles"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
            else -> "Otro tipo de conexión"
        }

        runOnUiThread {
            conexion.text = "Tipo de conexión: $connectionType"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Desregistrar el NetworkCallback al destruir la actividad
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}*/

package com.example.monitorconectividad

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var conexion: TextView

    // BroadcastReceiver que escucha cambios en la conectividad del dispositico
    private val connectivityReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateConnectionStatus(context)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        conexion = findViewById(R.id.textViewConexion)

        // Definimos que tipo de evento tiene que escuchar nuestro broadcastReciver con un IntentFilter
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        // registerReciver: activo un receptor de ciertos eventos para que escuche durante la
        // ejecucion. Asocio el tipo de evento que quiero que escuche (filter) y el objeto del
        // SO que se usará para manejar dichos eventos
        registerReceiver(connectivityReceiver, filter)

        // Actualizar estado inicial de conexión
        updateConnectionStatus(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Desregistrar el BroadcastReceiver
        unregisterReceiver(connectivityReceiver)
    }

    /**
     * Metodo que comprueba el estado de la red
     */
    private fun updateConnectionStatus(context: Context) {
        // Objeto del SO que registra la conectividad
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        // Recuperamos la red que se encuentra activa, puede ser null
        val network = connectivityManager.activeNetwork
        // Obtenemos las capacidades de la red activa (si es wifi, datos...)
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        // Compurba con booleanos el tipo de conexion que se esta usando para dar un valor
        // a la varibale que se usara luego en el mensaje
        val connectionType = when {
            capabilities == null -> "Sin conexión"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Datos móviles"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
            else -> "Otro tipo de conexión"
        }

        runOnUiThread {
            conexion.text = "Tipo de conexión: $connectionType"
        }
    }
}

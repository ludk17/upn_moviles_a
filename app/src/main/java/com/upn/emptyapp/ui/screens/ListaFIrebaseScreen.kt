package com.upn.emptyapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.upn.emptyapp.dataStore
import com.upn.emptyapp.models.Estudiante
import com.upn.emptyapp.ui.shared.theme.AppColors
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.checkerframework.checker.units.qual.Current

@Composable
fun ListaFirebaseScreen(navController: NavController) {
    val firestore = Firebase.firestore // instancia firestore
    var auth = Firebase.auth
    var estudiantes by remember { mutableStateOf(emptyList<Estudiante>()) }
    var estaCargando by remember { mutableStateOf(false) };
    val tokenKey = stringPreferencesKey("token")

    val context = LocalContext.current

    val apiToken by context.dataStore.data
        .mapNotNull { it[tokenKey] ?: "" }
        .collectAsState(initial = "")



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppColors.Surface,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("crear_estudiante")
            })
            {
                Text(apiToken)
            }
        }
    ) { paddingValues ->

        LaunchedEffect(Unit) {
            estaCargando = true;
            firestore.collection("estudiantes") // indica que vamos a trabaja con la collecion x
                .get() // obtiene los datos
                .addOnSuccessListener { result -> // este metodo se ejecuta cuando termino de obtener los datos, se usa porque la llamad es asincrona
                    // result.documents.mapNotNull sirve para transformar una lista de objetos en otro lista
                    estudiantes = result.documents.mapNotNull { ds ->
                        ds.toObject(Estudiante::class.java)?.copy(id = ds.id)
                    }

                    estaCargando = false
                }



        }


        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            if (estaCargando) {
                Text("Cargando...")
            } else {
                LazyColumn {
                    items(estudiantes) { item ->
                        Card() {
                            Text(
                                text = "${item.id} -> ${item.nombre}",
                                style = TextStyle(fontSize = 20.sp)
                            )
                        }
                    }
                }


            }

            Button(onClick = {
                auth.signOut()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }) {
                Text("Cerrar Sesion")
            }
        }

    }
}
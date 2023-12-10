package com.example.mybooks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mybooks.ui.theme.MyBooksTheme
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import com.example.mybooks.data.AppDataContainer
import com.example.mybooks.data.User
import com.example.mybooks.data.UsersRepository
import com.example.mybooks.data.BooksRepository

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mybooks.data.Book

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels(){
        MainViewModelFactory(appContainer.usersRepository, appContainer.booksRepository)
    }
    private lateinit var appContainer: AppDataContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize AppDataContainer
        appContainer = AppDataContainer(applicationContext)

        setContent {
            MyBooksTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // Inject the viewModel into composables
                    NavHost(navController, startDestination = "home") {
                        composable("home") {
                            Inicio(navController)
                        }
                        composable("login") {
                            IniciarSesion(navController, appContainer.usersRepository, viewModel)
                        }
                        composable("register") {
                            Registrarse(navController, appContainer.usersRepository, viewModel)
                        }
                        composable("perfil") {
                            Perfil()
                        }
                        composable("main") {
                            Principal(navController)
                        }
                        composable("añadirlibro") {
                            añadirLibro(navController, appContainer.booksRepository, viewModel)
                        }
                        composable("biblioteca") {
                            Biblioteca(viewModel)
                        }
                        composable("futuraslecturas") {
                            FuturasLecturas(viewModel)
                        }
                        composable("leidos") {
                            Leidos(viewModel)
                        }
                        composable("ayuda") {
                            Ayuda()
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun Inicio(navController: NavHostController) {
    val orange = Color(0xFFE77A1C)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = orange)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.8F,
            modifier = Modifier
                .size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de Iniciar sesión
        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón de Registrarse
        Button(
            onClick = { navController.navigate("register") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Registrarse")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Nombre de los autores
        Text(
            text = "Carlos Rodríguez del Toro",
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Antonio Medina Santana",
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        )
    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IniciarSesion(navController: NavHostController, usersRepository: UsersRepository, viewModel: MainViewModel) {
    val orange = Color(0xFFE77A1C)
    val context = LocalContext.current
    val density = LocalDensity.current.density

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = orange)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = 0.8F,
                modifier = Modifier
                    .size(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre de usuario
            var username by remember { mutableStateOf("") }
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )


            Spacer(modifier = Modifier.height(8.dp))

            // Contraseña
            var password by remember { mutableStateOf("") }
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))



            suspend fun loginUser(usern: String, passw: String) {
                val user = usersRepository.getUserStream(usern)
                user.collect { user ->
                    if (user != null) {
                        val username = user.username
                        val password = user.password
                        if (usern == username && passw == password) {
                            navController.navigate("main")
                        }
                    }
                }
            }

            // Botón de inicio de sesión
            Button(
                onClick = {
                    val scope = CoroutineScope(Dispatchers.Main)
                    if (username.isEmpty() || password.isEmpty()) {
                        navController.navigate("login")
                    } else {
                        scope.launch {
                            loginUser(username, password)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Iniciar sesión")
            }
        }
    }
}










@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Registrarse(navController: NavHostController, usersRepository: UsersRepository, viewModel: MainViewModel) {
    val orange = Color(0xFFE77A1C)
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = orange)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = 0.8F,
                modifier = Modifier
                    .size(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Correo electrónico
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Nombre de usuario
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Contraseña
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Confirmar Contraseña
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de registro

            val context = LocalContext.current

            val totalUsers = usersRepository.getTotalUsers()

            suspend fun registerUser() {
                var cont = 0
                totalUsers.collect {
                    if (cont < 1) {
                        cont = 1
                        viewModel.insertUser(it+1, username, password)
                    } else {
                        navController.navigate("main")
                    }
                }
            }

            Button(
                onClick = {
                    val scope = CoroutineScope(Dispatchers.Main) // Use Main dispatcher
                    if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                        navController.navigate("register")
                    } else if (password != confirmPassword) {
                        navController.navigate("register")
                    } else {
                        scope.launch {
                            registerUser()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Registrarse")
            }
        }
    }
}


@Composable
fun Perfil() {
    var usuario by remember { mutableStateOf("prueba1") }
    var contraseña by remember { mutableStateOf("contraseña1") }

    val orange = Color(0xFFE77A1C)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = orange)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.profile),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.8F,
            modifier = Modifier
                .size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Usuario:")
        TextField(
            value = usuario,
            onValueChange = { usuario = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Contraseña:")
        TextField(
            value = contraseña,
            onValueChange = { contraseña = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),

            )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Aquí puedes agregar la lógica para guardar los datos
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Guardar", color = Color.White)
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Principal(navController: NavHostController
              /*userList: List<User>, onUserClick: (Int) -> Unit, modifier: Modifier = Modifier*/
) {
    val navyBlue = Color(0xFF001F3F)
    val orange = Color(0xFFE77A1C)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = orange)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = 0.8F,
                modifier = Modifier
                    .size(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            /*UsersList(
                userList = userList,
                onUserClick = { onUserClick(it.id) },
                modifier = modifier
            )*/


            // Botones
            Button(
                onClick = {
                    // Lógica para Biblioteca
                    navController.navigate("biblioteca")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Biblioteca")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    // Lógica para Futuras Lecturas
                    navController.navigate("futuraslecturas")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Futuras Lecturas")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    // Lógica para Leído
                    navController.navigate("leidos")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Leídos")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    // Lógica para Añadir Libro
                    navController.navigate("añadirlibro")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Añadir Libro")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    // Lógica para Añadir Libro
                    navController.navigate("perfil")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Perfil")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    // Lógica para Añadir Libro
                    navController.navigate("ayuda")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Ayuda")
            }
        }
    }
    //Header()
}

@Composable
fun Biblioteca(viewModel: MainViewModel) {
    val orange = Color(0xFFE77A1C)

    // Obtener la lista de libros desde el ViewModel
    val libros by viewModel.getAllBooks().collectAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = orange)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título de la biblioteca
            Image(
                painter = painterResource(R.drawable.biblio),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = 0.8F,
                modifier = Modifier
                    .size(200.dp)
            )

            // LazyColumn con la lista de libros
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(libros) { libro ->
                    // Aquí puedes personalizar cómo se muestra cada fila del libro en la lista
                    BookItem(
                        libro = libro,
                        onClickDelete = {
                            // Lógica para marcar/desmarcar como favorito
                            // Puedes cambiar la implementación según tu lógica específica
                            viewModel.deleteBook(libro)
                        },
                        onAddToFavorites = {
                            // Lógica para añadir a futuras lecturas
                            viewModel.addToFavorites(libro)
                        },
                        onAddToRead = {
                            // Lógica para añadir a futuras lecturas
                            viewModel.addToRead(libro)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}



@Composable
private fun BookItem(libro: Book, onClickDelete: () -> Unit, onAddToFavorites: () -> Unit, onAddToRead: () -> Unit) {
    // Puedes personalizar cómo se muestra cada fila de libro aquí
    // Por ejemplo, puedes usar un Card, Row, o cualquier otro diseño según tus preferencias
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Título: ${libro.name}")
            Text(text = "Autor: ${libro.author}")
            Text(text = "Descripción: ${libro.description}")

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de eliminación
            Button(
                onClick = onAddToFavorites,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Añadir a Futuras Lecturas", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onAddToRead,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Añadir a Leídos", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de eliminar
            Button(
                onClick = onClickDelete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Eliminar de la Biblioteca", color = Color.White)
            }
        }
    }
}

@Composable
fun FuturasLecturas(viewModel: MainViewModel) {
    val orange = Color(0xFFE77A1C)

    // Obtener la lista de libros desde el ViewModel
    val libros by viewModel.getFavoriteBooks().collectAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = orange)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título de la biblioteca
            Image(
                painter = painterResource(R.drawable.future),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = 0.8F,
                modifier = Modifier
                    .size(200.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(libros) { libro ->
                    // Aquí puedes personalizar cómo se muestra cada fila del libro en la lista
                    BookItemFavorite(
                        libro = libro,
                        onDeleteFromFavorites = {
                            // Lógica para añadir a futuras lecturas
                            viewModel.deleteFromFavorites(libro)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}


@Composable
private fun BookItemFavorite(libro: Book, onDeleteFromFavorites: () -> Unit) {
    // Puedes personalizar cómo se muestra cada fila de libro aquí
    // Por ejemplo, puedes usar un Card, Row, o cualquier otro diseño según tus preferencias
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Título: ${libro.name}")
            Text(text = "Autor: ${libro.author}")
            Text(text = "Descripción: ${libro.description}")
            // Puedes agregar más información del libro según sea necesario

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onDeleteFromFavorites,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Eliminar de Futuras Lecturas", color = Color.White)
            }
        }
    }
}


@Composable
fun Leidos(viewModel: MainViewModel) {
    val orange = Color(0xFFE77A1C)

    // Obtener la lista de libros desde el ViewModel
    val libros by viewModel.getReadBooks().collectAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = orange)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título de la biblioteca
            Image(
                painter = painterResource(R.drawable.completed),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = 0.8F,
                modifier = Modifier
                    .size(200.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(libros) { libro ->
                    // Aquí puedes personalizar cómo se muestra cada fila del libro en la lista
                    BookItemRead(
                        libro = libro,
                        onDeleteFromRead = {
                            // Lógica para añadir a futuras lecturas
                            viewModel.deleteFromRead(libro)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}


@Composable
private fun BookItemRead(libro: Book, onDeleteFromRead: () -> Unit) {
    // Puedes personalizar cómo se muestra cada fila de libro aquí
    // Por ejemplo, puedes usar un Card, Row, o cualquier otro diseño según tus preferencias
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Título: ${libro.name}")
            Text(text = "Autor: ${libro.author}")
            Text(text = "Descripción: ${libro.description}")
            // Puedes agregar más información del libro según sea necesario

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onDeleteFromRead,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Eliminar de Leídos", color = Color.White)
            }
        }
    }
}


@Composable
fun añadirLibro(navController: NavHostController, booksRepository: BooksRepository, viewModel: MainViewModel) {
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    val green = Color(0xFF4CAF50)
    val orange = Color(0xFFE77A1C)

    val cameraLauncher = rememberLauncher()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = orange)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.addbook),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = 0.8F,
                modifier = Modifier
                    .size(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Título
            Text(
                text = "Título:",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            )
            TextField(
                value = titulo,
                onValueChange = { titulo = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Autor
            Text(
                text = "Autor:",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            )
            TextField(
                value = autor,
                onValueChange = { autor = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción
            Text(
                text = "Descripción:",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            )
            TextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de añadir
            Button(
                onClick = {
                    // Verificar que los campos no estén vacíos antes de agregar el libro
                    if (titulo.isNotEmpty() && autor.isNotEmpty() && descripcion.isNotEmpty()) {
                        // Crear un nuevo libro con los datos ingresados
                        val nuevoLibro = Book(
                            username = "nombre_usuario", // Aquí debes proporcionar el nombre de usuario adecuado
                            name = titulo,
                            author = autor,
                            description = descripcion
                        )

                        // Llamar al método para insertar el libro en la base de datos
                        viewModel.insertBook(nuevoLibro)

                        // Navegar a la pantalla principal u otra pantalla después de agregar el libro
                        navController.navigate("main")
                    } else {
                        // Manejar el caso en que algunos campos estén vacíos
                        // Puedes mostrar un mensaje de error o realizar otra acción según tus necesidades
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Añadir a la Biblioteca", color = Color.White)
            }
        }
    }
}

@Composable
fun Ayuda(){
    val orange = Color(0xFFE77A1C)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = orange)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.question),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.8F,
            modifier = Modifier
                .size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))


        Text(
            """
                Biblioteca:
                Visualiza toda la colección de libros que has añadido. Aquí podrás añadir un libro a la sección de 'Futuras Lecturas' o 'Leídos' .

                Futuras Lecturas:
                Añade aquí desde la 'Biblioteca' todos los libros que te interesen y desees leer en un futuro.

                Leídos:
                Añade aquí desde la 'Biblioteca' todos los libros que ya hayas leído.

                Añadir Libros:
                Añade los libros que te interesen a tu 'Biblioteca'. Los campos obligatorios serán el título y el autor. Eso sí, añade una descripción con todos los detalles importantes del libro.

                Perfil:
                Actualiza tu nombre de usuario y/o contraseña para cada vez que inicies sesión
            """.trimIndent(), color = Color.White)

    }
}

@Composable
fun rememberLauncher(): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val context = LocalContext.current

    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            // La foto fue tomada exitosamente
            // Puedes manejar la lógica de la foto aquí si es necesario
        }
    }
}
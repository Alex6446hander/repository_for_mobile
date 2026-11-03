package com.aefimov.playlist_maker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// 1. ЦВЕТА И ТЕМА
private val BluePrimary = Color(0xFF3772E7)
private val BackgroundLight = Color(0xFFFFFFFF)
private val TextPrimary = Color(0xFF1A1B22)
private val TextSecondary = Color(0xFFAEAFB4)
private val SurfaceGray = Color(0xFFE6E8EB)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    background = BackgroundLight,
    surface = BackgroundLight,
    onPrimary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceGray
)

@Composable
fun PlaylistMakerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}

// 2. MAIN ACTIVITY
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaylistMakerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PlaylistMakerApp()
                }
            }
        }
    }
}

// ДАННЫЕ ДЛЯ ЗАГЛУШЕК С КАРТИНКАМИ
data class TrackData(
    val name: String,
    val artist: String,
    val duration: String,
    val coverRes: Int? = null
)

val mockTracks = listOf(
    TrackData(
        "Yesterday (Remastered 2009)",
        "The Beatles",
        "2:55",
        R.drawable.yasterday_remastered_2009_
    ),
    TrackData(
        "Here Comes The Sun (Remastered...",
        "The Beatles",
        "2:55",
        R.drawable.here_comes_the_sun__remastered_
    ),
    TrackData("No Reply", "The Beatles", "5:41", null),
    TrackData("Let It Be", "The Beatles", "3:11", R.drawable.let_it_be),
    TrackData("Girl", "The Beatles", "6:01", R.drawable.let_it_be),
    TrackData("Michelle", "The Beatles", "2:09", R.drawable.let_it_be),
    TrackData("Eleanor Rigby", "The Beatles", "12:11", R.drawable.let_it_be),
    TrackData("Come Together", "The Beatles", "3:57", R.drawable.let_it_be),
    TrackData("Help!", "The Beatles", "2:62", R.drawable.let_it_be),
    TrackData("No Reply", "The Beatles", "2:23", R.drawable.let_it_be)
)

// 3. НАВИГАЦИЯ
@Composable
fun PlaylistMakerApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                onNavigateToSearch = { navController.navigate("search") },
                onNavigateToPlaylists = { navController.navigate("playlists") },
                onNavigateToFavorites = { navController.navigate("favorites") },
                onNavigateToSettings = { navController.navigate("settings") }
            )
        }
        composable("search") {
            SearchScreen(
                onBack = { navController.popBackStack() },
                onTrackClick = { navController.navigate("player") }
            )
        }
        composable("playlists") {
            PlaylistsScreen(
                onBack = { navController.popBackStack() },
                onPlaylistClick = { navController.navigate("playlist_details") },
                onCreatePlaylist = { navController.navigate("create_playlist") }
            )
        }
        composable("favorites") {
            FavoritesScreen(onBack = { navController.popBackStack() })
        }
        composable("settings") {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onAgreementClick = { navController.navigate("agreement") }
            )
        }
        composable("player") {
            PlayerScreen(
                onBack = { navController.popBackStack() },
                onAddToPlaylistClick = { navController.navigate("add_to_playlist") }
            )
        }
        composable("create_playlist") {
            CreatePlaylistScreen(onBack = { navController.popBackStack() })
        }
        composable("playlist_details") {
            PlaylistDetailsScreen(
                onBack = { navController.popBackStack() },
                onDeleteClick = { navController.popBackStack() }
            )
        }
        composable("agreement") {
            AgreementScreen(onBack = { navController.popBackStack() })
        }
        composable("add_to_playlist") {
            AddToPlaylistScreen(onBack = { navController.popBackStack() })
        }
    }
}

// 4. ГЛАВНЫЙ ЭКРАН (Меню)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToPlaylists: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Playlist Maker",
                        fontWeight = FontWeight.Medium,
                        fontSize = 22.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BluePrimary,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = BluePrimary
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                color = BackgroundLight
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    MainMenuItem(
                        text = "Поиск",
                        icon = Icons.Default.Search,
                        onClick = onNavigateToSearch
                    )
                    MainMenuItem(
                        text = "Плейлисты",
                        icon = Icons.Default.LibraryMusic,
                        onClick = onNavigateToPlaylists
                    )
                    MainMenuItem(
                        text = "Избранное",
                        icon = Icons.Default.FavoriteBorder,
                        onClick = onNavigateToFavorites
                    )
                    MainMenuItem(
                        text = "Настройки",
                        icon = Icons.Default.Settings,
                        onClick = onNavigateToSettings
                    )
                }
            }
        }
    }
}

@Composable
fun MainMenuItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextPrimary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(24.dp)
        )
    }
}

// 5. ЭКРАН ПОИСКА
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onBack: () -> Unit, onTrackClick: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Поиск", fontWeight = FontWeight.Medium, fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundLight,
                    titleContentColor = TextPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Поиск", color = TextSecondary, fontSize = 16.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Очистить",
                                tint = TextSecondary
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = SurfaceGray,
                    unfocusedContainerColor = SurfaceGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = BluePrimary
                )
            )

            if (searchQuery.isEmpty()) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HistoryItem(text = "Rammstein")
                    HistoryItem(text = "Queen")
                    HistoryItem(text = "Bruno Mars")
                }
            } else if (searchQuery == "пример") {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.not),
                            contentDescription = null,
                            modifier = Modifier.size(120.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Ничего не нашлось",
                            color = TextPrimary,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else if (searchQuery == "нет интернета") {
                Box(modifier = Modifier.fillMaxSize().padding(start = 32.dp),
                    contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.notwifi),
                            contentDescription = null,
                            modifier = Modifier.size(120.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Проблемы со связью. Загрузка не удалась. Проверьте подключение к интернету.",
                            color = TextPrimary,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(0.8f) // или .padding(horizontal = 24.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
                ) {
                    items(mockTracks.size) { index ->
                        val track = mockTracks[index]
                        TrackItem(
                            trackName = track.name,
                            artistName = track.artist,
                            duration = track.duration,
                            coverRes = track.coverRes,
                            onClick = onTrackClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            color = TextPrimary
        )
    }
}

@Composable
fun TrackItem(
    trackName: String,
    artistName: String,
    duration: String,
    coverRes: Int? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(SurfaceGray)
        ) {
            if (coverRes != null) {
                Image(
                    painter = painterResource(id = coverRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = trackName,
                fontSize = 16.sp,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = artistName,
                    fontSize = 14.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Text(
                    text = " • $duration",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(24.dp)
        )
    }
}

// 6. ЭКРАН ПЛЕЙЛИСТОВ
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistsScreen(onBack: () -> Unit, onPlaylistClick: () -> Unit, onCreatePlaylist: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Плейлисты", fontWeight = FontWeight.Medium, fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundLight,
                    titleContentColor = TextPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreatePlaylist,
                containerColor = Color(0xFFC4C4C4),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Создать",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
        ) {
            item {
                PlaylistItem(
                    title = "Best songs 2021",
                    count = "98 треков",
                    coverRes = R.drawable.best_songs_2021,
                    onClick = onPlaylistClick
                )
                PlaylistItem(
                    title = "Summer Party",
                    count = "157 треков",
                    coverRes = R.drawable.summer_party,
                    onClick = {})
                PlaylistItem(title = "Morning", count = "32 трека", coverRes = null, onClick = {})
            }
        }
    }
}

@Composable
fun PlaylistItem(title: String, count: String, coverRes: Int? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(SurfaceGray),
            contentAlignment = Alignment.Center
        ) {
            if (coverRes != null) {
                Image(
                    painter = painterResource(id = coverRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, fontSize = 16.sp, color = TextPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = count, fontSize = 14.sp, color = TextSecondary)
        }
    }
}

// 7. ЭКРАН ИЗБРАННОГО
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Избранное", fontWeight = FontWeight.Medium, fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundLight,
                    titleContentColor = TextPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
        ) {
            items(mockTracks.size) { index ->
                val track = mockTracks[index]
                TrackItem(
                    trackName = track.name,
                    artistName = track.artist,
                    duration = track.duration,
                    coverRes = track.coverRes,
                    onClick = {}
                )
            }
        }
    }
}

// 8. ЭКРАН НАСТРОЕК
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit, onAgreementClick: () -> Unit) {
    var isDarkTheme by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки", fontWeight = FontWeight.Medium, fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundLight,
                    titleContentColor = TextPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Темная тема",
                    fontSize = 16.sp,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { isDarkTheme = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = BluePrimary,
                        checkedTrackColor = BluePrimary.copy(alpha = 0.5f),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = SurfaceGray,
                        uncheckedBorderColor = Color.Transparent
                    )
                )
            }

            SettingsItem(text = "Поделиться приложением", icon = Icons.Default.Share, onClick = {})
            SettingsItem(
                text = "Написать в поддержку",
                icon = Icons.Default.SupportAgent,
                onClick = {})
            SettingsItem(
                text = "Пользовательское соглашение",
                icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                onClick = onAgreementClick
            )
        }
    }
}

@Composable
fun SettingsItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(24.dp)
        )
    }
}

// 9. ЭКРАН ПЛЕЕРА (Детали трека)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(onBack: () -> Unit, onAddToPlaylistClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundLight,
                    titleContentColor = TextPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceGray)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.yasterday_remastered_2009_),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Yesterday (Remastered 2009)",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "The Beatles",
                fontSize = 14.sp,
                color = TextPrimary,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onAddToPlaylistClick,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(SurfaceGray)
                ) {
                    Icon(painter = painterResource(id = R.drawable.button), contentDescription = "Добавить", tint = Color.White)
                }

                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(SurfaceGray)
                ) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = "В избранное",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Длительность", color = TextSecondary, fontSize = 14.sp)
                Text(text = "5:35", color = TextPrimary, fontSize = 14.sp)
            }
        }
    }
}

// 10. СОЗДАНИЕ ПЛЕЙЛИСТА
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaylistScreen(onBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val isButtonEnabled = title.isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Новый плейлист",
                        fontWeight = FontWeight.Medium,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundLight,
                    titleContentColor = TextPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = "Добавить фото",
                    modifier = Modifier.size(120.dp),
                    tint = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Название*") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BluePrimary,
                    unfocusedBorderColor = TextSecondary,
                    focusedLabelColor = BluePrimary,
                    unfocusedLabelColor = TextSecondary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BluePrimary,
                    unfocusedBorderColor = TextSecondary,
                    focusedLabelColor = BluePrimary,
                    unfocusedLabelColor = TextSecondary
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /* Создать */ },
                enabled = isButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BluePrimary,
                    disabledContainerColor = SurfaceGray,
                    contentColor = Color.White,
                    disabledContentColor = Color.White
                )
            ) {
                Text("Создать", fontSize = 16.sp)
            }
        }
    }
}

// 11. ДЕТАЛИ ПЛЕЙЛИСТА
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailsScreen(onBack: () -> Unit, onDeleteClick: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showDeleteDialog = false }) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = BackgroundLight
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Хотите удалить плейлист «Best songs 2021»?",
                        fontSize = 16.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text("НЕТ", color = BluePrimary, fontWeight = FontWeight.Medium)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = { 
                            showDeleteDialog = false
                            onDeleteClick() 
                        }) {
                            Text("ДА", color = BluePrimary, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }

    if (showMenu) {
        ModalBottomSheet(
            onDismissRequest = { showMenu = false },
            containerColor = BackgroundLight
        ) {
            Column(modifier = Modifier.padding(bottom = 32.dp)) {
                PlaylistItem(title = "Best songs 2021", count = "98 треков", coverRes = R.drawable.best_songs_2021, onClick = {})
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Поделиться",
                    fontSize = 16.sp,
                    color = TextPrimary,
                    modifier = Modifier.fillMaxWidth().clickable { showMenu = false }.padding(horizontal = 16.dp, vertical = 16.dp)
                )
                Text(
                    text = "Редактировать информацию",
                    fontSize = 16.sp,
                    color = TextPrimary,
                    modifier = Modifier.fillMaxWidth().clickable { showMenu = false }.padding(horizontal = 16.dp, vertical = 16.dp)
                )
                Text(
                    text = "Удалить плейлист",
                    fontSize = 16.sp,
                    color = TextPrimary,
                    modifier = Modifier.fillMaxWidth().clickable { 
                        showMenu = false 
                        showDeleteDialog = true 
                    }.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundLight,
                    titleContentColor = TextPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.35f) // Делаем картинку примерно на треть/половину экрана в высоту
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceGray)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.best_songs_2021),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    "Best songs 2021",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("2022", fontSize = 16.sp, color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Text("300 минут • 98 треков", fontSize = 16.sp, color = TextPrimary)

                Spacer(modifier = Modifier.height(16.dp))

                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Меню", tint = TextPrimary)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(4) { index ->
                    val track = mockTracks[index]
                    TrackItem(
                        trackName = track.name,
                        artistName = track.artist,
                        duration = track.duration,
                        coverRes = track.coverRes,
                        onClick = {}
                    )
                }
            }
        }
    }
}

// 12. ПОЛЬЗОВАТЕЛЬСКОЕ СОГЛАШЕНИЕ
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgreementScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Соглашение", fontWeight = FontWeight.Medium, fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundLight,
                    titleContentColor = TextPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Оферта на оказание \n" +
                        "образовательных услуг\n" +
                        " дополнительного \n" +
                        "образования \n" +
                        "Яндекс.Практикум \n" +
                        "для физических лиц\n",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Данный документ является действующим, \n" +
                        "если распространен по адресу: htpps://yandex.ru/legal/practicum_offer \n" +
                        "Российская Федерация, город Москва\n",
                fontSize = 16.sp,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "1. ТЕРМИНЫ\n",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Понятия, используемые в Оферте, означают \n" +
                        "следующее: \n" +
                        "Авторизованные адреса-адреса \n" +
                        "электронной почты каждой Стороны.\n" +
                        "Авторизованным адресом Исполнителя \n" +
                        "является адрес электронной почты, указанный \n" +
                        "в разделе 11 Оферты. Авторизованным \n" +
                        "адресом Студента является адрес \n" +
                        "электронной почты, указанный Студентом в \n" +
                        "Личном кабинете.\n" +
                        "\n" +
                        "Вводный курс - начальный Курс обучения по \n" +
                        "представленным на Сервисе Программам \n" +
                        "обучения в рамках выбранной Студентом \n" +
                        "Профессии или Курсу,рассчитанный на \n" +
                        "определенное количество часов \n" +
                        "самостоятельного обучения, который \n" +
                        "предоставляется Студенту единожды при \n" +
                        "регистрации на Сервисе на безвозмездной \n" +
                        "основе В процессе обучения... ",
                fontSize = 16.sp,
                color = TextPrimary
            )
        }
    }
}

// 13. ДОБАВИТЬ В ПЛЕЙЛИСТ
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToPlaylistScreen(onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Рисуем экран плеера на заднем фоне (как подложку)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp + 56.dp)) // Отступ под TopBar плеера

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceGray)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.yasterday_remastered_2009_),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // 2. Затемнение всего экрана (Scrim)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(onClick = onBack) // Клик по затемнению закроет шторку
        )

        // 3. Стрелка "Назад" поверх затемнения (как на макете)
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(top = 16.dp, start = 8.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = Color.Black)
        }

        // 4. Сама белая шторка снизу
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .align(Alignment.BottomCenter), // Прижимаем к низу
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            color = BackgroundLight,
            shadowElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(top = 8.dp)) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(SurfaceGray)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Добавить в плейлист",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(24.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        PlaylistItem(title = "Best songs 2021", count = "98 треков", coverRes = R.drawable.best_songs_2021, onClick = {})
                        PlaylistItem(title = "Summer Party", count = "157 треков", coverRes = R.drawable.summer_party, onClick = {})
                    }
                }
            }
        }
    }
}


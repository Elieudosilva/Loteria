package co.tiagoaguiar.loteriacomposedev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.tiagoaguiar.loteriacomposedev.ui.component.LoItemType
import co.tiagoaguiar.loteriacomposedev.ui.component.LoNumberTextField
import co.tiagoaguiar.loteriacomposedev.ui.theme.Green
import co.tiagoaguiar.loteriacomposedev.ui.theme.LoteriaTheme
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoteriaTheme {
                val navController = rememberNavController()
                
                NavHost(
                    navController = navController,
                    startDestination = AppRouter.HOME.route
                ){
                    composable(AppRouter.HOME.route){
                        HomeScreen{
                           navController.navigate(AppRouter.FORM.route)
                        }
                    }

                    composable(AppRouter.FORM.route){
                        FormScreen()
                    }
                }
            }
        }
    }
}
enum class AppRouter(val route: String) {
    HOME("home"),
    FORM("form")
}

@Composable
fun HomeScreen(onClick: () -> Unit){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){
        LotteryaItem("Mega Sena", onClick = onClick )
    }
}

@Composable
fun LotteryaItem(name: String, onClick: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .wrapContentSize()
            .clickable{
                onClick()
            }
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
        ){
         LoItemType(
             name = name,
             color = Color.White,
             bgColor = Green
         )
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FormScreen(){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){
        val errorBest = stringResource(id = R.string.error_bets)
        val errorNumbers = stringResource(id = R.string.error_numbers)

        var qtdNumbers by remember { mutableStateOf("") }
        var qtdBets by remember { mutableStateOf("")}
        var result by remember { mutableStateOf("")}
        var showAlertDialog by remember { mutableStateOf(false) }

        val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
        val scope = rememberCoroutineScope()
        val keyboardController = LocalSoftwareKeyboardController.current
        val scrollState = rememberScrollState()

        Column (
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(scrollState)
        ) {
       LoItemType(name = "Mega Sena")

            Text(
                text = stringResource(id = R.string.announcement),
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(8.dp)
            )

            LoNumberTextField(
                value = qtdNumbers,
                label = R.string.mega_rule,
                placeholder = R.string.quantity,
                ) {
                if (it.length < 3) {
                    qtdNumbers = validateInput(it)
                }
            }

            LoNumberTextField(
                value = qtdBets,
                label = R.string.bets,
                placeholder = R.string.bets_quantity,
                imeAction = ImeAction.Done
            ) {
                if (it.length < 3) {
                    qtdBets = validateInput(it)
                }
            }

            OutlinedButton(
                enabled = qtdNumbers.isNotEmpty() && qtdBets.isNotEmpty(),
                onClick = {
                    val best = qtdBets.toInt()
                    val numbers = qtdNumbers.toInt()
                    if (best < 1 || best > 10) {
                        scope.launch {
                            snackbarHostState.showSnackbar(errorBest)
                        }
                    } else if (numbers< 6 || numbers > 15) {
                        scope.launch {
                            snackbarHostState.showSnackbar(errorNumbers)
                        }
                    } else {
                            result = ""
                            for (i in  1.. best) {
                                result += "[$i]"
                                result += numberGenerator(numbers)
                                result += "\n\n"
                            }
                        showAlertDialog = true
                    }
                    keyboardController?.hide()
                }) {
                Text(stringResource(id = R.string.bets_generate))
            }

            Text(text = result)
        }

        Box {
            SnackbarHost(
                modifier = Modifier.align(Alignment.BottomCenter),
                hostState = snackbarHostState
            )
        }

        if (showAlertDialog) {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {
                    TextButton(onClick = { showAlertDialog = false}) {
                        Text(text = stringResource(id = android.R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAlertDialog = false}) {
                        Text(text = stringResource(id = android.R.string.cancel))
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                text = {
                    Text(text = stringResource(id = R.string.good_luck))
                }
            )
        }
    }
}

private fun numberGenerator(qtd: Int): String {
    val numbers = mutableSetOf<Int>()

    while (true) {
        val n = Random.nextInt(60)
        numbers.add(n + 1)
        if (numbers.size == qtd ) {
            break
        }
    }

    return  numbers.joinToString(" - ")
}

private fun validateInput(input: String): String {
    val filteredChar = input.filter { char ->
        char in "0123456789"
    }
    return filteredChar
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LoteriaTheme {
        HomeScreen { }
    }
}

@Preview(showBackground = true)
@Composable
fun FormPreview() {
    LoteriaTheme {
        FormScreen()
    }
}
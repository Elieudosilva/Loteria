package co.tiagoaguiar.loteriacomposedev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.tiagoaguiar.loteriacomposedev.ui.theme.Green
import co.tiagoaguiar.loteriacomposedev.ui.theme.LoteriaTheme

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
                .background(Green)
        ){
            Image(
                painter = painterResource(id = R.drawable.trevo),
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp)
                    .padding(10.dp)
            )
            Text(
                text = name,
                color = Color.White,
                modifier = Modifier
                    .align (Alignment.CenterHorizontally)
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){
        var qtdNumbers by remember { mutableStateOf("") }
        var qtdBets by remember { mutableStateOf("")}

        Column (
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.trevo),
                contentDescription = stringResource(id = R.string.trevo),
                modifier = Modifier
                    .size(100.dp)
                    .padding(10.dp)
            )

            Text(
                text = "Mega Sena",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = stringResource(id = R.string.announcement),
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(8.dp)
            )

            OutlinedTextField(
                value = qtdNumbers,
                maxLines = 1,
                label = {
                    Text(stringResource(id = R.string.mega_rule))
                },
                placeholder = {
                    Text(stringResource(id = R.string.quantity))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                onValueChange = {
                    if (it.length < 3) {
                        qtdNumbers = validateInput(it)
                    }
                }
            )

            OutlinedTextField(
                value = qtdBets,
                maxLines = 1,
                label = {
                    Text(stringResource(id = R.string.bets))
                },
                placeholder = {
                    Text(stringResource(id = R.string.bets_quantity))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                onValueChange = {
                    if (it.length < 3) {
                        qtdBets = validateInput(it)
                    }
                }
            )

            OutlinedButton( onClick = { }) {
                Text(stringResource(id = R.string.bets_generate))
            }
        }
    }
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
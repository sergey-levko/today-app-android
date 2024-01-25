package by.liauko.siarhei.app.today.ui.screen

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import by.liauko.siarhei.app.today.R
import by.liauko.siarhei.app.today.activity.SettingsActivity
import by.liauko.siarhei.app.today.ui.theme.AppTheme
import by.liauko.siarhei.app.today.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = viewModel.uiState.currentDate
                )
            })
        },
        floatingActionButton = {
            SettingsActionButton()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LinearProgressIndicator(
                progress = viewModel.uiState.yearProgress,
                modifier = Modifier.fillMaxWidth()

            )
            Column(
                modifier = Modifier.padding(12.dp, 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CalendarDataOutlineCard(
                        title = stringResource(id = R.string.day_of_the_year_title),
                        data = viewModel.uiState.currentDayNumber.toString(),
                        Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(id = R.dimen.card_height))
                            .padding(dimensionResource(id = R.dimen.card_padding))
                            .weight(1f)
                    )
                    CalendarDataOutlineCard(
                        title = stringResource(id = R.string.days_left_title),
                        data = viewModel.uiState.daysLeftNumber.toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(id = R.dimen.card_height))
                            .padding(dimensionResource(id = R.dimen.card_padding))
                            .weight(1f)
                    )
                }
                CalendarDataOutlineCard(
                    title = stringResource(id = R.string.week_number_title),
                    data = viewModel.uiState.weekNumber.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.card_height))
                        .padding(dimensionResource(id = R.dimen.card_padding))
                )
            }
        }
    }
}

@Composable
fun CalendarDataOutlineCard(title: String, data: String, modifier: Modifier) {
    Card(
        modifier = modifier
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (cardTitle, cardValue) = createRefs()

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .constrainAs(cardTitle) {
                        top.linkTo(parent.top, margin = 8.dp)
                    }
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = data,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.constrainAs(cardValue) {
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                }
            )
        }
    }
}

@Composable
fun SettingsActionButton() {
    val context = LocalContext.current

    ExtendedFloatingActionButton(
        onClick = {
            context.startActivity(
                Intent(context, SettingsActivity::class.java)
            )
        },
        icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = stringResource(id = R.string.settings_description)) },
        text = { Text(text = stringResource(id = R.string.settings_description)) }
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    AppTheme {
        HomeScreen()
    }
}

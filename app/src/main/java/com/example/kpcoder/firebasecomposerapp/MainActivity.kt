package com.example.kpcoder.firebasecomposerapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kpcoder.firebasecomposerapp.MainActivity.Companion.TAG
import com.example.kpcoder.firebasecomposerapp.ui.theme.FirebaseComposerAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    companion object {
        val TAG : String = MainActivity::class.java.simpleName
    }
    private val auth by lazy {
        Firebase.auth
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseComposerAppTheme {
                // A surface container using the 'background' color from the theme
                LoginScreen(auth)

                }
            }
        }
    }

@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginScreen(auth : FirebaseAuth) {

    val focusManager = LocalFocusManager.current

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    val isEmailValid by derivedStateOf {
        Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    val isPasswordValid by derivedStateOf {
        password.length > 7
    }

    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .background(color = Color.LightGray)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        // Some Text Field and Images Logos
        Text(text = "Welcome back...",
        fontFamily = FontFamily.Companion.SansSerif,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            fontSize = 32.sp,
            modifier = Modifier.padding(top = 16.dp)
        )

        Image(painter = painterResource(id = R.drawable.ic_kfc_logo),
            contentDescription = "KFC Loge",
            modifier = Modifier.size(150.dp)
        )

        Text(text = "...to the house of the fried chicken",
            fontFamily = FontFamily.Companion.SansSerif,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // Login Views
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(all = 10.dp)
            ) {
                // Email Text View
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = "Email Address")},
                    placeholder = { Text(text = "abc@domain.com")},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    // KeyBoard Function For After Done Click What having for email
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    // Keyboard actions for Email
                    keyboardActions = KeyboardActions(
                        onNext = {focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    isError = !isEmailValid,
                    // TrailingIcon  using this dependancy implementation "androidx.compose.material:material-icons-extended:$compose_ui_version"
                    trailingIcon = {
                        if (email.isNotBlank()) {
                            IconButton(onClick = { }) {
                                Icon(imageVector = Icons.Filled.Clear,
                                    contentDescription = "Clear email")

                            }
                        }
                    }
                )
                // This is Password EditText
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = "Password")},
                    //placeholder = { Text(text = " ")},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    // Keyboard option for Password
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    // keyboard action for Password
                    keyboardActions = KeyboardActions(
                        onDone = {focusManager.clearFocus() }
                    ),
                    isError = !isPasswordValid,
                    // TrailingIcon  using this dependancy implementation "androidx.compose.material:material-icons-extended:$compose_ui_version"
                    trailingIcon = {
                        // this for password visibility toggles
                        //if (password.isNotBlank()) {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(imageVector = if(isPasswordVisible) Icons.Default.Visibility
                                                    else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password visibility")

                            }
                       // }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()


                )
                // This Button for Login
                Button(
                    onClick = {
                              auth.signInWithEmailAndPassword(email, password)
                                  .addOnCompleteListener {
                                      if (it.isSuccessful){
                                          // we are using to Log.d first we are declare to companion object
                                          Log.d(TAG, "The user has successfully logged in")
                                      }else {
                                          Log.d(TAG, "The user has failed to log in", it.exception)
                                      }
                                  }
                              },
                    // this is use for the all text field is valid than login button enabled
                    enabled = isEmailValid && isPasswordValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                    // after enabled than login button color Red
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Text(
                        text = "Log in",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 16.sp
                    )

                }


            }


        }

        // This Row for Forgot Button using TextButton
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    color = Color.Black,
                    fontStyle = FontStyle.Italic,
                    text = "Forgotten Password?",
                    modifier = Modifier.padding(end = 8.dp)
                )
                
            }
        }


        // This Button for Register Intent or activity
        Button(
            onClick = {

            },
            // this is always true there is no condition
            enabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
            Text(
                text = "Register",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 16.sp
            )
            
        }



    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FirebaseComposerAppTheme {
        LoginScreen(Firebase.auth)
        
    }
    
}
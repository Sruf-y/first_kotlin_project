package com.example.myapplication2

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text

var nume:String="";

class BlankFragment : Fragment(R.layout.fragment_blank) {

    lateinit var greetingTextView:TextView
    lateinit var inputField : EditText
    lateinit var resetBut: Button
    lateinit var sf: SharedPreferences;
    lateinit var editor :SharedPreferences.Editor;


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val localContext= requireContext()
        greetingTextView = requireView().findViewById(R.id.textBox);
        inputField = requireView().findViewById(R.id.insertTextBox);
        resetBut = requireView().findViewById(R.id.button);

        sf=localContext.getSharedPreferences("My SF",Context.MODE_PRIVATE);
        editor=sf.edit();







        inputField.setOnEditorActionListener{ v,actionId,event ->
            // daca s-a apasat Done pe tastatura
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                val enteredName = inputField.text.toString();

                if (inputField.text.toString() != "")
                {

                    val message = "Welcome $enteredName";
                    greetingTextView.text = message;
                    nume=enteredName;
                    inputField.text.clear();

                }
                else
                {
                    if (nume == "")
                    {
                        customToast(requireView().findViewById(R.id.main),localContext,"It needs a name.")
                    }
                }



                true
            }
            else
            {
                false
            }
        }






        resetBut.setOnClickListener{
            nume="";
            greetingTextView.text = "Enter a name";

        }











    }





    override fun onPause() {
        super.onPause()

        editor.apply(){
            putString("SF_name",nume);
            commit();
        }


    }
    override fun onResume() {
        super.onResume()






        //can't overwrite transitions sadly, needs to be overwritten in onPause of the current activity

        greetingTextView = requireView().findViewById(R.id.textBox);
        nume= sf.getString("SF_name","").toString();
        // NUME_VARIABILA= sf.getInt("SF_age",0).toInt();
        if(nume!="") {
            greetingTextView.text = "Welcome $nume";
        }
        else
        {
            greetingTextView.text = "Enter a name";
        }

    }

    override fun onStop() {
        super.onStop()
    }


}



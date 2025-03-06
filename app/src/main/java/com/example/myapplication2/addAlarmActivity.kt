package com.example.myapplication2


import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.icu.util.Calendar
import android.media.Image
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CalendarView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.NumberPicker
import android.widget.ScrollView
import android.widget.Switch
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.key
import androidx.compose.ui.graphics.findFirstRoot
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication2.Utils.Companion.dP
import com.google.android.material.checkbox.MaterialCheckBox
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.min
import kotlin.time.Duration.Companion.milliseconds

var newAllarm:alarmViewModel = alarmViewModel(SoundTime = LocalDateTime.now())

class addAlarmActivity : AppCompatActivity() {
    lateinit var hourPicker:NumberPicker
    lateinit var minutePicker:NumberPicker
    lateinit var amPicker:NumberPicker
    lateinit var Luni:MaterialCheckBox
    lateinit var Marti:MaterialCheckBox
    lateinit var Miercuri:MaterialCheckBox
    lateinit var Joi:MaterialCheckBox
    lateinit var Vineri:MaterialCheckBox
    lateinit var Sambata:MaterialCheckBox
    lateinit var Duminica:MaterialCheckBox

    lateinit var calendar_background:ConstraintLayout
    lateinit var calendar: CalendarView
    lateinit var calendar_click: ImageView
    lateinit var is_delayed: Switch

    lateinit var saveButton:Button
    lateinit var cancelButton:Button
    lateinit var textedit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_add_alarm)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }


        val wherebuttonsarea = findViewById<LinearLayout>(R.id.whereButtonsAre)
        cancelButton=findViewById(R.id.button3)
        saveButton=findViewById(R.id.button5)
        hourPicker=findViewById(R.id.numberPicker)
        minutePicker=findViewById(R.id.numberPicker2)
        amPicker=findViewById(R.id.numberPicker3)
        Luni=findViewById(R.id.lunea)
        Marti=findViewById(R.id.marti)
        Miercuri=findViewById(R.id.miercuri)
        Joi=findViewById(R.id.joi)
        Vineri=findViewById(R.id.vineri)
        Sambata=findViewById(R.id.sambata)
        Duminica=findViewById(R.id.duminica)
        textedit= findViewById(R.id.EditAlarmTitle)
        calendar=findViewById(R.id.calendarView)
        calendar_background=findViewById(R.id.calend_back)
        calendar_click=findViewById(R.id.calendarClick)
        is_delayed=findViewById(R.id.is_timed_alarm)

//        val auxbutton:Button = findViewById(R.id.newbuttondeletelater)
//        auxbutton.setOnClickListener{
//            val intent= Intent(this,AlarmActivity::class.java)
//            startActivity(intent)
//        }
        var selected_a_new_day=-1
        newAllarm.type[11]=-1

        newAllarm.SoundTime=LocalDateTime.now()



        hourPicker.minValue=0
        hourPicker.maxValue=11
        val displayedValues1 = Array(12) { i -> if (i == 0) "12" else i.toString() }
        hourPicker.displayedValues=displayedValues1
        minutePicker.minValue=0
        minutePicker.maxValue=59
        val displayedValues = Array(60) { i -> String.format("%02d", i) }
        minutePicker.displayedValues = displayedValues
        amPicker.minValue=0;
        amPicker.maxValue=1;
        amPicker.displayedValues= arrayOf("AM","PM");


        //setari valori initiale
        if(editingAlarm==-1) //not editing an allarm so it's making a new one
        {
            hourPicker.value=6
            minutePicker.value=0
            amPicker.value=0
            is_delayed.isChecked=false
            is_delayed.isEnabled=false


        }
        else {
            // daca editez o alarma care exista deja, i preiau valorile

            textedit.setText(newAllarm.properties[0])

            when(newAllarm.type[0]%10){
                1->{
                    //need to make it so some text displays when the allarm will sound, otherwise if nothing was selected in calendar, next time the date comes

                }
                2->{

                    Luni.isChecked = newAllarm.type[1].toBool()
                    Marti.isChecked = newAllarm.type[2].toBool()
                    Miercuri.isChecked = newAllarm.type[3].toBool()
                    Joi.isChecked = newAllarm.type[4].toBool()
                    Vineri.isChecked = newAllarm.type[5].toBool()
                    Sambata.isChecked = newAllarm.type[6].toBool()
                    Duminica.isChecked = newAllarm.type[7].toBool()



                }
                3->{
                    Luni.isChecked=true
                    Marti.isChecked=true
                    Miercuri.isChecked=true
                    Joi.isChecked=true
                    Vineri.isChecked=true
                    Sambata.isChecked=true
                    Duminica.isChecked=true


                }

            }

            // load the delay if there is any
            if(newAllarm.type[0]>10){
                is_delayed.isEnabled=true
                is_delayed.isChecked=true

                calendar.minDate=Calendar.getInstance().timeInMillis
                val cal_aux=Calendar.getInstance()
                cal_aux.set(newAllarm.SoundTime.year,newAllarm.SoundTime.monthValue-1, newAllarm.SoundTime.dayOfMonth)
                calendar.date = cal_aux.timeInMillis


            }


            hourPicker.value = newAllarm.ora
            minutePicker.value= newAllarm.minute
            amPicker.value= if(newAllarm.aM=="AM") 0 else 1
        }

        checkZileleSaptamanii()
        newAllarm.SoundTime= assign_time(newAllarm,is_delayed.isChecked,true)

        cancelButton.setOnClickListener{
            newAllarm.type[11]=-1
            checkZileleSaptamanii()
            newAllarm.SoundTime=assign_time(newAllarm,is_delayed.isChecked,true)
            finish()
        }

        saveButton.setOnClickListener{

            var checknumber=0
            newAllarm.type[11]=1
            newAllarm.ora= hourPicker.value
            newAllarm.minute= minutePicker.value
            if(amPicker.value==0)
                newAllarm.aM="AM"
            else
                newAllarm.aM="PM"

            if(Luni.isChecked){checknumber++}
            if(Marti.isChecked){checknumber++}
            if(Miercuri.isChecked){checknumber++}
            if(Joi.isChecked){checknumber++}
            if(Vineri.isChecked){checknumber++}
            if(Sambata.isChecked){checknumber++}
            if(Duminica.isChecked){checknumber++}

            if(checknumber==7) //everyday
            {
                newAllarm.type[0]=3
            }
            else if(checknumber==0) // one time only
            {
                newAllarm.type[0]=1



            }
            else // chosen days
            {
                newAllarm.type[0] = 2

                checkZileleSaptamanii()
            }

            // BOOLEAN TO INT VECTOR FOR DAYS IS WRITTEN ABOVE!!! USE IF NEEDED IN THE FUTURE, DON'T BE STUPID


            // save delay
            if(is_delayed.isChecked)
            {
                newAllarm.type[0]+=10

            }


            if(textedit.text.toString().isNotBlank())
                newAllarm.properties[0]=textedit.text.toString()
            else
                newAllarm.properties[0]=""
            checkZileleSaptamanii()
            newAllarm.SoundTime=assign_time(newAllarm,is_delayed.isChecked,true)


            finish()
        }

        calendar_click.setOnClickListener{

            is_delayed.isEnabled=true
            is_delayed.isChecked=true

            selected_a_new_day=0
            val tomorrow= Calendar.getInstance();




            calendar.minDate=tomorrow.timeInMillis

            if(newAllarm.type[0]>10){



                val cal_aux=Calendar.getInstance()
                calendar.minDate=Calendar.getInstance().timeInMillis
                cal_aux.set(newAllarm.SoundTime.year,newAllarm.SoundTime.month.value-1, newAllarm.SoundTime.dayOfMonth)
                calendar.date = cal_aux.timeInMillis

            }

            calendar_background.animate()
                .translationY(0.dP.toFloat())
                .setDuration(300)
                .start()


        }


        calendar_background.setOnClickListener {

            is_delayed.isEnabled=true
            is_delayed.isChecked=true

            calendar_background.animate()
                .translationY(700.dP.toFloat()) // Animate to this position
                .setDuration(300) // Set duration to 2 seconds
                .start()


        }

        calendar.setOnDateChangeListener{_,year,month,dayofmonth->
            newAllarm.type[8]=dayofmonth
            newAllarm.type[9]=month+1
            newAllarm.type[10]=year

            is_delayed.isEnabled=true
            is_delayed.isChecked=true
            selected_a_new_day=1



        }

        is_delayed.setOnClickListener{
            is_delayed.isEnabled=false
            selected_a_new_day=-1

            val today=LocalDateTime.now()
                newAllarm.type[8] = today.dayOfMonth
                newAllarm.type[9] = today.monthValue
                newAllarm.type[10] = today.year

            newAllarm.ora= hourPicker.value
            newAllarm.minute= minutePicker.value
            if(amPicker.value==0)
                newAllarm.aM="AM"
            else
                newAllarm.aM="PM"

            checkZileleSaptamanii()
            assign_time(newAllarm,is_delayed.isChecked,false)

        }



        onBackPressedDispatcher.addCallback() {
            // Handle the back press



            if (calendar_background.translationY == 0.dP.toFloat()) {

                val tomorrow = LocalDateTime.now()

                if(is_delayed.isChecked)
                {
                    selected_a_new_day=1
                }
                if (selected_a_new_day == 0) {
                    newAllarm.type[8] = tomorrow.dayOfMonth
                    newAllarm.type[9] = tomorrow.monthValue
                    newAllarm.type[10] = tomorrow.year
                }

                is_delayed.isEnabled = true
                is_delayed.isChecked = true

                calendar_background.animate()
                    .translationY(700.dP.toFloat()) // Animate to this position
                    .setDuration(300) // Set duration to 2 seconds
                    .start()
            } else {
                checkZileleSaptamanii()
                newAllarm.SoundTime=assign_time(newAllarm,is_delayed.isChecked,true)
                // Allow the system to handle the back press
                finish()
            }
        }





    }



    override fun onPause() {
        super.onPause()
        //overridePendingTransition(R.anim.to_up,R.anim.from_bellow)

    }

    fun checkZileleSaptamanii():Int {
        Luni=findViewById(R.id.lunea)
        Marti=findViewById(R.id.marti)
        Miercuri=findViewById(R.id.miercuri)
        Joi=findViewById(R.id.joi)
        Vineri=findViewById(R.id.vineri)
        Sambata=findViewById(R.id.sambata)
        Duminica=findViewById(R.id.duminica)

        newAllarm.type[1] = Luni.isChecked.toInt()
        newAllarm.type[2] = Marti.isChecked.toInt()
        newAllarm.type[3] = Miercuri.isChecked.toInt()
        newAllarm.type[4] = Joi.isChecked.toInt()
        newAllarm.type[5] = Vineri.isChecked.toInt()
        newAllarm.type[6] = Sambata.isChecked.toInt()
        newAllarm.type[7] = Duminica.isChecked.toInt()

        var checknumber:Int=0

        if(Luni.isChecked){checknumber++}
        if(Marti.isChecked){checknumber++}
        if(Miercuri.isChecked){checknumber++}
        if(Joi.isChecked){checknumber++}
        if(Vineri.isChecked){checknumber++}
        if(Sambata.isChecked){checknumber++}
        if(Duminica.isChecked){checknumber++}

        when(checknumber){
            0-> newAllarm.type[0]=1
            7-> newAllarm.type[0]=3
            else->newAllarm.type[0]=2
        }

        if(is_delayed.isChecked)
            newAllarm.type[0]+=10

        return checknumber
    }



}





fun assign_time(data: alarmViewModel, is_delayed: Boolean, afisare_diferenta: Boolean): LocalDateTime {
    val today = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS) // Remove milliseconds


    var selected_date:LocalDateTime = LocalDateTime.of(
        data.type[10], // Year
        data.type[9], // Month
        data.type[8], // Day
        data.ora, // Hour
        data.minute, // Minute
        0 // Seconds
    ).truncatedTo(ChronoUnit.SECONDS)

    if(ora_am_or_pm(data)!=selected_date.hour)
        selected_date=selected_date.plusHours(12)

    if(is_delayed)
    {
        while(selected_date.isBefore(LocalDateTime.now()))
        {
            selected_date=selected_date.plusDays(1)
        }
    }

    if(data.type[0]%10==2){

        var counter=0;
        var i=selected_date.dayOfWeek.value
           while(counter<7)
            {

                if(data.type[i]==1)
                    break;

                selected_date=selected_date.plusDays(1)
                if(i<7)
                    i++
                else
                    i=1
            }
        }
        else{
            if(selected_date.isBefore(LocalDateTime.now()))
                selected_date.plusDays(1)
        }

    while(selected_date.isBefore(LocalDateTime.now()))
        selected_date=selected_date.plusDays(1)
//if(afisare_diferenta)
    //Log.i("MYTAG", "Retry ${LocalTime.now()}    ${data.type.slice(1..7)}\nDifference:     Mins: $minutesDifference, Hours: $hoursDifference, Days: $daysDifference, Day ${selected_date.dayOfMonth}, Month ${selected_date.monthValue}")

    return selected_date
}


fun ora_am_or_pm(data: alarmViewModel): Int {


    if(data.aM=="AM")
        return data.ora
    else
        return data.ora+12
}

fun swipeNumberPicker(button:View){



        // Get the current time for event timestamps
        val downTime = SystemClock.uptimeMillis()

        // Simulate touch down (ACTION_DOWN)
        val downEvent = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, 0f, 0f, 0)
        button.dispatchTouchEvent(downEvent) // Dispatch touch down event

        // Simulate touch up (ACTION_UP) after a short delay
        val upTime = SystemClock.uptimeMillis()
        val upEvent = MotionEvent.obtain(downTime, upTime, MotionEvent.ACTION_UP, 0f, 0f, 0)
        button.dispatchTouchEvent(upEvent) // Dispatch touch up event


}

fun Int.toBool():Boolean{
    return if(this!=0) true else false
}

fun Boolean.toInt():Int{
    return if(this) 1 else 0
}
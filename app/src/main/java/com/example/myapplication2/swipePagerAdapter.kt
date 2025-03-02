package com.example.myapplication2


import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter

class swipePagerAdapter(fragment:Fragment): FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int {
        return 3;
    }


    override fun createFragment(position: Int): Fragment {



        return when(position){
            0-> alarmeFragment() // MusicFragment();
            1-> clock_calc();
            2->timer() //LyricsFragment();
            else->alarmeFragment();
        }


    }

class alarmeFragment:alarme() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }



}





}
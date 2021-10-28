package com.lab2.tabata.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.lab2.tabata.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : Fragment(), CoroutineScope {
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    protected fun replaceAndAddToBackStackFragment(fragment: Fragment){
        requireActivity().supportFragmentManager.beginTransaction().apply {

            replace(R.id.frame_layout, fragment).addToBackStack(fragment.javaClass.simpleName)
            commit()
        }
    }

    protected fun replaceFragment(fragment: Fragment){
        requireActivity().supportFragmentManager.beginTransaction().apply {

            replace(R.id.frame_layout, fragment, fragment.javaClass.simpleName)
            commit()
        }
    }
}
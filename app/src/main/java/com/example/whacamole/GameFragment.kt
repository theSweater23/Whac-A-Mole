package com.example.whacamole

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        val timer = view.findViewById<TextView>(R.id.timer)
        val scoreText = view.findViewById<TextView>(R.id.scroe)
        GameData.score = 0
        val holes = view.findViewById<ConstraintLayout>(R.id.holes_container)
        holes.children.forEach { v ->
            (v as FrameLayout).getChildAt(0).setOnClickListener {
                GameData.score++
                scoreText.text = GameData.score.toString()
                v.isEnabled = false
            }
        }

        val resultFragment = ResultFragment()
        object : CountDownTimer(30000, 500) {

            override fun onTick(millisUntilFinished: Long) {
                timer.text = (millisUntilFinished / 1000 + 1).toString()

                val fl = holes.children.elementAt(Random.nextInt(0, holes.children.count()))
                val v = (fl as FrameLayout).getChildAt(0)
                v.isEnabled = true
                v.visibility = View.VISIBLE

                ObjectAnimator.ofInt(v,"visibility", View.INVISIBLE).apply {
                    duration = 500
                    start()
                }
            }

            override fun onFinish() {
                if(GameData.score > GameData.highscore)
                    GameData.highscore = GameData.score

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, resultFragment)
                    ?.commit()
            }
        }.start()

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
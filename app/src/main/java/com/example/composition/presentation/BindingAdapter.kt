package com.example.composition.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.composition.R

interface OnOptionsClickListener{
    fun onOptionClick(value: Int)
}

@BindingAdapter("setEmojiResult")
fun bindEmojiResult(imageView: ImageView, isWinner: Boolean) {
    if (isWinner) {
        imageView.setImageResource(R.drawable.ic_smile)
    } else {
        imageView.setImageResource(R.drawable.ic_sad)
    }
}

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, countOfRightAnswers: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_score),
        countOfRightAnswers
    )
}

@BindingAdapter("scoreAnswers")
fun bindScoreOfRightAnswers(textView: TextView, countOfRightAnswers: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.score_answers),
        countOfRightAnswers
    )
}

@BindingAdapter("minPercentOfRightAnswers")
fun bindMinPercentOfRightAnswers(textView: TextView, minPercentOfRightAnswers: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_percentage),
        minPercentOfRightAnswers
    )
}

@BindingAdapter("countPercentOfRightAnswers")
fun bindCountPercentOfRightAnswers(textView: TextView, percentOfRightAnswers: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.score_percentage),
        percentOfRightAnswers
    )
}

@BindingAdapter("setSum")
fun bindSum(textView: TextView, sum: Int) {
    textView.text = sum.toString()
}

@BindingAdapter("setVisibleNumber")
fun bindVisibleNumber(textView: TextView, number: Int) {
    textView.text = number.toString()
}

@BindingAdapter("setProgress")
fun bindProgress(progressBar: ProgressBar, value: Int) {
    progressBar.setProgress(value, true)
}

@BindingAdapter("setSecondaryProgress")
fun bindSecondaryProgress(progressBar: ProgressBar, value: Int) {
    progressBar.secondaryProgress = value
}

@BindingAdapter("enoughCountOfRightAnswers")
fun bindColorTitle(textView: TextView, isEnough: Boolean) {
    val color = getColorByState(isEnough, textView.context)
    textView.setTextColor(color)
}

@BindingAdapter("enoughPercentOfRightAnswers")
fun bindColorProgressBar(progressBar: ProgressBar, isEnough: Boolean) {
    val color = getColorByState(isEnough, progressBar.context)
    progressBar.progressTintList = ColorStateList.valueOf(color)
}

@BindingAdapter("progressAnswers")
fun bindProgressAnswers(textView: TextView, message: String) {
    textView.text = message
}

@BindingAdapter("onOptionClickListener")
fun bindOnOptionClickListener(textView: TextView, clickListener: OnOptionsClickListener) {
    textView.setOnClickListener {
        clickListener.onOptionClick(textView.text.toString().toInt())
    }
}

private fun getColorByState(goodState: Boolean, context: Context): Int {
    val colorResId = if (goodState) {
        android.R.color.holo_green_light
    } else {
        android.R.color.holo_red_light
    }
    return ContextCompat.getColor(context, colorResId)
}
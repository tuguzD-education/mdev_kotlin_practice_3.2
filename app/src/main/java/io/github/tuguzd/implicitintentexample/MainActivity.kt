package io.github.tuguzd.implicitintentexample

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import io.github.tuguzd.implicitintentexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            action.setOnClickListener {
                val text = editText.text?.toString().orEmpty()
                when {
                    text.isEmpty() -> {
                        showSnackbar(root, "Text should not be empty")
                        return@setOnClickListener
                    }
                    browser.isChecked -> openBrowser(text)
                    map.isChecked -> openMap(text)
                    call.isChecked -> when (val number = text.toLongOrNull()) {
                        null -> showSnackbar(root, "Number should contain only digits")
                        else -> callByNumber(number)
                    }
                    else -> actionByText(text)
                }
            }
        }
    }

    private fun openBrowser(query: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, query.toUri())
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            showSnackbar(binding.root, "Cannot get result for query \"$query\"")
        }
    }

    private fun openMap(query: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, "geo:0,0?q=$query".toUri())
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            showSnackbar(binding.root, "Cannot get result for map query \"$query\"")
        }
    }

    private fun callByNumber(number: Long) {
        try {
            val intent = Intent(Intent.ACTION_DIAL, "tel:$number".toUri())
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            showSnackbar(binding.root, "Cannot get result for number \"$number\"")
        }
    }

    private fun actionByText(text: String) {
        text.toLongOrNull()?.let {
            callByNumber(it)
            return
        }
        if (text.startsWith("http://") || text.startsWith("https://")) {
            openBrowser(text)
            return
        }
        openMap(text)
    }
}

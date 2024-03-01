import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.FragmentSharedViewModel
import com.example.todolist.R

class SecondFragment:Fragment(R.layout.fragment_second) {
    private lateinit var sharedViewModel: FragmentSharedViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(FragmentSharedViewModel::class.java)

        val currentView = getView()
        if (currentView != null) {
            val fragment2Text = currentView.findViewById<TextView>(R.id.secondFragment)
            sharedViewModel.data.observe(viewLifecycleOwner) { newData ->
                // Update UI or perform actions based on newData
                fragment2Text.text = getString(R.string.fragmentText, 2, newData)
            }
        }
    }
}
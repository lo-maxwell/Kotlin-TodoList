import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.FragmentSharedViewModel
import com.example.todolist.R

class ThirdFragment:Fragment(R.layout.fragment_third) {
    private lateinit var sharedViewModel: FragmentSharedViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(FragmentSharedViewModel::class.java)

        val currentView = getView()
        if (currentView != null) {
            val fragment3Text = currentView.findViewById<TextView>(R.id.thirdFragment)
            sharedViewModel.data.observe(viewLifecycleOwner) { newData ->
                // Update UI or perform actions based on newData
                fragment3Text.text = getString(R.string.fragmentText, 3, newData)
            }
        }
    }
}
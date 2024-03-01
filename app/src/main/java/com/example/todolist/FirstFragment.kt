import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.FragmentDBHelper
import com.example.todolist.FragmentSharedViewModel
import com.example.todolist.R
import com.example.todolist.TodoDBHelper

class FirstFragment:Fragment(R.layout.fragment_first) {
    private lateinit var sharedViewModel: FragmentSharedViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(FragmentSharedViewModel::class.java)

        val currentView = getView()

        if (currentView != null) {
            val inputBox = currentView.findViewById<EditText>(R.id.editTextInputBox)
            val updateButton = currentView.findViewById<Button>(R.id.ButtonAddTODO)
            val fragment1Text = currentView.findViewById<TextView>(R.id.firstFragment)
            val db = FragmentDBHelper(requireContext(), null)
            updateButton.setOnClickListener {
                if(inputBox.text.toString().isNotEmpty()) {
                    sharedViewModel.updateData(inputBox.text.toString())
                    db.updateItem(1, inputBox.text.toString())
                    inputBox.text.clear()
                }
            }

            sharedViewModel.data.observe(viewLifecycleOwner) { newData ->
                // Update UI or perform actions based on newData
                fragment1Text.text = getString(R.string.fragmentText, 1, newData)

            }
            val fragmentTextString = db.loadAndParseItemsOnInit()
            if (fragmentTextString.isNotEmpty()) {
                sharedViewModel.updateData(fragmentTextString)
                fragment1Text.text = getString(R.string.fragmentText, 1, fragmentTextString)
            } else {
                sharedViewModel.updateData("Text")
                fragment1Text.text = getString(R.string.fragmentText, 1, "Text")
            }
        }
    }
}
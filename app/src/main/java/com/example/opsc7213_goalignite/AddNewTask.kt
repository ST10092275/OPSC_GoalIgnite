package com.example.opsc7213_goalignite

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.opsc7213_goalignite.utilis.DatabaseHandler
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.opsc7213_goalignite.model.ToDoModel


//THIS WHOLE CODE WAS TAKEN FROM YOUTUBE
//https://www.youtube.com/watch?v=7u5_NNrbQos&list=PLzEWSvaHx_Z2MeyGNQeUCEktmnJBp8136
//Penguin Coders - TO-DO-LIST APPLICATION
class AddNewTask : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ActionBottomDialog"

        fun newInstance(): AddNewTask {
            return AddNewTask()
        }
    }

    private lateinit var newTaskText: EditText
    private lateinit var newTaskSaveButton: Button
    private lateinit var db: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.new_document, container, false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newTaskText = view.findViewById(R.id.newDocumentText)
        newTaskSaveButton = view.findViewById(R.id.newDocumentButton)

        db = DatabaseHandler(requireActivity())

        var isUpdate = false
        val bundle = arguments
        if (bundle != null) {
            isUpdate = true
            val task = bundle.getString("task")
            newTaskText.setText(task)
            if (task?.isNotEmpty() == true) {
                newTaskSaveButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.baby_blue
                    )
                )
            }
        }

        newTaskText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, after: Int) {
                if (s.isNullOrEmpty()) {
                    newTaskSaveButton.isEnabled = false
                    newTaskSaveButton.setTextColor(Color.GRAY)
                } else {
                    newTaskSaveButton.isEnabled = true
                    newTaskSaveButton.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.baby_blue
                        )
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        newTaskSaveButton.setOnClickListener {
            val text = newTaskText.text.toString()
            if (isUpdate) {
                db.updateTask(bundle?.getInt("id") ?: 0, text)
            } else {
                val task = ToDoModel(task = text, status = 0)
                db.insertTask(task)
            }
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as? DialogCloseListener)?.handleDialogClose(dialog)
    }
}
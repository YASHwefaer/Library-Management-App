package com.example.library_menagement_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.library_menagement_app.Activity.StudentActivity
import com.example.library_menagement_app.Model.Student
import com.example.library_menagement_app.databinding.ItemStudentBinding

class StudentAdapter(
    private val studentList: ArrayList<Student>,
    private val activity: StudentActivity
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(
        val binding: ItemStudentBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentViewHolder {

        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: StudentViewHolder,
        position: Int
    ) {

        val student = studentList[position]

        holder.binding.tvStudentName.text = student.name

        holder.binding.tvStudentEmail.text = "Email: ${student.email}"

        holder.binding.tvStudentPhone.text = "Phone: ${student.phone}"

        holder.binding.btnEdit.setOnClickListener {
            activity.editStudent(student)
        }

        holder.binding.btnDelete.setOnClickListener {
            activity.deleteStudent(student)
        }
    }

    override fun getItemCount(): Int {
        return studentList.size
    }
}
package com.example.library_menagement_app.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.library_menagement_app.Activity.Viewissue
import com.example.library_menagement_app.Model.Issue
import com.example.library_menagement_app.databinding.ItemissueBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class IssueAdapter(
    private val issuelist: ArrayList<Issue>,
    private val activity: Viewissue
) : RecyclerView.Adapter<IssueAdapter.Viewholder>() {

    class Viewholder(
        val binding: ItemissueBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {

        val binding = ItemissueBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return Viewholder(binding)
    }

    override fun onBindViewHolder(
        holder: Viewholder,
        position: Int
    ) {

        val issue = issuelist[position]

        holder.binding.tvStudentName.text =
            "Student: ${issue.studentName}"

        holder.binding.tvBookName.text =
            "Book: ${issue.bookName}"

        holder.binding.tvStartDate.text =
            "Start Date: ${issue.startDate}"

        holder.binding.tvEndDate.text =
            "End Date: ${issue.endDate}"

        val today = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(Date())

        if (issue.endDate < today) {
            holder.binding.cardIssue.setCardBackgroundColor(
                Color.RED
            )
        } else {
            holder.binding.cardIssue.setCardBackgroundColor(
                Color.rgb(24, 38, 58)
            )
        }

        holder.binding.btnReturn.setOnClickListener {
            activity.returnBook(issue)
        }
    }

    override fun getItemCount(): Int {
        return issuelist.size
    }
}
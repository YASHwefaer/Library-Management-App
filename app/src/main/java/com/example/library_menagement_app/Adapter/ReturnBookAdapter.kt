package com.example.library_menagement_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.library_menagement_app.Model.ReturnBook
import com.example.library_menagement_app.databinding.ItemreturnbookbindingBinding

class ReturnBookAdapter(
    private val returnBookList: ArrayList<ReturnBook>
) : RecyclerView.Adapter<ReturnBookAdapter.ReturnBookViewHolder>() {

    class ReturnBookViewHolder(
        val binding: ItemreturnbookbindingBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReturnBookViewHolder {

        val binding = ItemreturnbookbindingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ReturnBookViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ReturnBookViewHolder,
        position: Int
    ) {

        val returnBook = returnBookList[position]

        holder.binding.tvBookName.text = "Book: ${returnBook.bookName}"

        holder.binding.tvStudentName.text = "Student: ${returnBook.studentName}"

        holder.binding.tvStartDate.text = "Issue Date: ${returnBook.startDate}"

        holder.binding.tvEndDate.text = "Due Date: ${returnBook.endDate}"

        holder.binding.tvReturnDate.text = "Return Date: ${returnBook.returnDate}"
    }

    override fun getItemCount(): Int {
        return returnBookList.size
    }
}
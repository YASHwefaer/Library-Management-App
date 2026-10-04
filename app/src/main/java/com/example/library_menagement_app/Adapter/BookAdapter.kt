package com.example.library_menagement_app.Adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.library_menagement_app.Model.Book
import com.example.library_menagement_app.databinding.ItemBookBinding
import java.io.File

class BookAdapter(
    private val bookList: ArrayList<Book>,
    private val listener: OnBookClick
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    interface OnBookClick {

        fun editBook(book: Book)

        fun deleteBook(book: Book)
    }

    class BookViewHolder(val binding: ItemBookBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookViewHolder {

        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BookViewHolder,
        position: Int
    ) {

        val book = bookList[position]

        holder.binding.tvBookName.text = book.name

        holder.binding.tvAuthor.text = "Author: " + book.author

        holder.binding.tvisbn.text = "ISBN: " + book.isbn

        holder.binding.tvCategory.text = "Category: " + book.category

        holder.binding.tvQuantity.text = "Quantity: " + book.quantity

        val file = File(book.image)

        if (file.exists()) {

            val bitmap = BitmapFactory.decodeFile(
                file.absolutePath
                )

            holder.binding.imgBook.setImageBitmap(
                bitmap
            )

        } else {

            holder.binding.imgBook.setImageResource(
                com.example.library_menagement_app.R.drawable.book1
            )
        }

        holder.binding.btnEdit.setOnClickListener {

            listener.editBook(book)
        }

        holder.binding.btnDelete.setOnClickListener {

            listener.deleteBook(book)
        }
    }

    override fun getItemCount(): Int {

        return bookList.size
    }
}
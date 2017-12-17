package com.tcc.mensageria.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tcc.mensageria.R
import com.tcc.mensageria.model.MensagemDTO
import com.tcc.mensageria.utils.formatHour
import kotlinx.android.synthetic.main.item_message_received.view.*


/**
 * Adapter do recycler view de mensagens
 */
class ConversaAdapter(val context: Context, val idAutor: Long) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_MESSAGE_SENT = 1
    private val VIEW_TYPE_MESSAGE_RECEIVED = 2
    private val VIEW_TYPE_MESSAGE_RECEIVED_PRIORITY = 3

    // itens para popular a lista
    var dados: List<MensagemDTO> = ArrayList()
        set (new) {
            if (new != field) {
                field = new
                notifyDataSetChanged()
            }
        }

    override fun getItemCount(): Int = dados.size

    override fun getItemViewType(position: Int): Int {
        val mensagem = dados[position]

        if (mensagem.prioridade > 0) {
            return VIEW_TYPE_MESSAGE_RECEIVED_PRIORITY
        }

        return if (mensagem.idAutor == idAutor) {
            // If the current user is the sender of the message
            VIEW_TYPE_MESSAGE_SENT
        } else {
            // If some other user sent the message
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        val view: View

        return when (viewType) {
            VIEW_TYPE_MESSAGE_SENT -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_sent, parent, false)
                SentMessageHolder(view)
            }
            VIEW_TYPE_MESSAGE_RECEIVED -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_received, parent, false)
                ReceivedMessageHolder(view)
            }
            VIEW_TYPE_MESSAGE_RECEIVED_PRIORITY -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_received_priority, parent, false)
                ReceivedMessageHolder(view)
            }
            else -> {
                null
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = dados[position]

        when (holder.itemViewType) {
            VIEW_TYPE_MESSAGE_SENT -> (holder as SentMessageHolder).bind(message)
            VIEW_TYPE_MESSAGE_RECEIVED, VIEW_TYPE_MESSAGE_RECEIVED_PRIORITY -> (holder as ReceivedMessageHolder).bind(message)
        }
    }

    private inner class SentMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText = itemView.text_message_body
        val timeText = itemView.text_message_time

        internal fun bind(message: MensagemDTO) {
            messageText.text = message.conteudo
            timeText.text = message.dataEnvio?.formatHour()
        }
    }

    private inner class ReceivedMessageHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText = itemView.text_message_body
        val timeText = itemView.text_message_time
        val nameText = itemView.text_message_name
        val profileImage = itemView.image_message_profile


        internal fun bind(message: MensagemDTO) {
            messageText.text = message.conteudo
            timeText.text = message.dataEnvio?.formatHour()
            nameText.text = message.nomeAutor
        }
    }

}
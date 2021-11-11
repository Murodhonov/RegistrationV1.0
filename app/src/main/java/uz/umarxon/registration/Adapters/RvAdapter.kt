package uz.umarxon.registration.Adapters

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.umarxon.registration.Models.User
import uz.umarxon.registration.databinding.RvItemBinding

class RvAdapter(private val list: List<User>,var click:Click) : RecyclerView.Adapter<RvAdapter.Vh>() {
    inner class Vh(var itemRv: RvItemBinding) : RecyclerView.ViewHolder(itemRv.root) {
        fun onBind(user: User, position: Int) {
            itemRv.rvName.text = user.name
            itemRv.rvPhone.text = user.phone
            itemRv.rvImage.setImageURI(Uri.parse(user.image))


            itemRv.card.setOnClickListener {
                click.click(user)
            }
        }
    }

    interface Click{
        fun click(user:User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
}
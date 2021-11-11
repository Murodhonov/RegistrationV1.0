package uz.umarxon.registration.Fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*
import uz.umarxon.registration.Adapters.RvAdapter
import uz.umarxon.registration.DB.DbHelper
import uz.umarxon.registration.Models.User
import uz.umarxon.registration.R
import uz.umarxon.registration.databinding.BottomSheetDialogBinding
import uz.umarxon.registration.databinding.FragmentListBinding
import uz.umarxon.registration.databinding.RvItemBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {


    lateinit var binding : FragmentListBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(LayoutInflater.from(context))


        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.rv.adapter = RvAdapter(DbHelper(binding.root.context).getAllMember(),object :RvAdapter.Click{
            override fun click(user: User) {
                val dialog = BottomSheetDialog(binding.root.context,R.style.MyBottomSheet)

                val view = LayoutInflater.from(binding.root.context).inflate(R.layout.bottom_sheet_dialog,null,false)

                dialog.setContentView(view)

                dialog.setCancelable(true)

                view.dialog_image.setImageURI(Uri.parse(user.image))

                val country = when(user.countries){
                    1->{
                        "O'zbekiston"
                    }
                    2->{
                        "USA"
                    }
                    else->{
                        "Россия"
                    }
                }
                view.dialog_name.text = user.name
                view.dialog_address.text = "${user.address},$country"

                view.sms.setOnClickListener {
                    context?.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${user.phone}")))
                }

                view.call.setOnClickListener {
                    context?.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${user.phone}")))
                }

                dialog.show()
            }
        })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
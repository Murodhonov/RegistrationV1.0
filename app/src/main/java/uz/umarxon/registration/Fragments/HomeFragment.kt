package uz.umarxon.registration.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.view.*
import uz.umarxon.registration.DB.DbHelper
import uz.umarxon.registration.Models.User
import uz.umarxon.registration.R
import uz.umarxon.registration.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private lateinit var root: View
    private lateinit var binding:FragmentHomeBinding

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

    override fun onStart() {
        super.onStart()
        binding.edtNumber.text.clear()
        binding.edtPassword.text.clear()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.bind(root)


        root.register.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }

        root.enter_btn.setOnClickListener {
            val number = binding.edtNumber.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            if(number.isNotEmpty()){
                if (password.isNotEmpty()){

                    val db = DbHelper(binding.root.context)
                    val list = db.getAllMember()
                    var isHave = false
                    var notHave = true
                    var incorrectPassword = true
                    var user = User()

                    for (i in list.indices){
                        if (list[i].phone == number){
                            if (list[i].password == password){
                                user = list[i]
                                isHave = true
                                incorrectPassword = false
                                break
                            }
                            notHave = false
                        }
                    }

                    if (isHave){
                        findNavController().navigate(R.id.listFragment, bundleOf("user" to user))
                        Snackbar.make(binding.root,"Xush kelibsiz ${user.name}",Snackbar.LENGTH_SHORT).show()
                    }else{
                        if (notHave){
                            Snackbar.make(binding.root,"Foydalanuvchi topilmadi !!",Snackbar.LENGTH_SHORT).show()
                        }else if (incorrectPassword){
                            Snackbar.make(binding.root,"Login yoki parol noto'g'ri",Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Snackbar.make(binding.root,"Enter password",Snackbar.LENGTH_SHORT).show()
                }
            }else{
                Snackbar.make(binding.root,"Enter phone number",Snackbar.LENGTH_SHORT).show()
            }
        }


        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
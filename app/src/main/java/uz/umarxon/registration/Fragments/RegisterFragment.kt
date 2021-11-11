package uz.umarxon.registration.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.florent37.runtimepermission.kotlin.askPermission
import uz.umarxon.registration.BuildConfig
import uz.umarxon.registration.DB.DbHelper
import uz.umarxon.registration.Models.User
import uz.umarxon.registration.R
import uz.umarxon.registration.databinding.FragmentRegisterBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {

    private lateinit var root: View
    private lateinit var binding: FragmentRegisterBinding

    lateinit var currentImagePath: String
    val REQUEST_CODE = 1
    lateinit var photoUri: Uri

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

    @SuppressLint("SimpleDateFormat")
    private val getTakeImageContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()){
            if (it) {
                val date= SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                binding.image.setImageURI(photoUri)
                val file = File(activity?.filesDir, "$date.jpg")
                val inputStream = activity?.contentResolver?.openInputStream(photoUri)
                val fileOutputStream = FileOutputStream(file)
                inputStream?.copyTo(fileOutputStream)
                inputStream?.close()
                fileOutputStream.close()
                val absolutePath = file.absolutePath
                Toast.makeText(context, "$absolutePath", Toast.LENGTH_SHORT).show()
            }
        }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val format = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val externalFilesDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        println("createImageFile ishlayapti")
        return File.createTempFile("JPEG_$format", ".jpg", externalFilesDir).apply {
            currentImagePath = absolutePath
        }
    }

    override fun onStart() {
        super.onStart()

        binding.image.setOnClickListener {
            askPermission(Manifest.permission.CAMERA) {
                //all permissions already granted or just granted

                val imageFile = createImageFile()
                photoUri = FileProvider.getUriForFile(root.context, BuildConfig.APPLICATION_ID, imageFile)
                println("mageFile: $imageFile, photoUri = $photoUri")
                getTakeImageContent.launch(photoUri)

            }.onDeclined { e ->
                if (e.hasDenied()) {

                   e.askAgain()

                }

                if (e.hasForeverDenied()) {
                    //the list of forever denied permissions, user has check 'never ask again'

                    // you need to open setting manually if you really need it
                    e.goToSettings();
                }
            }
        }

        binding.enterBtn.setOnClickListener {

            val name = binding.name.text.toString().trim()
            val number = binding.phone.text.toString().trim()
            val country = binding.country.selectedItemPosition
            val address = binding.address.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (name != "") {
                if (number != "") {
                    if (country != 0) {
                        if (address != "") {
                            if (password != "") {


                                val user = User(name, number,currentImagePath, country, address, password)
                                DbHelper(root.context).addMember(user)
                                Toast.makeText(context, "User Created !", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.listFragment, bundleOf("user" to user))

                            } else {
                                binding.password.error = "Enter password"
                                Toast.makeText(context, "Enter password !!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            binding.address.error = "Enter address"
                            Toast.makeText(context, "Enter address !!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Select country !!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    binding.phone.error = "Enter phone number"
                    Toast.makeText(context, "Enter phone number !!", Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.name.error = "Enter name"
                Toast.makeText(context, "Enter name !!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_register, container, false)
        binding = FragmentRegisterBinding.bind(root)



        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
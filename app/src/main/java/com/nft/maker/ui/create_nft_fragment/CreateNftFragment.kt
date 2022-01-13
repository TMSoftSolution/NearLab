package com.nft.maker.ui.create_nft_fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.nft.maker.BaseFragment
import com.nft.maker.R
import com.nft.maker.databinding.FragmentCreateNftBinding
import com.nft.maker.utils.ValidationAlert
import com.bumptech.glide.Glide

class CreateNftFragment : BaseFragment() {
    private lateinit var fragmentCreateNftBinding: FragmentCreateNftBinding
    var selectedImage: Uri? = null

    override val layoutId: View
        get() {
            fragmentCreateNftBinding =
                FragmentCreateNftBinding.inflate(layoutInflater)
            return fragmentCreateNftBinding.root
        }
    override val layoutTag: String?
        get() = CreateNftFragment::class.java.simpleName

    override fun initUI(view: View) {
        initiate()
        setListener()
        onBackPressed()
    }
    fun onBackPressed() {
        fragmentCreateNftBinding.backButton.setOnClickListener {
            findNavController().popBackStack()

        }
        view.isFocusableInTouchMode = true;
        view.requestFocus()
        view.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() === KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        findNavController().popBackStack()
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun initiate() {
        val spinnerArray = ArrayList<String>()
        spinnerArray.add("Digital Arts")

        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            spinnerArray
        )
        fragmentCreateNftBinding.spinner.adapter = spinnerArrayAdapter
    }

    private fun setListener() {
        fragmentCreateNftBinding.card1.setOnClickListener {
            if (selectedImage==null){
                ValidationAlert.getValidationDialog(requireContext(),false,"Alert","Please select image!")
            }else if (TextUtils.isEmpty(fragmentCreateNftBinding.etTitle.text.toString())){
                ValidationAlert.getValidationDialog(requireContext(),false,"Alert","Please enter tittle!")


            }else if (TextUtils.isEmpty(fragmentCreateNftBinding.etDescription.text.toString())){
                ValidationAlert.getValidationDialog(requireContext(),false,"Alert","Please enter description!")


            }else{
                val bundles: Bundle = Bundle().apply {
                    putString("key", arguments?.getString("key"))
                    putString("image", selectedImage.toString())
                    putSerializable("tittle", fragmentCreateNftBinding.etTitle.text.toString())
                    putSerializable("description", fragmentCreateNftBinding.etDescription.text.toString())
                }
                if (arguments?.getString("key") == "true"){

                    findNavController().navigate(R.id.action_createNftFragment2_to_previewNftFragment2 , bundles)
                }else{
                    findNavController().navigate(R.id.action_createNftFragment_to_previewNftFragment,bundles)
                }

            }
        }

        fragmentCreateNftBinding.choseImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2)

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode === Activity.RESULT_OK) {
            if (requestCode === 2) {
                selectedImage = data!!.data
                val bitmap = MediaStore.Images.Media.getBitmap(
                    requireContext().contentResolver, selectedImage
                )
                Glide.with(requireActivity())
                    .load(selectedImage)
                    .into(fragmentCreateNftBinding.image)

                //  fragmentProfileScreenBinding.profileImage.setImageBitmap(bitmap)
            }
        }
    }

}